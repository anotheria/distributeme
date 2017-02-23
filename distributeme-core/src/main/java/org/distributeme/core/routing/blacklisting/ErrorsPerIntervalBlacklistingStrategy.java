package org.distributeme.core.routing.blacklisting;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.GenericRouterConfiguration;
import org.distributeme.core.util.SystemTimeProvider;
import org.distributeme.core.util.TimeProvider;


public class ErrorsPerIntervalBlacklistingStrategy implements BlacklistingStrategy {

	private static final int ONE_SECOND = 1000;
	private AtomicInteger currentIntervalErrorCounter = new AtomicInteger();
	private AtomicInteger nonStopIntervalsWithErrorsCounter = new AtomicInteger();
	private int errorsPerIntervalThreshold;
	private int intervalDurationInSeconds;
	private int requiredNumberOfIntervalsWithErrors;
	private long startTime;
	private TimeProvider timeProvider = new SystemTimeProvider();
	private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	public ErrorsPerIntervalBlacklistingStrategy() {
		scheduledExecutorService.scheduleAtFixedRate(new TimeTicker(), ONE_SECOND, ONE_SECOND, TimeUnit.MILLISECONDS);
	}

	ErrorsPerIntervalBlacklistingStrategy(ScheduledExecutorService scheduledExecutorService) {
		this.scheduledExecutorService = scheduledExecutorService;
	}

	private class TimeTicker implements Runnable {

		@Override
		public void run() {
			timerTick();
		}
	}

	@Override
	public boolean isBlacklisted(String selectedServiceId) {
		return isValidConfiguration() && (reachedRequiredNumberOfIntervalsWithErrors() || reachRequiredNumberOfIntervalsWithErrorsWithCurrentInterval());
	}

	private boolean isValidConfiguration() {
		return errorsPerIntervalThreshold > 0 && intervalDurationInSeconds > 0 && requiredNumberOfIntervalsWithErrors > 0;
	}

	private boolean reachedRequiredNumberOfIntervalsWithErrors() {
		return nonStopIntervalsWithErrorsCounter.get() >= requiredNumberOfIntervalsWithErrors;
	}

	private boolean reachRequiredNumberOfIntervalsWithErrorsWithCurrentInterval() {
		return reachedThresholdWithinCurrentInterval() && nonStopIntervalsWithErrorsCounter.get() >= requiredNumberOfIntervalsWithErrors - 1;
	}

	private boolean reachedThresholdWithinCurrentInterval() {
		return currentIntervalErrorCounter.get() >= errorsPerIntervalThreshold;
	}

	@Override
	public void notifyCallFailed(ClientSideCallContext clientSideCallContext) {
		currentIntervalErrorCounter.incrementAndGet();
	}

	@Override
	public void setConfiguration(GenericRouterConfiguration configuration) {

	}

	public void setErrorsPerIntervalThreshold(int errorsPerIntervalThreshold) {
		this.errorsPerIntervalThreshold = errorsPerIntervalThreshold;
	}

	public void setIntervalDurationInSeconds(int intervalDurationInSeconds) {
		this.intervalDurationInSeconds = intervalDurationInSeconds;
	}

	void setTimeProvider(TimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	public void setRequiredNumberOfIntervalsWithErrors(int requiredNumberOfIntervalsWithErrors) {
		this.requiredNumberOfIntervalsWithErrors = requiredNumberOfIntervalsWithErrors;
	}

	void timerTick() {
		if (isIntervalDurationReached()) {
			setNonStopIntervalsWithErrorCounter();
			currentIntervalErrorCounter.set(0);
			startTime = timeProvider.getCurrentTimeMillis();
		}
	}

	private boolean isIntervalDurationReached() {
		return ((timeProvider.getCurrentTimeMillis() - startTime) / 1000) >= intervalDurationInSeconds;
	}

	private void setNonStopIntervalsWithErrorCounter() {
		if (reachedThresholdWithinCurrentInterval()) {
			nonStopIntervalsWithErrorsCounter.incrementAndGet();
		} else {
			nonStopIntervalsWithErrorsCounter.set(0);
		}
	}

}
