package org.distributeme.core.failing;

import org.distributeme.core.ClientSideCallContext;

/**
 * This failing strategy always retries the call. 
 * @author lrosenberg
 */
public class RetryCall implements FailingStrategy{

	@Override
	public FailDecision callFailed(ClientSideCallContext context) {
		return FailDecision.retry();
	}

}
