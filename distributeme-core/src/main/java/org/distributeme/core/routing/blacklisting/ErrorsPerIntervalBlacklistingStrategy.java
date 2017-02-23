package org.distributeme.core.routing.blacklisting;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.GenericRouterConfiguration;
import org.distributeme.core.util.SystemTimeProvider;
import org.distributeme.core.util.TimeProvider;


/**
 * Created by rboehling on 2/22/17.
 */
public class ErrorsPerIntervalBlacklistingStrategy implements BlacklistingStrategy {

	private AtomicInteger currentIntervalErrorCounter = new AtomicInteger();
	private TimeProvider timeProvider = new SystemTimeProvider();
	private long startTime;
	private AtomicInteger nonStopIntervalsWithErrorsCounter = new AtomicInteger();
	private int errorsPerIntervalThreshold;
	private int intervalDurationInSeconds;
	private int requiredNumberOfIntervalsWithErrors;
	private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	public ErrorsPerIntervalBlacklistingStrategy() {
		scheduledExecutorService.schedule(new TimeTicker(), 10000, TimeUnit.MILLISECONDS);
	}

	private class TimeTicker implements Runnable {
		@Override
		public void run() {
			timerTick();
		}
	}

	@Override
	public boolean isBlacklisted(String selectedServiceId) {
		return reachedThresholdWithinCurrentInterval() && reachedThresholdInPreviousIntervals();
	}

	private boolean reachedThresholdInPreviousIntervals() {
		return nonStopIntervalsWithErrorsCounter.get() >= (requiredNumberOfIntervalsWithErrors - 1);
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
		if(isIntervalDurationExceeded()) {
			setNonStopIntervalsWithErrorCounter();
			currentIntervalErrorCounter.set(0);
		}
		resetStartTime();
	}

	void setScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
		this.scheduledExecutorService = scheduledExecutorService;
	}

	private boolean isIntervalDurationExceeded() {
		return ((timeProvider.getCurrentTimeMillis() - startTime) / 1000) > intervalDurationInSeconds;
	}

	private void setNonStopIntervalsWithErrorCounter() {
		if(reachedThresholdWithinCurrentInterval()) {
			nonStopIntervalsWithErrorsCounter.incrementAndGet();
		} else {
			nonStopIntervalsWithErrorsCounter.set(0);
		}
	}

	private void resetStartTime() {
		if(currentIntervalErrorCounter.get() == 0) {
			startTime = timeProvider.getCurrentTimeMillis();
		}
	}

}
