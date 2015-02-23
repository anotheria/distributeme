package org.distributeme.core.failing;

import org.distributeme.core.ClientSideCallContext;

/**
 * This failing strategy simply fails the call and not tries to retry anything. 
 * @author lrosenberg
 */
public class FailCall implements FailingStrategy{

	@Override
	public FailDecision callFailed(ClientSideCallContext context) {
		return FailDecision.fail();
	}

}
