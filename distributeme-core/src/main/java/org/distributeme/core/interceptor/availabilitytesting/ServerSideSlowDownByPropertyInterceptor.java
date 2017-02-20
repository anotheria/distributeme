package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ServerSideCallContext;

/**
 * This interceptor slows down a server if the service id is submitted to the process as -DavailabilityTestingServiceId.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class ServerSideSlowDownByPropertyInterceptor extends ServerSideSlowDownInterceptor{

	/** {@inheritDoc} */
	@Override
	protected boolean slowDown(ServerSideCallContext context) {
		return PropertyInterceptorUtil.isServiceIdConfiguredByProperty(context.getServiceId());
	}

	/** {@inheritDoc} */
	@Override
	protected long getSlowDownTime() {
		return PropertyInterceptorUtil.getSlowDownTime();
	}
	
}
