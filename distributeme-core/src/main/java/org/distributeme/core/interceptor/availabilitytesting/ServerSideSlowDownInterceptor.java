package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ServerSideCallContext;
import org.distributeme.core.interceptor.AbstractServerSideRequestInterceptor;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptorResponse;

/**
 * Base class for service side slow down interceptors.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public abstract class ServerSideSlowDownInterceptor extends AbstractServerSideRequestInterceptor{

	/** {@inheritDoc} */
	@Override
	public InterceptorResponse beforeServantCall(ServerSideCallContext context,
			InterceptionContext iContext) {
		if (slowDown(context)){
				try{
					Thread.sleep(getSlowDownTime());
				}catch(InterruptedException e){
					//ignore
				}
		}
		return super.beforeServantCall(context, iContext);
	}
	
	/**
	 * Returns true if current call should be slowed down. This decision is mostly based on service id.
	 *
	 * @param context a {@link org.distributeme.core.ServerSideCallContext} object.
	 * @return a boolean.
	 */
	protected abstract boolean slowDown(ServerSideCallContext context);
	
	/**
	 * Returns the amount of time in milliseconds the call should be slowed down.
	 *
	 * @return a long.
	 */
	protected abstract long getSlowDownTime();

}
