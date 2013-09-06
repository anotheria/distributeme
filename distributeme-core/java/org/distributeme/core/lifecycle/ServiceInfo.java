package org.distributeme.core.lifecycle;

import java.io.Serializable;

import net.anotheria.util.NumberUtils;
/**
 * This class contains information about a service.
 * @author lrosenberg
 *
 */
public class ServiceInfo implements Serializable{
	
	/**
	 * SerialVersionUID. 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Id of the service.
	 */
	private String serviceId;
	/**
	 * The last time the service was accessed via the public interface.
	 */
	private long lastAccessTimestamp;
	/**
	 * Timestamp of the service start and service implementation created.
	 */
	private long creationTimestamp;

	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public long getLastAccessTimestamp() {
		return lastAccessTimestamp;
	}
	public void setLastAccessTimestamp(long lastAccessTimestamp) {
		this.lastAccessTimestamp = lastAccessTimestamp;
	}
	public long getCreationTimestamp() {
		return creationTimestamp;
	}
	public void setCreationTimestamp(long creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}
	
	@Override public String toString(){
		return getServiceId()+" LastAccess: "+getLastAccessTimestamp()+", "+NumberUtils.makeISO8601TimestampString(getLastAccessTimestamp())+
			" Creation: "+getCreationTimestamp()+", "+NumberUtils.makeISO8601TimestampString(getCreationTimestamp());
	}
}
