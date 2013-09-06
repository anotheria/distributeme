package org.distributeme.core.routing;

import org.distributeme.core.ClientSideCallContext;

/**
 * This router implementation does mostly nothing. That means that it sends the request to the preconfigured service without ever modifying the serviceId.
 * @author lrosenberg.
 *
 */
public class NoOpRouter implements Router{

	@Override
	public String getServiceIdForCall(ClientSideCallContext context) {
		return context.getServiceId();
	}

	@Override
	public void customize(String parameter) {
		//do nothing
	}
}
