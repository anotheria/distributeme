package org.distributeme.core.failing;

/**
 * This failing strategy / router combination is pretty much the same as Failover, but instead of staying on
 * the failover instance forever, it tries to switch back after 10 seconds.
 *
 * @author another
 * @version $Id: $Id
 */
public class FailoverAndReturnInTenSeconds extends FailoverAndReturn{

	/**
	 * Timeout aka 10 seconds.
	 */
	private static final long TIMEOUT = 10*SECOND;
	
	/** {@inheritDoc} */
	@Override
	protected long getFailbackTimeout() {
		return TIMEOUT;
	}

}
