package org.distributeme.core.failing;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.AbstractRouter;
import org.distributeme.core.routing.RegistrationNameProvider;
import org.distributeme.core.routing.Router;

/**
 * This is base class for failing strategy / router combination which is pretty much the same as Failover, but instead of staying on 
 * the failover instance forever, it tries to switch back after some timeout.
 * @author another
 */
public abstract class FailoverAndReturn extends AbstractRouter implements FailingStrategy, RegistrationNameProvider, Router{

	/**
	 * Target service id after first failover.
	 */
	private String failedOverServiceId = null;
	/**
	 * Timestamp in ms of last failover event.
	 */
	private long failoverTimestamp = 0L;
	
	/**
	 * Seconds in ms.
	 */
	protected static final long SECOND = 1000L;
	/**
	 * Minutes in ms.
	 */
	protected static final long MINUTE = SECOND*60;
	
	/**
	 * Returns the time unit in milliseconds between the router retries to get back to original server.
	 * @return
	 */
	protected abstract long getFailbackTimeout();
	
	@Override
	public String getServiceIdForCall(ClientSideCallContext callContext) {
		
		if (failoverTimestamp!=0 && callContext.isFirstCall() && (System.currentTimeMillis()-failoverTimestamp>getFailbackTimeout())){
			failoverTimestamp = 0;
			failedOverServiceId = null;
		}

		if (!callContext.isFirstCall() && failedOverServiceId==null){
			failedOverServiceId = callContext.getServiceId();
			failoverTimestamp = System.currentTimeMillis();
		}

		String ret =  failedOverServiceId == null ? callContext.getServiceId() : failedOverServiceId;
		return ret;
	}

	/**
	 * Suffix of the failover instance.
	 */
	public static final String SUFFIX = "-failover";
	
	@Override
	public String getRegistrationName(String serviceId) {
		return serviceId+SUFFIX;
	}

	@Override
	public void customize(String parameter) {
		// not used.
	}

	@Override
	public FailDecision callFailed(ClientSideCallContext context) {
		FailDecision ret = FailDecision.retryOnce();
        ret.setTargetService(context.getServiceId()+ SUFFIX);
		return ret;
	}

	protected String getSuffix(){
		return SUFFIX;
	}
}
