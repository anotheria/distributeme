package org.distributeme.core.qos;

/**
 * A thread local that stores information for current request.
 *
 * @author lrosenberg
 * @since 22.02.15 22:29
 * @version $Id: $Id
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

	/**
	 * Thread local context instance.
	 */
	private static ThreadLocal<QOSCallContext> context = new ThreadLocal<QOSCallContext>(){
		@Override
		protected QOSCallContext initialValue() {
			return new QOSCallContext();
		}
	};

	/**
	 * <p>currentQOSCallContext.</p>
	 *
	 * @return a {@link org.distributeme.core.qos.QOSCallContext} object.
	 */
	public static QOSCallContext currentQOSCallContext(){
		return context.get();
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
	 * <p>Getter for the field <code>startTime</code>.</p>
	 *
	 * @return a long.
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * <p>Setter for the field <code>startTime</code>.</p>
	 *
	 * @param startTime a long.
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * <p>setServiceCallIdAndTimestamp.</p>
	 *
	 * @param aServiceId a {@link java.lang.String} object.
	 * @param aCallId a {@link java.lang.String} object.
	 * @param aStartTime a long.
	 */
	public void setServiceCallIdAndTimestamp(String aServiceId, String aCallId, long aStartTime){
		serviceId = aServiceId;
		callId = aCallId;
		startTime = aStartTime;
	}
}
