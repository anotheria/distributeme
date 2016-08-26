package org.distributeme.core.failing;

import org.distributeme.core.ClientSideCallContext;

/**
 * This failing strategy always retries the call.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class RetryCall implements FailingStrategy{

	/** {@inheritDoc} */
	@Override
	public FailDecision callFailed(ClientSideCallContext context) {
		return FailDecision.retry();
	}

}
