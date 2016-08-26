package org.distributeme.core.interceptor;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.ServerSideCallContext;

/**
 * Base class offered for extension which provides default implementations for all interception methods. You simply have to override those interception methods (phases) you are interested in.
 * The default behaviour is to do nothing (return InterceptorResponse.CONTINUE).
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public abstract class AbstractRequestInterceptor implements ClientSideRequestInterceptor, ServerSideRequestInterceptor{

	/** {@inheritDoc} */
	@Override
	public InterceptorResponse beforeServantCall(ServerSideCallContext context,
			InterceptionContext iContext) {
		return InterceptorResponse.CONTINUE;
	}

	/** {@inheritDoc} */
	@Override
	public InterceptorResponse afterServantCall(ServerSideCallContext context,
			InterceptionContext iContext) {
		return InterceptorResponse.CONTINUE;
	}

	/** {@inheritDoc} */
	@Override
	public InterceptorResponse beforeServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {
		return InterceptorResponse.CONTINUE;
	}

	/** {@inheritDoc} */
	@Override
	public InterceptorResponse afterServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {
		return InterceptorResponse.CONTINUE;
	}

}
