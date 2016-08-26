package org.distributeme.core.interceptor.availabilitytesting;

import org.distributeme.core.ClientSideCallContext;

/**
 * This interceptor marks service unavailable and is configured by system properties.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class ServiceUnavailableByPropertyInterceptor extends ServiceUnavailableInterceptor{
	
	/** {@inheritDoc} */
	@Override
	protected boolean abortCall(ClientSideCallContext context) {
		return PropertyInterceptorUtil.isServiceIdConfiguredByProperty(context.getServiceId());
	}
}
