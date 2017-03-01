package org.distributeme.consulintegration;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import org.distributeme.core.ServiceDescriptor;


/**
 * Created by rboehling on 2/28/17.
 */
class ConsulServiceDescription {

	@SerializedName("ID")
	private String id;
	@SerializedName("Name")
	private String name;
	@SerializedName("Tags")
	private List<String> tags = new ArrayList<>();
	@SerializedName("Address")
	private String address;
	@SerializedName("Port")
	private int port;
	@SerializedName("EnableTagOverride")
	private boolean enableTagOverride = false;

	ConsulServiceDescription(ServiceDescriptor serviceDescriptor) {
		id = serviceDescriptor.getServiceId();
		name = serviceDescriptor.getServiceId();
		port = serviceDescriptor.getPort();
		address = serviceDescriptor.getHost();
		addInstanceId(serviceDescriptor.getInstanceId());
		addJmxPort("XXXXX");
		addProtocol(serviceDescriptor.getProtocol());
		addTimestamp(serviceDescriptor.getTimestamp());
	}

	private void addJmxPort(String jmxPort) {
		tags.add("jmx=" + jmxPort);
	}

	private void addInstanceId(String instanceId) {
		tags.add(ConsulTag.INSTANCE_ID.getTagName() + "=" + instanceId);
	}

	private void addProtocol(String protocol) {
		tags.add(ConsulTag.PROTOCOL.getTagName() + "=" + protocol);
	}

	private void addTimestamp(long timestamp) {
		tags.add(ConsulTag.TIMESTAMP.getTagName() + "=" + timestamp);
	}
}
