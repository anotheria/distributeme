package org.distributeme.core.routing;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.distributeme.core.ClientSideCallContext;


/**
 * Created by rboehling on 2/21/17.
 */
public class DefaultBlacklistingStrategy implements BlacklistingStrategy {

	/**
	 * Map with timestamps for last server failures.
	 */
	private ConcurrentMap<String, Long> serverFailureTimestamps = new ConcurrentHashMap<String, Long>();
	private GenericRouterConfiguration configuration;

	@Override
	public boolean isBlacklisted(String selectedServiceId) {
		Long lastFailed = serverFailureTimestamps.get(selectedServiceId);
		return lastFailed != null && (System.currentTimeMillis() - lastFailed) < configuration.getBlacklistTime();
	}

	@Override
	public void notifyCallFailed(ClientSideCallContext clientSideCallContext) {
		serverFailureTimestamps.put(clientSideCallContext.getServiceId(), System.currentTimeMillis());
	}

	@Override
	public void setConfiguration(GenericRouterConfiguration configuration) {
		this.configuration = configuration;
	}
}
