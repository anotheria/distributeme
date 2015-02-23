package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ClientSideCallContext;

/**
 * This interceptor slows down the request depending on a configured probability (flipping) on the client side.
 */
public class FlippingClientSideSlowDownByConfigurationInterceptor extends
		ClientSideSlowDownByConfigurationInterceptor {

	@Override
	protected boolean slowDown(ClientSideCallContext context) {
		return ConfigurationInterceptorUtil.flip() && super.slowDown(context);
	}

}
