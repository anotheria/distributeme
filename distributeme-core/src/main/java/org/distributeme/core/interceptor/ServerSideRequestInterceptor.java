package org.distributeme.core.interceptor;

import org.distributeme.core.ServerSideCallContext;
/**
 * This interceptor describes an interface that is acting on the server side.
 * @author lrosenberg
 *
 */
public interface ServerSideRequestInterceptor {
	/**
	 * Called before any real processing happens on the server side. 
	 * @param context
	 * @return
	 */
	InterceptorResponse beforeServantCall(ServerSideCallContext context, InterceptionContext iContext);
	/**
	 * Called after the service actually processed the request. Allows to inspect or modify the answer.
	 * @param context
	 * @return
	 */
	InterceptorResponse afterServantCall(ServerSideCallContext context, InterceptionContext iContext);
}
