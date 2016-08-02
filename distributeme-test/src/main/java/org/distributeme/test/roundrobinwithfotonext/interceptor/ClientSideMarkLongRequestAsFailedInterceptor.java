package org.distributeme.test.roundrobinwithfotonext.interceptor;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.interceptor.ClientSideRequestInterceptor;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptorResponse;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.02.15 01:09
 */
public class ClientSideMarkLongRequestAsFailedInterceptor implements ClientSideRequestInterceptor{

	public static final String START_ATTRIBUTE = ClientSideMarkLongRequestAsFailedInterceptor.class.getName()+ '-' +"start";
	public static final String TIMEOUT_PROPERTY = "availabilityTestingFailureTimeout";

	//default 1 minute
	private long TIMEOUT = 60000L;

	public ClientSideMarkLongRequestAsFailedInterceptor(){
		String timeoutProperty = System.getProperty(TIMEOUT_PROPERTY);
		if (timeoutProperty!= null && !timeoutProperty.isEmpty())
			TIMEOUT = Long.parseLong(timeoutProperty);
	}

	@Override
	public InterceptorResponse afterServiceCall(ClientSideCallContext context, InterceptionContext iContext) {
		long start = (Long)iContext.getLocalStore().get(START_ATTRIBUTE);
		long end = System.currentTimeMillis();
		long duration = end - start;
		if (duration > TIMEOUT){
			System.out.println("%%% SLOW CALL "+duration);
			return InterceptorResponse.RETURN_AND_FAIL;
		}
		return InterceptorResponse.CONTINUE;
	}

	@Override
	public InterceptorResponse beforeServiceCall(ClientSideCallContext context, InterceptionContext iContext) {
		iContext.getLocalStore().put(START_ATTRIBUTE, System.currentTimeMillis());
		return InterceptorResponse.CONTINUE;
	}
}
