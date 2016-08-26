package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.exception.ServiceUnavailableException;
import org.distributeme.core.interceptor.AbstractClientSideRequestInterceptor;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptorResponse;

/**
 * Base interceptor class for service unavailable interceptors. This kind of interceptors throw
 * a ServiceUnavailableException as if the service would be down.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public abstract class ServiceUnavailableInterceptor extends AbstractClientSideRequestInterceptor{

	/** {@inheritDoc} */
	@Override
	public InterceptorResponse beforeServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {
		
		if (abortCall(context))
			throw new ServiceUnavailableException("Interceptor aborted the call");
		return super.beforeServiceCall(context, iContext);
	}

	/**
	 * Returns true if current call should be aborted.
	 *
	 * @param context a {@link org.distributeme.core.ClientSideCallContext} object.
	 * @return a boolean.
	 */
	protected abstract boolean abortCall(ClientSideCallContext context);
}
