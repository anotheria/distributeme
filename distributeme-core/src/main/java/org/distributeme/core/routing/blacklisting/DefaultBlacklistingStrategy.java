package org.distributeme.core.routing.blacklisting;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.GenericRouterConfiguration;


/**
 * Default blacklisting strategy. A service instance will be blacklisted after one failed call.
 * This behavior was default in distributeme 2.3.1
 *
 *
 * Created by rboehling on 2/21/17.
 */
public class DefaultBlacklistingStrategy implements BlacklistingStrategy {

	/**
	 * Map with timestamps for last server failures.
	 */
	private ConcurrentMap<String, Long> serverFailureTimestamps = new ConcurrentHashMap<String, Long>();
	private GenericRouterConfiguration configuration;

	@Override
	public boolean isBlacklisted(String instanceId) {
		Long lastFailed = serverFailureTimestamps.get(instanceId);
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
