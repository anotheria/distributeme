package org.distributeme.core.failing;

import org.distributeme.core.ClientSideCallContext;

/**
 * This interface describes a decision utility for reaction on failing.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public interface FailingStrategy {
	/**
	 * What should happen if a call is failed.
	 *
	 * @param context a {@link org.distributeme.core.ClientSideCallContext} object.
	 * @return a {@link org.distributeme.core.failing.FailDecision} object.
	 */
	FailDecision callFailed(ClientSideCallContext context);
}
