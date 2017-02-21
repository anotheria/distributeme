package org.distributeme.core.routing.blacklisting;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.GenericRouterConfiguration;


/**
 * Created by rboehling on 2/21/17.
 */
public class DebugBlacklistingStrategy implements BlacklistingStrategy {

	@Override
	public boolean isBlacklisted(String selectedServiceId) {
		System.out.println("DebugBlacklistingStrategy isBlacklisted for " + selectedServiceId);
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
