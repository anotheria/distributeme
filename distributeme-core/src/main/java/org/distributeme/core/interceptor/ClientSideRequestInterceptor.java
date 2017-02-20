package org.distributeme.core.interceptor;

import org.distributeme.core.ClientSideCallContext;

/**
 * Describes the client side request interceptor. This interceptor runs in the stub on the client side.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public interface ClientSideRequestInterceptor {
	/**
	 * Called after client side processing but before the actual call is performed.
	 *
	 * @param context a {@link org.distributeme.core.ClientSideCallContext} object.
	 * @param iContext a {@link org.distributeme.core.interceptor.InterceptionContext} object.
	 * @return a {@link org.distributeme.core.interceptor.InterceptorResponse} object.
	 */
	InterceptorResponse beforeServiceCall(ClientSideCallContext context, InterceptionContext iContext);
	/**
	 * Called immediately after service call, but before anything is returned to the caller. This interceptor can be used to inspect or modify the answer from server.
	 *
	 * @param context a {@link org.distributeme.core.ClientSideCallContext} object.
	 * @param iContext a {@link org.distributeme.core.interceptor.InterceptionContext} object.
	 * @return a {@link org.distributeme.core.interceptor.InterceptorResponse} object.
	 */
	InterceptorResponse afterServiceCall(ClientSideCallContext context, InterceptionContext iContext);
	
}
