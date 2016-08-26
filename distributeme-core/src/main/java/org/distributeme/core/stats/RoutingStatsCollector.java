package org.distributeme.core.stats;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 21.09.15 17:29
 * @version $Id: $Id
 */
public interface RoutingStatsCollector {

	/**
	 * <p>addFailedCall.</p>
	 */
	void addFailedCall();

	/**
	 * <p>addFailDecision.</p>
	 */
	void addFailDecision();

	/**
	 * <p>addRetryDecision.</p>
	 */
	void addRetryDecision();

	/**
	 * <p>addRequestRoutedTo.</p>
	 */
	void addRequestRoutedTo();

	/**
	 * <p>addBlacklisted.</p>
	 */
	void addBlacklisted();
}
