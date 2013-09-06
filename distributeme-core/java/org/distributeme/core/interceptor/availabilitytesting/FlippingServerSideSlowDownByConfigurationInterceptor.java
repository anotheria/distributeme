package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ServerSideCallContext;

/**
 * This interceptor slows down the request depending on a configured probability (flipping) on the server side.
 */
public class FlippingServerSideSlowDownByConfigurationInterceptor extends
		ServerSideSlowDownByConfigurationInterceptor {

	@Override
	protected boolean slowDown(ServerSideCallContext context) {
		return ConfigurationInterceptorUtil.flip() && super.slowDown(context);
	}

}
