package org.distributeme.core.qos;

import net.anotheria.util.IdCodeGenerator;
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
		QOSCallContext callContext = QOSCallContext.currentQOSCallContext();
		String serviceId = callContext.getServiceId();
		String callId = callContext.getCallId();

		QOSRegistry.getInstance().callFinished(serviceId, callId);
		return InterceptorResponse.CONTINUE;
	}

	@Override
	public InterceptorResponse beforeServiceCall(ClientSideCallContext context, InterceptionContext iContext) {
		String serviceId = context.getServiceId();
		long startTime = System.currentTimeMillis();
		String callId = IdCodeGenerator.generateCode(20);
		QOSCallContext.currentQOSCallContext().setServiceCallIdAndTimestamp(serviceId, callId, startTime);
		boolean mayStart = QOSRegistry.getInstance().callStarted(serviceId, callId);
		if (!mayStart){
			return InterceptorResponse.ABORT_AND_FAIL;
		}
		return InterceptorResponse.CONTINUE;
	}
}
