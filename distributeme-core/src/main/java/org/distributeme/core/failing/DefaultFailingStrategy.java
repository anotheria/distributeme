package org.distributeme.core.failing;

import org.distributeme.core.ClientSideCallContext;

/**
 * This failing strategy is used if no other failing strategy is specified.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class DefaultFailingStrategy implements FailingStrategy{

	/** {@inheritDoc} */
	@Override
	public FailDecision callFailed(ClientSideCallContext context) {
		return FailDecision.fail();
	}

}
