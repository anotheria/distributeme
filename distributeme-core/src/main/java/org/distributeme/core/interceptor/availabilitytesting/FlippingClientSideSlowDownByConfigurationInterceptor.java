package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ClientSideCallContext;

/**
 * This interceptor slows down the request depending on a configured probability (flipping) on the client side.
 *
 * @author another
 * @version $Id: $Id
 */
public class FlippingClientSideSlowDownByConfigurationInterceptor extends
		ClientSideSlowDownByConfigurationInterceptor {

	/** {@inheritDoc} */
	@Override
	protected boolean slowDown(ClientSideCallContext context) {
		return ConfigurationInterceptorUtil.flip() && super.slowDown(context);
	}

}
