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

	ConsulServiceDescription(ServiceDescriptor service) {
		id = service.getServiceId();
		name = service.getServiceId();
		port = service.getPort();
		address = service.getHost();
		addInstanceId(service.getInstanceId());
		addJmxPort("XXXXX");
		addProtocol(service.getProtocol());
		addTimestamp(service.getTimestamp());
	}

	private void addJmxPort(String jmxPort) {
		tags.add("jmx=" + jmxPort);
	}

	private void addInstanceId(String instanceId) {
		tags.add(ConsulRegistryConnector.TAG_INSTANCE_ID + "=" + instanceId);
	}

	private void addProtocol(String protocol) {
		tags.add(ConsulRegistryConnector.TAG_PROTOCOL + "=" + protocol);
	}

	private void addTimestamp(long timestamp) {
		tags.add(ConsulRegistryConnector.TAG_TIMESTAMP + "=" + timestamp);
	}
}
