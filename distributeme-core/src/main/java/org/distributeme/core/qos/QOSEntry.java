package org.distributeme.core.qos;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 22.02.15 23:04
 */
public class QOSEntry {
	private String serviceId;
	private String callId;
	private long timestamp;
	private long blacklistedUntil;

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

	long getAge(){
		return System.currentTimeMillis() - timestamp;
	}

	public void setBlacklistedUntil(long l) {
		blacklistedUntil = l;
	}

	public long getBlacklistedUntil(){
		return blacklistedUntil;
	}

	public boolean isBlacklistExpired(){
		return System.currentTimeMillis() > getBlacklistedUntil();
	}
}
