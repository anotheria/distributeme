package org.distributeme.core.interceptor;

import org.distributeme.core.AbstractCallContext;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.ServerSideCallContext;

import java.util.HashSet;

/**
 * This interceptor can be configured to use one or multiple phases, but doesn't need to support all phases.
 * @author lrosenberg
 *
 */
public abstract class SinglePhaseInterceptor extends AbstractRequestInterceptor{
	
	/**
	 * Set of supported phases that are configured in the constructor of the overriding class. 
	 */
	private HashSet<InterceptionPhase> supportedPhases = new HashSet<InterceptionPhase>();
	
	protected SinglePhaseInterceptor(InterceptionPhase atLeastOne, InterceptionPhase ... phases){
		supportedPhases.add(atLeastOne);
		if (phases!=null){
			for (InterceptionPhase p : phases)
				supportedPhases.add(p);
		}
	}

	@Override
	public final InterceptorResponse beforeServantCall(ServerSideCallContext context,
			InterceptionContext iContext) {
		if (supportedPhases.contains(InterceptionPhase.BEFORE_SERVANT_CALL))
			return processPhase(context, iContext);
		return super.beforeServantCall(context, iContext);
	}

	@Override
	public final InterceptorResponse afterServantCall(ServerSideCallContext context,
			InterceptionContext iContext) {
		if (supportedPhases.contains(InterceptionPhase.AFTER_SERVANT_CALL))
			return processPhase(context, iContext);
		return super.afterServantCall(context, iContext);
	}

	@Override
	public final InterceptorResponse beforeServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {
		if (supportedPhases.contains(InterceptionPhase.BEFORE_SERVICE_CALL))
			return processPhase(context, iContext);
		return super.beforeServiceCall(context, iContext);
	}

	@Override
	public final InterceptorResponse afterServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {
		if (supportedPhases.contains(InterceptionPhase.AFTER_SERVICE_CALL))
			return processPhase(context, iContext);
		return super.afterServiceCall(context, iContext);
	}

	protected abstract InterceptorResponse processPhase(AbstractCallContext callContext, InterceptionContext iContext); 
	
}
