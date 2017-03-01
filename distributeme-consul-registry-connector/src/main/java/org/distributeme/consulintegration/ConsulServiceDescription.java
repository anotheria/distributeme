package org.distributeme.consulintegration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.google.gson.annotations.SerializedName;
import net.anotheria.util.StringUtils;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.conventions.SystemProperties;


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

	public ConsulServiceDescription(ServiceDescriptor serviceDescriptor, Map<String, String> tagableSystemProperties) {
		String consulFriendlyServiceName = ServiceNameTranslator.toConsul(serviceDescriptor.getServiceId());
		id = consulFriendlyServiceName;
		name = consulFriendlyServiceName;
		port = serviceDescriptor.getPort();
		address = serviceDescriptor.getHost();
		addInstanceId(serviceDescriptor.getInstanceId());
		addSystemProperties(tagableSystemProperties);
		addProtocol(serviceDescriptor.getProtocol());
		addTimestamp(serviceDescriptor.getTimestamp());
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
}
