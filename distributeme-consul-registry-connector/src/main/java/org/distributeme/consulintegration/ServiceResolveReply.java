package org.distributeme.consulintegration;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import net.anotheria.util.StringUtils;
import org.distributeme.core.ServiceDescriptor;


/**
 *  * Created by rboehling on 2/28/17.
 */
class ServiceResolveReply {

	@SerializedName("ServiceTags")
	private List<String> serviceTags;
	@SerializedName("ServiceAddress")
	private String serviceAddress;
	@SerializedName("ServicePort")
	private int servicePort;
	@SerializedName("ServiceID")
	private String serviceID;


	/**
	 * method to create a service descriptor from a registration string (used for bind in the registry).
	 *
	 * @return a {@link org.distributeme.core.ServiceDescriptor} object.
	 */
	ServiceDescriptor createServiceDescriptor(){
		return new ServiceDescriptor(ServiceDescriptor.Protocol.valueOf(getProtocol().toUpperCase()), ServiceNameTranslator.fromConsul(serviceID), getInstanceId(), serviceAddress, servicePort, getTimestamp());
	}

	boolean isYoungerThan(ServiceResolveReply reply) {
		return getTimestamp() > reply.getTimestamp();
	}

	private List<String> getServiceTags() {
		return serviceTags;
	}

	private String getInstanceId() {
		return getTag(ConsulTag.INSTANCE_ID);
	}

	private String getTag(ConsulTag tag) {
		for (String t : getServiceTags()) {
			String[] tokens = StringUtils.tokenize(t, '=');
			if (tokens[0].equals(tag.getTagName())) {
				return tokens[1];
			}
		}
		throw new IllegalArgumentException("Tag " + tag + " was not found for " + serviceID);
	}

	private String getProtocol() {
		return getTag(ConsulTag.PROTOCOL);
	}

	private long getTimestamp() {
		return Long.parseLong(getTag(ConsulTag.TIMESTAMP));
	}

	@Override
	public String toString() {
		return "ServiceResolveReply{" +
				"serviceTags=" + serviceTags +
				", serviceAddress='" + serviceAddress + '\'' +
				", servicePort=" + servicePort +
				'}';
	}


}
