package org.distributeme.core.interceptor;

import org.distributeme.core.ClientSideCallContext;

/**
 * Base, doing nothing, implementation of the ServerSideRequestInterceptor meant for extension.
 * @author lrosenberg
 *
 */
public class AbstractClientSideRequestInterceptor implements ClientSideRequestInterceptor{
	@Override
	public InterceptorResponse beforeServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {
		return InterceptorResponse.CONTINUE;
	}

	@Override
	public InterceptorResponse afterServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {
		return InterceptorResponse.CONTINUE;
	}
}
