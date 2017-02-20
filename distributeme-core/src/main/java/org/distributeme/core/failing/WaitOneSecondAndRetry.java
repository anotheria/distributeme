package org.distributeme.core.failing;

import org.distributeme.core.ClientSideCallContext;

/**
 * This call strategy will make one second pause and retry afterwards.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class WaitOneSecondAndRetry implements FailingStrategy{

	/** {@inheritDoc} */
	@Override
	public FailDecision callFailed(ClientSideCallContext context) {
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
			//ignored
		}
		return FailDecision.retry();
	}

}
