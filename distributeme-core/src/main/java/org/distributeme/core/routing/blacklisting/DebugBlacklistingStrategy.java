package org.distributeme.core.routing.blacklisting;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.GenericRouterConfiguration;


/**
 * Debug blacklisting strategie. This strategy actually never blacklists any service instance.
 * It just prints a debug message on system out. This might be useful for debugging.
 * Created by rboehling on 2/21/17.
 */
public class DebugBlacklistingStrategy implements BlacklistingStrategy {

	@Override
	public boolean isBlacklisted(String instanceId) {
		System.out.println("DebugBlacklistingStrategy isBlacklisted called for " + instanceId);
		return false;
	}

	@Override
	public void notifyCallFailed(ClientSideCallContext clientSideCallContext) {
		System.out.println("DebugBlacklistingStrategy notifyCallFailed for " + clientSideCallContext);
	}

	@Override
	public void setConfiguration(GenericRouterConfiguration configuration) {
		System.out.println("DebugBlacklistingStrategy setConfiguration for " + configuration);
	}
}
