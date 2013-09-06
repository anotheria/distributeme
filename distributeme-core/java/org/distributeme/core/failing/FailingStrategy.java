package org.distributeme.core.failing;

import org.distributeme.core.ClientSideCallContext;

/**
 * This interface describes a decision utility for reaction on failing.
 * @author lrosenberg
 *
 */
public interface FailingStrategy {
	/**
	 * What should happen if a call is failed.
	 * @return
	 */
	FailDecision callFailed(ClientSideCallContext context);
}
