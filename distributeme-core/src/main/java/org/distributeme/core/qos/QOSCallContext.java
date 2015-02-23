package org.distributeme.core.qos;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 22.02.15 22:29
 */
public class QOSCallContext {

	private String callId;
	private String serviceId;
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
