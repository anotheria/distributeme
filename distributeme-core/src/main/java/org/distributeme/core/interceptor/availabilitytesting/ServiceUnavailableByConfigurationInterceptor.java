package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ClientSideCallContext;

/**
 * This interceptor marks service unavailable and is configured by availabilitytesting.json configuration file.
 * @author lrosenberg
 *
 */
public class ServiceUnavailableByConfigurationInterceptor extends ServiceUnavailableInterceptor{
	
	@Override
	protected boolean abortCall(ClientSideCallContext context) {
		return ConfigurationInterceptorUtil.isServiceIdConfiguredByProperty(context.getServiceId());
	}
}
