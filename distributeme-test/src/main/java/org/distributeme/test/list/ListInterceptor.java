package org.distributeme.test.list;

import org.distributeme.core.ServerSideCallContext;
import org.distributeme.core.interceptor.AbstractServerSideRequestInterceptor;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptorResponse;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 23.09.15 12:26
 */
public class ListInterceptor extends AbstractServerSideRequestInterceptor {
	@Override
	public InterceptorResponse beforeServantCall(ServerSideCallContext context, InterceptionContext iContext) {

		System.out.println("... Incoming call, context: "+context);
		System.out.println("... tcc: "+context.getTransportableCallContext());

		return super.beforeServantCall(context, iContext);
	}
}
