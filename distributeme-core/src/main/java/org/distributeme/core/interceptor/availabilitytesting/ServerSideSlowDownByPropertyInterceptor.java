package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ServerSideCallContext;

/**
 * This interceptor slows down a server if the service id is submitted to the process as -DavailabilityTestingServiceId.
 * @author lrosenberg
 *
 */
public class ServerSideSlowDownByPropertyInterceptor extends ServerSideSlowDownInterceptor{

	@Override
	protected boolean slowDown(ServerSideCallContext context) {
		return PropertyInterceptorUtil.isServiceIdConfiguredByProperty(context.getServiceId());
	}

	@Override
	protected long getSlowDownTime() {
		return PropertyInterceptorUtil.getSlowDownTime();
	}
	
}
