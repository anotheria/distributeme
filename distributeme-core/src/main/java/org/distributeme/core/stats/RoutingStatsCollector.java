package org.distributeme.core.stats;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 21.09.15 17:29
 */
public interface RoutingStatsCollector {

	void addFailedCall();

	void addFailDecision();

	void addRetryDecision();

	void addRequestRoutedTo();

	void addBlacklisted();
}
