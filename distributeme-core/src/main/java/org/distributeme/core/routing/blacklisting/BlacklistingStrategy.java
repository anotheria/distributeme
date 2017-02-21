package org.distributeme.core.routing.blacklisting;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.GenericRouterConfiguration;


/**
 * Created by rboehling on 2/21/17.
 */
public interface BlacklistingStrategy {

	boolean isBlacklisted(String selectedServiceId);

	void notifyCallFailed(ClientSideCallContext clientSideCallContext);

	void setConfiguration(GenericRouterConfiguration configuration);
}
