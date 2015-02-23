package org.distributeme.core.failing;

import org.distributeme.core.ClientSideCallContext;

/**
 * This failing strategy only retries the call once. This is useful in combination with failing aware routers, because the strategy itself will send the 
 * call to the same service.
 * @author lrosenberg
 *
 */
public class RetryCallOnce implements FailingStrategy{

	@Override
	public FailDecision callFailed(ClientSideCallContext context) {
		return FailDecision.retryOnce();
	}
}
