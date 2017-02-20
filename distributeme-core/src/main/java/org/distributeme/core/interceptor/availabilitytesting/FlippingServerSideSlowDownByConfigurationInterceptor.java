package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ServerSideCallContext;

/**
 * This interceptor slows down the request depending on a configured probability (flipping) on the server side.
 *
 * @author another
 * @version $Id: $Id
 */
public class FlippingServerSideSlowDownByConfigurationInterceptor extends
		ServerSideSlowDownByConfigurationInterceptor {

	/** {@inheritDoc} */
	@Override
	protected boolean slowDown(ServerSideCallContext context) {
		return ConfigurationInterceptorUtil.flip() && super.slowDown(context);
	}

}
