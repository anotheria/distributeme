package org.distributeme.core.qos;

/**
 * A thread local that stores information for current request.
 *
 * @author lrosenberg
 * @since 22.02.15 22:29
 */
public class QOSCallContext {

	/**
	 * Unique id of the call.
	 */
	private String callId;
	/**
	 * Service id for the current call.
	 */
	private String serviceId;
	/**
	 * Start time of the call.
	 */
	private long startTime;

	private static ThreadLocal<QOSCallContext> context = new ThreadLocal<QOSCallContext>(){
		@Override
		protected QOSCallContext initialValue() {
			return new QOSCallContext();
		}
	};

	public static QOSCallContext currentQOSCallContext(){
		return context.get();
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

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setServiceCallIdAndTimestamp(String aServiceId, String aCallId, long aStartTime){
		serviceId = aServiceId;
		callId = aCallId;
		startTime = aStartTime;
	}
}
