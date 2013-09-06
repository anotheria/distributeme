package org.distributeme.core.failing;

import org.distributeme.core.ClientSideCallContext;

/**
 * This call strategy will make one second pause and retry afterwards.
 * @author lrosenberg
 *
 */
public class WaitOneSecondAndRetry implements FailingStrategy{

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
