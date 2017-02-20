package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ClientSideCallContext;

/**
 * This interceptor makes the service unavailable depending on a configured probability (flipping) on the client side.
 *
 * @author another
 * @version $Id: $Id
 */
public class FlippingServiceUnavailableByConfigurationInterceptor extends ServiceUnavailableByConfigurationInterceptor {

	/** {@inheritDoc} */
	@Override
	protected boolean abortCall(ClientSideCallContext context) {
		return ConfigurationInterceptorUtil.flip() && super.abortCall(context);
	}

}
