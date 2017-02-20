package org.distributeme.core.lifecycle;

import net.anotheria.util.NumberUtils;

import java.io.Serializable;
/**
 * This class contains information about a service.
 *
 * @author lrosenberg
 * @version $Id: $Id
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

	/**
	 * <p>Getter for the field <code>serviceId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getServiceId() {
		return serviceId;
	}
	/**
	 * <p>Setter for the field <code>serviceId</code>.</p>
	 *
	 * @param serviceId a {@link java.lang.String} object.
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * <p>Getter for the field <code>lastAccessTimestamp</code>.</p>
	 *
	 * @return a long.
	 */
	public long getLastAccessTimestamp() {
		return lastAccessTimestamp;
	}
	/**
	 * <p>Setter for the field <code>lastAccessTimestamp</code>.</p>
	 *
	 * @param lastAccessTimestamp a long.
	 */
	public void setLastAccessTimestamp(long lastAccessTimestamp) {
		this.lastAccessTimestamp = lastAccessTimestamp;
	}
	/**
	 * <p>Getter for the field <code>creationTimestamp</code>.</p>
	 *
	 * @return a long.
	 */
	public long getCreationTimestamp() {
		return creationTimestamp;
	}
	/**
	 * <p>Setter for the field <code>creationTimestamp</code>.</p>
	 *
	 * @param creationTimestamp a long.
	 */
	public void setCreationTimestamp(long creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return getServiceId()+" LastAccess: "+getLastAccessTimestamp()+", "+NumberUtils.makeISO8601TimestampString(getLastAccessTimestamp())+
			" Creation: "+getCreationTimestamp()+", "+NumberUtils.makeISO8601TimestampString(getCreationTimestamp());
	}
}
