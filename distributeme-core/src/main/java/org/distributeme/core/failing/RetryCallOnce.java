package org.distributeme.core.failing;

import org.distributeme.core.ClientSideCallContext;

/**
 * This failing strategy only retries the call once. This is useful in combination with failing aware routers, because the strategy itself will send the
 * call to the same service.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class RetryCallOnce implements FailingStrategy{

	/** {@inheritDoc} */
	@Override
	public FailDecision callFailed(ClientSideCallContext context) {
		return FailDecision.retryOnce();
	}
}
