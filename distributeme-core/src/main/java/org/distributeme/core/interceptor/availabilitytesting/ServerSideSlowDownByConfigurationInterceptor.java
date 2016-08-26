package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ServerSideCallContext;

/**
 * This slow down interceptor is acting on server side and configured by availabilitytesting.json configuration file.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class ServerSideSlowDownByConfigurationInterceptor extends ServerSideSlowDownInterceptor{
	
	/** {@inheritDoc} */
	@Override
	protected boolean slowDown(ServerSideCallContext context) {
		return ConfigurationInterceptorUtil.isServiceIdConfiguredByProperty(context.getServiceId());
	}
	
	/** {@inheritDoc} */
	@Override
	protected long getSlowDownTime() {
		return ConfigurationInterceptorUtil.getSlowDownTime();
	}
}
