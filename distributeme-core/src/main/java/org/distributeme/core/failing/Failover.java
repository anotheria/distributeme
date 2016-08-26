package org.distributeme.core.failing;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.AbstractRouter;
import org.distributeme.core.routing.RegistrationNameProvider;
import org.distributeme.core.routing.Router;

/**
 * This failing strategy / router combination fails over to a special failover service instance and stays there.
 *
 * @author another
 * @version $Id: $Id
 */
public class Failover extends AbstractRouter implements FailingStrategy, RegistrationNameProvider, Router{

	/**
	 * Target service if to failover to.
	 */
	private String failedOverServiceId = null;
	
	/** {@inheritDoc} */
	@Override
	public String getServiceIdForCall(ClientSideCallContext callContext) {
		if (!callContext.isFirstCall() && failedOverServiceId==null){
			failedOverServiceId = callContext.getServiceId();
		}
		String ret =  failedOverServiceId == null ? callContext.getServiceId() : failedOverServiceId;
		return ret;
	}

	/**
	 * Suffix of the failover instance.
	 */
	public static final String SUFFIX = "-failover";
	
	/** {@inheritDoc} */
	@Override
	public String getRegistrationName(String serviceId) {
		return serviceId+SUFFIX;
	}

	/** {@inheritDoc} */
	@Override
	public void customize(String parameter) {
		// not used.
	}

	/** {@inheritDoc} */
	@Override
	public FailDecision callFailed(ClientSideCallContext context) {
		FailDecision ret = FailDecision.retryOnce();
		ret.setTargetService(context.getServiceId()+SUFFIX);
		return ret;
	}

}
