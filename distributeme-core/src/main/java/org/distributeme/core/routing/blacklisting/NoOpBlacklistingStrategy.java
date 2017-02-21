package org.distributeme.core.routing.blacklisting;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.GenericRouterConfiguration;


/**
 * Created by rboehling on 2/21/17.
 */
public class NoOpBlacklistingStrategy implements BlacklistingStrategy {

	@Override
	public boolean isBlacklisted(String selectedServiceId) {
		return false;
	}

	@Override
	public void notifyCallFailed(ClientSideCallContext clientSideCallContext) {

	}

	@Override
	public void setConfiguration(GenericRouterConfiguration configuration) {

	}
}
