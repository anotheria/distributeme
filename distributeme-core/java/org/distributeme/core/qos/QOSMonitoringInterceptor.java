package org.distributeme.core.qos;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.interceptor.ClientSideRequestInterceptor;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptorResponse;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 20.02.15 17:29
 */
public class QOSMonitoringInterceptor implements ClientSideRequestInterceptor{
	@Override
	public InterceptorResponse afterServiceCall(ClientSideCallContext context, InterceptionContext iContext) {
		return null;
	}

	@Override
	public InterceptorResponse beforeServiceCall(ClientSideCallContext context, InterceptionContext iContext) {
		return null;
	}
}
