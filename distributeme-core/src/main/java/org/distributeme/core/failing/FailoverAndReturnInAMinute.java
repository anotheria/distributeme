package org.distributeme.core.failing;

/**
 * This failing strategy / router combination is pretty much the same as Failover, but instead of staying on
 * the failover instance forever, it tries to switch back after a minute.
 *
 * @author another
 * @version $Id: $Id
 */
public class FailoverAndReturnInAMinute extends FailoverAndReturn{

	/** {@inheritDoc} */
	@Override
	protected long getFailbackTimeout() {
		return MINUTE;
	}

}
