package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ServerSideCallContext;

/**
 * This slow down interceptor is acting on server side and configured by availabilitytesting.json configuration file.
 * @author lrosenberg
 *
 */
public class ServerSideSlowDownByConfigurationInterceptor extends ServerSideSlowDownInterceptor{
	
	@Override
	protected boolean slowDown(ServerSideCallContext context) {
		return ConfigurationInterceptorUtil.isServiceIdConfiguredByProperty(context.getServiceId());
	}
	
	@Override
	protected long getSlowDownTime() {
		return ConfigurationInterceptorUtil.getSlowDownTime();
	}
}
