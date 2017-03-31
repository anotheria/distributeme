package org.distributeme.consulintegration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;
import org.distributeme.core.ServiceDescriptor;


/**
 * Request object for register a service at the consul registry.
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

	ConsulServiceDescription(ServiceDescriptor serviceDescriptor, Map<String, String> tagableSystemProperties, List<String> customTagList) {
		String consulFriendlyServiceName = ServiceNameTranslator.toConsul(serviceDescriptor.getServiceId());
		id = consulFriendlyServiceName;
		name = consulFriendlyServiceName;
		port = serviceDescriptor.getPort();
		address = serviceDescriptor.getHost();
		addInstanceId(serviceDescriptor.getInstanceId());
		addCustomTags(customTagList);
		addSystemProperties(tagableSystemProperties);
		addProtocol(serviceDescriptor.getProtocol());
		addTimestamp(serviceDescriptor.getTimestamp());
	}

	ConsulServiceDescription(ServiceDescriptor servicesDescriptor, Map<String, String> tagableSystemProperties) {
		this(servicesDescriptor, tagableSystemProperties, new ArrayList<String>());
	}

	private void addCustomTags(List<String> customTagList) {
		for (String customTag : customTagList) {
			tags.add(customTag);
		}
	}

	private void addSystemProperties(Map<String, String> tagableSystemProperties) {
		for(Map.Entry<String, String> entry: tagableSystemProperties.entrySet()) {
			String tagName = entry.getKey();
			String tagValue = entry.getValue();
			tags.add(tagName + "=" + tagValue);
		}
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

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public List<String> getTags() {
		return tags;
	}
}
