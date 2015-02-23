package org.distributeme.test.interception.interceptor;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.interceptor.AbstractClientSideRequestInterceptor;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptorResponse;

public class InterceptInClientInterceptor extends AbstractClientSideRequestInterceptor {

	@Override
	public InterceptorResponse beforeServiceCall(
			ClientSideCallContext context, InterceptionContext iContext) {
		if (!context.getServiceId().equals("org_distributeme_test_interception_TestService"))
			return InterceptorResponse.CONTINUE;
		if (!context.getMethodName().equals("returnStringCaughtInClient"))
			return InterceptorResponse.CONTINUE;
		return InterceptorResponse.returnNow("Hello from client, no server call here ;-)");
	}

}
