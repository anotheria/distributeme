package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ClientSideCallContext;
/**
 * This slow down interceptor is acting on client side and configured by availabilitytesting.json configuration file.
 * @author lrosenberg
 *
 */
public class ClientSideSlowDownByConfigurationInterceptor extends ClientSideSlowDownInterceptor{

	@Override
	protected boolean slowDown(ClientSideCallContext context) {
		return ConfigurationInterceptorUtil.isServiceIdConfiguredByProperty(context.getServiceId());
	}

	@Override
	protected long getSlowDownTime() {
		return ConfigurationInterceptorUtil.getSlowDownTime();
	}
}
