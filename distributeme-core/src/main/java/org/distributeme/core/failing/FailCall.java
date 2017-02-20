package org.distributeme.core.failing;

import org.distributeme.core.ClientSideCallContext;

/**
 * This failing strategy simply fails the call and not tries to retry anything.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class FailCall implements FailingStrategy{

	/** {@inheritDoc} */
	@Override
	public FailDecision callFailed(ClientSideCallContext context) {
		return FailDecision.fail();
	}

}
