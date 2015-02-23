package org.distributeme.test.interception.interceptor;

import java.util.Map;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.ServerSideCallContext;
import org.distributeme.core.interceptor.AbstractRequestInterceptor;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptorResponse;

public class PiggybackingInterceptor extends AbstractRequestInterceptor{

	@Override
	public InterceptorResponse afterServantCall(ServerSideCallContext context,
			InterceptionContext iContext) {

		if (!context.getServiceId().equals("org_distributeme_test_interception_TestService"))
			return InterceptorResponse.CONTINUE;
		if (!context.getMethodName().equals("callByReference"))
			return InterceptorResponse.CONTINUE;

		Map originalParameter = (Map)context.getParameters().get(0);
		context.getTransportableCallContext().put("piggyback", originalParameter);
		
		return InterceptorResponse.CONTINUE;
	}

	@Override
	public InterceptorResponse afterServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {
		
		if (!context.getServiceId().equals("org_distributeme_test_interception_TestService"))
			return InterceptorResponse.CONTINUE;
		if (!context.getMethodName().equals("callByReference"))
			return InterceptorResponse.CONTINUE;


		Map originalMap = (Map)context.getTransportableCallContext().get("piggyback");
		if (originalMap!=null){
			for (Object key : originalMap.keySet()){
				((Map)context.getParameters().get(0)).put(key, originalMap.get(key));
			}
		}
		
		return InterceptorResponse.CONTINUE;
	}
}
