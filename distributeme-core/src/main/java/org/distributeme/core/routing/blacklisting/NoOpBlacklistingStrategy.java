package org.distributeme.core.routing.blacklisting;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.GenericRouterConfiguration;


/**
 * NoOpBlacklisting strategy simply does nothing. Returns always false for isBlacklisted()
 * Might be useful for dummy usage.
 *
 * Created by rboehling on 2/21/17.
 */
public class NoOpBlacklistingStrategy implements BlacklistingStrategy {

	@Override
	public boolean isBlacklisted(String instanceId) {
		return false;
	}

	@Override
	public void notifyCallFailed(ClientSideCallContext clientSideCallContext) {

	}

	@Override
	public void setConfiguration(GenericRouterConfiguration configuration) {

	}
}
