package org.distributeme.core.routing.blacklisting;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.GenericRouterConfiguration;


/**
 * Interface to implement own strategies for blacklisting. Blacklisting is used from routers to
 * avoid calls to faulty to service instances.
 *
 * Created by rboehling on 2/21/17.
 */
public interface BlacklistingStrategy {

	/**
	 * Returns the blacklist decision for given service instanceID.
	 */
	boolean isBlacklisted(String instanceId);

	/**
	 * Called by the router when a call to service instance failed.
	 * @param clientSideCallContext
	 */
	void notifyCallFailed(ClientSideCallContext clientSideCallContext);

	/**
	 * Called by the router upon configuration change.
	 * @param configuration
	 */
	void setConfiguration(GenericRouterConfiguration configuration);
}
