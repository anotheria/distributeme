package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.interceptor.AbstractClientSideRequestInterceptor;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptorResponse;

/**
 * Base class for client side slow down interceptors.
 * @author lrosenberg
 *
 */
public abstract class ClientSideSlowDownInterceptor extends AbstractClientSideRequestInterceptor{

	@Override
	public InterceptorResponse beforeServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {
		if (slowDown(context)){
				try{
					Thread.sleep(getSlowDownTime());
				}catch(InterruptedException e){
					//ignore
				}
		}
		return super.beforeServiceCall(context, iContext);
	}
	/**
	 * Returns true if current call should be slowed down. This decision is mostly based on service id.
	 * @param context
	 * @return
	 */
	protected abstract boolean slowDown(ClientSideCallContext context);

	/**
	 * Returns the amount of time in milliseconds the call should be slowed down.
	 * @return
	 */
	protected abstract long getSlowDownTime();
}
