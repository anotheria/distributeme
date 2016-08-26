package org.distributeme.core.routing;

/**
 * Router Strategy. Refactored from AbstractRounterWithFailoverToNextNode.
 *
 * @author lrosenberg
 * @since 13.03.15 00:59
 * @version $Id: $Id
 */
public enum RouterStrategy {
	/**
	 * Router based on Mod policy/strategy.
	 */
	MOD_ROUTER,
	/**
	 * Router based on RoundRobin policy/strategy.
	 */
	RR_ROUTER
}
