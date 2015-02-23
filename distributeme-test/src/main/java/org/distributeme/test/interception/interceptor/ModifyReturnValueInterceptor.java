package org.distributeme.test.interception.interceptor;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.ServerSideCallContext;
import org.distributeme.core.interceptor.AbstractRequestInterceptor;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptorResponse;

public class ModifyReturnValueInterceptor extends AbstractRequestInterceptor{

	@Override
	public InterceptorResponse afterServantCall(ServerSideCallContext context,
			InterceptionContext iContext) {
		if (!context.getServiceId().equals("org_distributeme_test_interception_TestService"))
			return InterceptorResponse.CONTINUE;
		if (!context.getMethodName().equals("returnString"))
			return InterceptorResponse.CONTINUE;
		
		return InterceptorResponse.returnLater("Servant said: \""+iContext.getReturnValue()+"\"");
	}

	@Override
	public InterceptorResponse afterServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {
		if (!context.getServiceId().equals("org_distributeme_test_interception_TestService"))
			return InterceptorResponse.CONTINUE;
		if (!context.getMethodName().equals("returnString"))
			return InterceptorResponse.CONTINUE;
		return InterceptorResponse.returnLater("Service said: \""+iContext.getReturnValue()+"\"");
	}
	
}
