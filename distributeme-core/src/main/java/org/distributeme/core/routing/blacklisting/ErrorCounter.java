package org.distributeme.core.routing.blacklisting;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Created by rboehling on 2/24/17.
 */
class ErrorCounter {

	private AtomicInteger currentIntervalErrorCounter = new AtomicInteger();
	private AtomicInteger nonStopIntervalsWithErrorsCounter = new AtomicInteger();
	private final ErrorsPerIntervalBlacklistingStrategyConfig config;
	private AtomicLong startTime = new AtomicLong(0);

	public ErrorCounter(ErrorsPerIntervalBlacklistingStrategyConfig config) {
		this.config = config;
	}

	boolean reachedRequiredNumberOfIntervalsWithErrors() {
		return nonStopIntervalsWithErrorsCounter.get() >= config.getRequiredNumberOfIntervalsWithErrors();
	}

	boolean reachRequiredNumberOfIntervalsWithErrorsWithCurrentInterval() {
		return reachedThresholdWithinCurrentInterval() && nonStopIntervalsWithErrorsCounter.get() >= config.getRequiredNumberOfIntervalsWithErrors() - 1;
	}

	boolean reachedThresholdWithinCurrentInterval() {
		return currentIntervalErrorCounter.get() >= config.getErrorsPerIntervalThreshold();
	}

	private void setNonStopIntervalsWithErrorCounter() {
		if (reachedThresholdWithinCurrentInterval()) {
			nonStopIntervalsWithErrorsCounter.incrementAndGet();
		} else {
			nonStopIntervalsWithErrorsCounter.set(0);
		}
	}

	public void notifyCallFailed() {
		currentIntervalErrorCounter.incrementAndGet();
	}

	void timerTick(long currentTimeMillis) {
		if (isIntervalDurationReached(currentTimeMillis)) {
			setNonStopIntervalsWithErrorCounter();
			currentIntervalErrorCounter.set(0);
			startTime.set(currentTimeMillis);
		}
	}

	private boolean isIntervalDurationReached(long currentTimeMillis) {
		return ((currentTimeMillis - startTime.get()) / 1000) >= config.getIntervalDurationInSeconds();
	}
}
