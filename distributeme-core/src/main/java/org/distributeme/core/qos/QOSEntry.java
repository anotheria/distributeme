package org.distributeme.core.qos;

/**
 * A call entry for QOS.
 *
 * @author lrosenberg
 * @since 22.02.15 23:04
 * @version $Id: $Id
 */
public class QOSEntry {
	/**
	 * Id of the service.
	 */
	private String serviceId;
	/**
	 * Unique id of the call.
	 */
	private String callId;
	/**
	 * Timestamp of the call.
	 */
	private long timestamp;
	/**
	 * If service is blacklisted due to a long call, the time until that the blacklist is active.
	 */
	private long blacklistedUntil;

	/**
	 * Creates a new QOSEntry.
	 *
	 * @param aServiceId the id of the service.
	 * @param aCallId the unique id of the call.
	 */
	public QOSEntry(String aServiceId, String aCallId){
		serviceId = aServiceId;
		callId = aCallId;
		timestamp = System.currentTimeMillis();
	}

	/**
	 * <p>Getter for the field <code>callId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCallId() {
		return callId;
	}

	/**
	 * <p>Setter for the field <code>callId</code>.</p>
	 *
	 * @param callId a {@link java.lang.String} object.
	 */
	public void setCallId(String callId) {
		this.callId = callId;
	}

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
	 * <p>Getter for the field <code>timestamp</code>.</p>
	 *
	 * @return a long.
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * <p>Setter for the field <code>timestamp</code>.</p>
	 *
	 * @param timestamp a long.
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * <p>getKey.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getKey(){
		return getKey(getServiceId(), getCallId());
	}

	/**
	 * <p>getKey.</p>
	 *
	 * @param serviceId a {@link java.lang.String} object.
	 * @param callId a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getKey(String serviceId, String callId){
		return serviceId+"-"+callId;
	}

	/** {@inheritDoc} */
	@Override
	public String toString(){
		return getKey()+" age: "+getAge()+", blacklisted? : "+blacklistedUntil;
	}

	/**
	 * How long this call has been active.
	 * @return
	 */
	long getAge(){
		return System.currentTimeMillis() - timestamp;
	}

	/**
	 * <p>Setter for the field <code>blacklistedUntil</code>.</p>
	 *
	 * @param l a long.
	 */
	public void setBlacklistedUntil(long l) {
		blacklistedUntil = l;
	}

	/**
	 * <p>Getter for the field <code>blacklistedUntil</code>.</p>
	 *
	 * @return a long.
	 */
	public long getBlacklistedUntil(){
		return blacklistedUntil;
	}

	/**
	 * If blacklisted, true if the blacklist expiration timestamp has been reached?
	 *
	 * @return a boolean.
	 */
	public boolean isBlacklistExpired(){
		return System.currentTimeMillis() > getBlacklistedUntil();
	}
}
