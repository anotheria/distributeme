package org.distributeme.core.interceptor;

import org.distributeme.core.ClientSideCallContext;

/**
 * Base, doing nothing, implementation of the ServerSideRequestInterceptor meant for extension.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class AbstractClientSideRequestInterceptor implements ClientSideRequestInterceptor{
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
