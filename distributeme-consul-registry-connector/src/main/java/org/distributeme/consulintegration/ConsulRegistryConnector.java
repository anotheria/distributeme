package org.distributeme.consulintegration;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.distributeme.core.CustomTagProvider;
import org.distributeme.core.Location;
import org.distributeme.core.RegistryConnector;
import org.distributeme.core.RegistryLocation;
import org.distributeme.core.ServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.distributeme.consulintegration.ServiceNameTranslator.toConsul;


/**
 * Connector to use consul service registry with distributeme framework.
 *
 * Consul support is configurable via distributeme.json
 * To activate consul instead of distributeme default registry:
 *  "registryConnectorClazz": "org.distributeme.consulintegration.ConsulRegistryConnector"
 *
 * To configure the location of consul:
 * 	"registryContainerPort": 8500
 *  "registryContainerHost": "localhost"
 *  "registryContainerProtocol" : "http"
 *
 * To configure java system properties as optional tags for consul (comma separated list):
 *  "systemPropertiesToTags": "com.sun.management.jmxremote.port,java.vm.version,configureme.defaultEnvironment,java.version",
 *
 * Created by rboehling on 2/28/17.
 */
public class ConsulRegistryConnector implements RegistryConnector {

	private Logger logger = LoggerFactory.getLogger(ConsulRegistryConnector.class);

	private RegistryLocation registryLocation = RegistryLocation.create();
	private Map<String, String> tagableSystemProperties = new HashMap<>();
	private List<String> customTagProviderClassList = new ArrayList<>();

	@Override
	public String describeRegistry() {
		return "Consul@" + getRegistryUrl();
	}

	@Override
	public boolean bind(ServiceDescriptor service) {
		ClientResponse response = null;
		try {
			List<String> customTagList = createCustomTagsFromProvidedClassList();
			String requestAsJsonString = new Gson().toJson(new ConsulServiceDescription(service, tagableSystemProperties, customTagList));

			WebResource webResource = Client.create()
											.resource(getRegistryUrl() + "/v1/agent/service/register");

			response = webResource.accept("application/json")
								  .put(ClientResponse.class, requestAsJsonString);
			if (response.getStatus() != 200) {
				logger.error("Registry returns status: " + response.getStatus());
				return false;
			}

			return true;
		} catch (RuntimeException e) {
			logger.error("Could not bind service " + service);
			return false;
		} finally {
			closeResponseNullSafe(response);
		}
	}

	/**
	 * Instantiate given Classes and create tags
	 *
	 */
	private List<String> createCustomTagsFromProvidedClassList() {

		List<String> customTagsList=new ArrayList<>();
		for (String className : customTagProviderClassList) {

			try {
				Class customTagProviderClass = null;
				customTagProviderClass = Class.forName(className);
				CustomTagProvider customTagProvider = null;
				customTagProvider = (CustomTagProvider)customTagProviderClass.newInstance();
				String customTag = customTagProvider.getTag();
				if(customTag!= null) {
					customTagsList.add(customTag);
				}
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ClassCastException e) {
				logger.error("Could NOT create instance of CustomTagProvider class : '"+className+"'");
			}


		}
		return customTagsList;
	}

	@Override
	public boolean unbind(ServiceDescriptor service) {
		ClientResponse response = null;
		try {
			WebResource webResource = Client.create().resource(getRegistryUrl() + "/v1/agent/service/deregister/" + toConsul(service.getServiceId()));

			response = webResource.accept("application/json").put(ClientResponse.class);
			if (response.getStatus() != 200) {
				logger.error("Registry returns status: " + response.getStatus());
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error("Could not unbind service " + service, e);
			return false;
		} finally {
			closeResponseNullSafe(response);
		}
	}

	@Override
	public ServiceDescriptor resolve(ServiceDescriptor toResolve, Location loc) {
		ClientResponse response = null;
		try {
			WebResource webResource = Client.create().resource(getRegistryUrl() + "/v1/catalog/service/" + toConsul(toResolve.getServiceId()));

			response = webResource.accept("application/json").get(ClientResponse.class);
			if (response.getStatus() != 200) {
				logger.error("Failed : HTTP error code : " + response.getStatus());
			}
			return ServiceDescriptorFactory.createFrom(response);
		} catch (Exception e) {
			logger.error("Could not resolve ServiceDescriptor: ", e);
		} finally {
			closeResponseNullSafe(response);
		}
		return null;
	}

	@Override
	public void setTagableSystemProperties(Map<String, String> tagableSystemProperties) {
		this.tagableSystemProperties = tagableSystemProperties;
	}

	@Override
	public void setCustomTagProviderClassList(List<String> customTagProviderClassList) {
		this.customTagProviderClassList=customTagProviderClassList;
	}

	@Override
	public boolean notifyBind(Location location, ServiceDescriptor descriptor) {
		return false;
	}

	@Override
	public boolean notifyUnbind(Location location, ServiceDescriptor descriptor) {
		return false;
	}

	private void closeResponseNullSafe(ClientResponse response) {
		if(response != null) {
			response.close();
		}
	}

	private String getRegistryUrl() {
		return registryLocation.getRegistryContainerProtocol() + "://" + registryLocation.getRegistryContainerHost() + ":" + registryLocation.getRegistryContainerPort();
	}

}
