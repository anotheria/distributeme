package org.distributeme.core.routing;

import org.distributeme.core.ClientSideCallContext;


/**
 * Created by rboehling on 2/21/17.
 */
public interface BlacklistingStrategy {

	boolean isBlacklisted(String selectedServiceId);

	void notifyCallFailed(ClientSideCallContext clientSideCallContext);

	void setConfiguration(GenericRouterConfiguration configuration);
}
