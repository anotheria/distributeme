package org.distributeme.test.interception.interceptor;

import java.util.List;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.interceptor.AbstractClientSideRequestInterceptor;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptorResponse;


public class InterceptSumInterceptor extends AbstractClientSideRequestInterceptor{

	@Override
	public InterceptorResponse beforeServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {

		if (!context.getServiceId().equals("org_distributeme_test_interception_TestService"))
			return InterceptorResponse.CONTINUE;

		if (!context.getMethodName().equals("modifiedSumParameters")){
			return InterceptorResponse.CONTINUE;
		}

		//lets double the outgoing parameters.
		List parameters = context.getParameters();
		Integer a = (Integer) parameters.get(0);
		Integer b = (Integer) parameters.get(1);
		parameters.set(0, new Integer(a*2));
		parameters.set(1, new Integer(b*2));
		//this call is not needed, but it makes the code readable.
		context.setParameters(parameters);
		
		return InterceptorResponse.CONTINUE;
	}

	@Override
	public InterceptorResponse afterServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {

		if (!context.getServiceId().equals("org_distributeme_test_interception_TestService"))
			return InterceptorResponse.CONTINUE;

		if (!context.getMethodName().equals("modifiedSum")){
			//lets double the outgoing parameters.
			return InterceptorResponse.CONTINUE;
		}
		
		Integer ret = (Integer)iContext.getReturnValue();
		return InterceptorResponse.returnNow(ret.intValue()*3);		
		
	}

}
