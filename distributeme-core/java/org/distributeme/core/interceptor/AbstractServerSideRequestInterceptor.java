package org.distributeme.core.interceptor;

import org.distributeme.core.ServerSideCallContext;

/**
 * Base, doing nothing, implementation of the ClientSideRequestInterceptor meant for extension.
 * @author lrosenberg
 *
 */
public class AbstractServerSideRequestInterceptor implements ServerSideRequestInterceptor{

	@Override
	public InterceptorResponse beforeServantCall(ServerSideCallContext context,
			InterceptionContext iContext) {
		return InterceptorResponse.CONTINUE;
	}

	@Override
	public InterceptorResponse afterServantCall(ServerSideCallContext context,
			InterceptionContext iContext) {
		return InterceptorResponse.CONTINUE;
	}

}
