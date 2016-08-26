package org.distributeme.core.interceptor;

import org.distributeme.core.ServerSideCallContext;
/**
 * This interceptor describes an interface that is acting on the server side.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public interface ServerSideRequestInterceptor {
	/**
	 * Called before any real processing happens on the server side.
	 *
	 * @param context a {@link org.distributeme.core.ServerSideCallContext} object.
	 * @param iContext a {@link org.distributeme.core.interceptor.InterceptionContext} object.
	 * @return a {@link org.distributeme.core.interceptor.InterceptorResponse} object.
	 */
	InterceptorResponse beforeServantCall(ServerSideCallContext context, InterceptionContext iContext);
	/**
	 * Called after the service actually processed the request. Allows to inspect or modify the answer.
	 *
	 * @param context a {@link org.distributeme.core.ServerSideCallContext} object.
	 * @param iContext a {@link org.distributeme.core.interceptor.InterceptionContext} object.
	 * @return a {@link org.distributeme.core.interceptor.InterceptorResponse} object.
	 */
	InterceptorResponse afterServantCall(ServerSideCallContext context, InterceptionContext iContext);
}
