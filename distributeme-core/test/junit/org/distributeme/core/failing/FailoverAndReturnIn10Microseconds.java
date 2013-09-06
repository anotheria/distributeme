package org.distributeme.core.failing;

/**
 * This extension is for test of Failover And Return.
 * @author lrosenberg
 *
 */
public class FailoverAndReturnIn10Microseconds extends FailoverAndReturn{

	@Override
	protected long getFailbackTimeout() {
		return 9;
	}

}
