package org.distributeme.consulintegration;

import com.google.gson.Gson;
import jakarta.ws.rs.core.Response;
import org.distributeme.core.ServiceDescriptor;


/**
 * Created by rboehling on 3/30/17.
 */
public class ServiceDescriptorFactory {

	private ServiceDescriptorFactory() {
	}

	public static ServiceDescriptor createFrom(Response response) {
		String responseAsString = (String)response.getEntity();
		ServiceResolveReply[] serviceResolveReplies = new Gson().fromJson(responseAsString, ServiceResolveReply[].class);
		if(serviceResolveReplies == null || serviceResolveReplies.length == 0) {
			return null;
		}
		ServiceResolveReply serviceResolveReply = getLatestServiceInstance(serviceResolveReplies);
		if(serviceResolveReply == null) {
			return null;
		}
		return serviceResolveReply.createServiceDescriptor();
	}

	private static ServiceResolveReply getLatestServiceInstance(ServiceResolveReply[] serviceResolveReplies) {
		ServiceResolveReply tmpResult = serviceResolveReplies[0];
		for (ServiceResolveReply reply: serviceResolveReplies) {
			if(reply.isYoungerThan(tmpResult)) {
				tmpResult = reply;
			}
		}
		return tmpResult;
	}
}
