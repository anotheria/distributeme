package org.distributeme.consulintegration;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.distributeme.core.RegistryLocation;
import org.distributeme.core.ServiceDescriptor;


/**
 * Created by rboehling on 2/28/17.
 */
public class ConsulRestClient {

	private RegistryLocation registryLocation = RegistryLocation.create();

	ClientResponse unbind(ServiceDescriptor service) {
		WebResource webResource = Client.create().resource(getRegistryUrl() + "/v1/agent/service/deregister/" + service.getServiceId());

		return webResource.accept("application/json").get(ClientResponse.class);
	}

	ClientResponse bind(ServiceDescriptor service) {
		String requestAsJsonString = new Gson().toJson(new ConsulServiceDescription(service));

		WebResource webResource = Client.create()
										.resource(getRegistryUrl() + "/v1/agent/service/register");

		return webResource.accept("application/json")
							  .put(ClientResponse.class, requestAsJsonString);
	}

	ClientResponse resolve(ServiceDescriptor toResolve) {
		WebResource webResource = Client.create().resource(getRegistryUrl() + "/v1/catalog/service/" + toResolve.getServiceId());

		return webResource.accept("application/json").get(ClientResponse.class);
	}

	String getRegistryUrl() {
		return registryLocation.getRegistryContainerProtocol() + "://" + registryLocation.getRegistryContainerHost() + ":" + registryLocation.getRegistryContainerPort();
	}
}
