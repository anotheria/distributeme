package org.distributeme.consulintegration;

/**
 * Created by rboehling on 3/1/17.
 */
public enum ConsulTag {

	INSTANCE_ID("instanceId"),
	PROTOCOL("protocol"),
	TIMESTAMP("timestamp")
	;
	private final String tagName;

	ConsulTag(String tagName) {
		this.tagName = tagName;
	}

	public String getTagName() {
		return tagName;
	}
}
