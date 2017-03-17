package org.distributeme.core.routing;

import org.distributeme.core.stats.RoutingStatsCollector;

public class RoutingStatsCollectorForTest implements RoutingStatsCollector {

	private int failedCall = 0;
	private int failedDecision = 0;
	private int retryDecision = 0;
	private int requestRoutedTo = 0;
	private int blacklisted = 0;

	@Override
	public void addFailedCall() {
		failedCall++;
	}

	@Override
	public void addFailDecision() {
		failedDecision++;
	}

	@Override
	public void addRetryDecision() {
		retryDecision++;
	}

	@Override
	public void addRequestRoutedTo() {
		requestRoutedTo++;
	}

	@Override
	public void addBlacklisted() {
		blacklisted++;
	}

	public int getFailedCall() {
		return failedCall;
	}

	public int getFailedDecision() {
		return failedDecision;
	}

	public int getRetryDecision() {
		return retryDecision;
	}

	public int getRequestRoutedTo() {
		return requestRoutedTo;
	}

	public int getBlacklisted() {
		return blacklisted;
	}
}
