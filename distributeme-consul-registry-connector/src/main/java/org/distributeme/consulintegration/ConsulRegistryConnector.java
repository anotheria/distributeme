package org.distributeme.consulintegration;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import net.anotheria.util.StringUtils;
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

	private RegistryLocation registryLocation = RegistryLocation.create();
	private static final String TAG_INSTANCE_ID = "instanceId";
	private static final String TAG_PROTOCOL = "protocol";
	private static final String TAG_TIMESTAMP = "timestamp";

	static class ConsulServiceDescription {
		String ID;
		String Name;
		List<String> Tags = new ArrayList<>();
		String Address;
		int Port;
		boolean EnableTagOverride = false;

		void addJmxPort(String jmxPort) {
			Tags.add("jmx=" + jmxPort);
		}

		void addInstanceId(String instanceId) {
			Tags.add(TAG_INSTANCE_ID + "=" + instanceId);
		}

		void addProtocol(String protocol) {
			Tags.add(TAG_PROTOCOL + "=" + protocol);
		}

		void addTimestamp(long timestamp) {
			Tags.add(TAG_TIMESTAMP + "=" + timestamp);
		}
	}

	static class ServiceResolveReply{


		List<String> ServiceTags;
		String ServiceAddress;
		int ServicePort;
		String ServiceID;

		public List<String> getServiceTags() {
			return ServiceTags;
		}

		public String getServiceAddress() {
			return ServiceAddress;
		}

		public int getServicePort() {
			return ServicePort;
		}

		@Override
		public String toString() {
			return "ServiceResolveReply{" +
					"ServiceTags=" + ServiceTags +
					", ServiceAddress='" + ServiceAddress + '\'' +
					", ServicePort=" + ServicePort +
					'}';
		}

		public String getServiceID() {
			return ServiceID;
		}

		public String getInstanceId() {
			return getTag(TAG_INSTANCE_ID);
		}

		private String getTag(String tag) {
			for(String t: getServiceTags()) {
				String[] tokens = StringUtils.tokenize(t, '=');
				if(tokens[0].equals(tag)) {
					return tokens[1];
				}
			}
			throw new IllegalArgumentException("Tag " + tag + " was not found for " + getServiceID());
		}

		public String getProtocol() {
			return getTag(TAG_PROTOCOL);
		}

		public long getTimestamp() {
			return Long.parseLong(getTag(TAG_TIMESTAMP));
		}
	}

	@Override
	public String describeRegistry() {
		return "Consul@" + getRegistryUrl();
	}

	@Override
	public boolean bind(ServiceDescriptor service) {
		ClientResponse response = null;
		try {
			Gson gson = new Gson();
			ConsulServiceDescription serviceDescription = new ConsulServiceDescription();
			serviceDescription.ID = service.getServiceId();
			serviceDescription.Name = service.getServiceId();
			serviceDescription.Port = service.getPort();
			serviceDescription.Address = service.getHost();
			serviceDescription.addInstanceId(service.getInstanceId());
			serviceDescription.addJmxPort("XXXXX");
			serviceDescription.addProtocol(service.getProtocol());
			serviceDescription.addTimestamp(service.getTimestamp());

			String requestAsJsonString = gson.toJson(serviceDescription);

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
			Gson gson = new Gson();
			WebResource webResource = Client.create().resource(getRegistryUrl() + "/v1/catalog/service/" + toResolve.getServiceId());

			response = webResource.accept("application/json").get(ClientResponse.class);

			String output = response.getEntity(String.class);
			ServiceResolveReply[] serviceResolveReplies = gson.fromJson(output, ServiceResolveReply[].class);
			if(serviceResolveReplies.length == 0) {
				return null;
			}
			ServiceResolveReply serviceResolveReply = serviceResolveReplies[0];

			if (response.getStatus() != 200) {
				logger.error("Failed : HTTP error code : " + response.getStatus());
			}
			return fromRegistrationString(serviceResolveReply);
		} catch (Exception e) {
			logger.error("Could not resolve ServiceDescriptor: ", e);
		} finally {
			closeResponseNullSafe(response);
		}
		return null;
	}

	private void closeResponseNullSafe(ClientResponse response) {
		if(response != null) {
			response.close();
		}
	}

	/**
	 * Factory method to create a service descriptor from a registration string (used for bind in the registry).
	 *
	 * @param serviceResolveReply a {@link String} object.
	 * @return a {@link org.distributeme.core.ServiceDescriptor} object.
	 */
	private ServiceDescriptor fromRegistrationString(ServiceResolveReply serviceResolveReply){
		int port = serviceResolveReply.getServicePort();
		String host = serviceResolveReply.getServiceAddress();
		long timestamp = serviceResolveReply.getTimestamp();
		String protocol = serviceResolveReply.getProtocol();
		String serviceId = serviceResolveReply.getServiceID();
		String instanceId = serviceResolveReply.getInstanceId();
		return new ServiceDescriptor(ServiceDescriptor.Protocol.valueOf(protocol.toUpperCase()), serviceId, instanceId, host, port, timestamp);
	}
}
