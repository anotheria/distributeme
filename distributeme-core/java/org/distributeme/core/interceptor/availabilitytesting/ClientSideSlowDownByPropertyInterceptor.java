package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ClientSideCallContext;

/**
 * This interceptor slows down a server if the service id is submitted to the process as -DavailabilityTestingServiceId.
 * This interceptor works on the client side, it slows down the call BEFORE it leaves the vm. 
 * @author lrosenberg
 *
 */
public class ClientSideSlowDownByPropertyInterceptor extends ClientSideSlowDownInterceptor{

	@Override
	protected boolean slowDown(ClientSideCallContext context) {
		return PropertyInterceptorUtil.isServiceIdConfiguredByProperty(context.getServiceId());
	}
	
	protected long getSlowDownTime(){
		return PropertyInterceptorUtil.getSlowDownTime();
	}
}
