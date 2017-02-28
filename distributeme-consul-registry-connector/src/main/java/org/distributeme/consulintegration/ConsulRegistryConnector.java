package org.distributeme.consulintegration;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.distributeme.core.Location;
import org.distributeme.core.RegistryConnector;
import org.distributeme.core.RegistryLocation;
import org.distributeme.core.ServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by rboehling on 2/28/17.
 */
public class ConsulRegistryConnector implements RegistryConnector {

	private Logger logger = LoggerFactory.getLogger(ConsulRegistryConnector.class);

	static final String TAG_INSTANCE_ID = "instanceId";
	static final String TAG_PROTOCOL = "protocol";
	static final String TAG_TIMESTAMP = "timestamp";

	private RegistryLocation registryLocation = RegistryLocation.create();

	@Override
	public String describeRegistry() {
		return "Consul@" + getRegistryUrl();
	}

	@Override
	public boolean bind(ServiceDescriptor service) {
		ClientResponse response = null;
		try {

			String requestAsJsonString = new Gson().toJson(new ConsulServiceDescription(service));

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

	private String getRegistryUrl() {
		return registryLocation.getRegistryContainerProtocol() + "://" + registryLocation.getRegistryContainerHost() + ":" + registryLocation.getRegistryContainerPort();
	}

	@Override
	public boolean notifyBind(Location location, ServiceDescriptor descriptor) {
		return false;
	}

	@Override
	public boolean notifyUnbind(Location location, ServiceDescriptor descriptor) {
		return false;
	}

	@Override
	public boolean unbind(ServiceDescriptor service) {
		ClientResponse response = null;
		try {
			WebResource webResource = Client.create().resource(getRegistryUrl() + "/v1/agent/service/deregister/" + service.getServiceId());

			response = webResource.accept("application/json").get(ClientResponse.class);

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

			WebResource webResource = Client.create().resource(getRegistryUrl() + "/v1/catalog/service/" + toResolve.getServiceId());

			response = webResource.accept("application/json").get(ClientResponse.class);

			if (response.getStatus() != 200) {
				logger.error("Failed : HTTP error code : " + response.getStatus());
			}
			ServiceResolveReply serviceResolveReply = getServiceReplyFromConsulResponse(response);
			if(serviceResolveReply == null) {
				return null;
			}
			return serviceResolveReply.createServiceDescriptor();
		} catch (Exception e) {
			logger.error("Could not resolve ServiceDescriptor: ", e);
		} finally {
			closeResponseNullSafe(response);
		}
		return null;
	}

	private ServiceResolveReply getServiceReplyFromConsulResponse(ClientResponse response) {
		String responseAsString = response.getEntity(String.class);
		ServiceResolveReply[] serviceResolveReplies = new Gson().fromJson(responseAsString, ServiceResolveReply[].class);
		if(serviceResolveReplies.length == 0) {
			return null;
		}
		return serviceResolveReplies[0];
	}

	private void closeResponseNullSafe(ClientResponse response) {
		if(response != null) {
			response.close();
		}
	}

}
