package org.distributeme.core.qos;

/**
 * A call entry for QOS.
 *
 * @author lrosenberg
 * @since 22.02.15 23:04
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
	 * @param aServiceId the id of the service.
	 * @param aCallId the unique id of the call.
	 */
	public QOSEntry(String aServiceId, String aCallId){
		serviceId = aServiceId;
		callId = aCallId;
		timestamp = System.currentTimeMillis();
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getKey(){
		return getKey(getServiceId(), getCallId());
	}

	public static String getKey(String serviceId, String callId){
		return serviceId+"-"+callId;
	}

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

	public void setBlacklistedUntil(long l) {
		blacklistedUntil = l;
	}

	public long getBlacklistedUntil(){
		return blacklistedUntil;
	}

	/**
	 * If blacklisted, true if the blacklist expiration timestamp has been reached?
	 * @return
	 */
	public boolean isBlacklistExpired(){
		return System.currentTimeMillis() > getBlacklistedUntil();
	}
}
