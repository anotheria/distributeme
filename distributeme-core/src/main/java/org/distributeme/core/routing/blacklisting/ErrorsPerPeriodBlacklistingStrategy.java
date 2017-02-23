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
public class ErrorsPerPeriodBlacklistingStrategy implements BlacklistingStrategy {

	private AtomicInteger currentPeriodErrorCounter = new AtomicInteger();
	private TimeProvider timeProvider = new SystemTimeProvider();
	private long startTime;
	private AtomicInteger nonStopPeriodsWithErrorsCounter = new AtomicInteger();
	private int errorsPerPeriodThreshold;
	private int periodDurationInSeconds;
	private int requiredNumberOfPeriodsWithErrors;
	private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	public ErrorsPerPeriodBlacklistingStrategy() {
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
		if(reachedThresholdWithinCurrentPeriod() && reachedThresholdInPreviousPeriods()) {
			return true;
		}
		return false;
	}

	private boolean reachedThresholdInPreviousPeriods() {
		return nonStopPeriodsWithErrorsCounter.get() >= (requiredNumberOfPeriodsWithErrors - 1);
	}

	private boolean reachedThresholdWithinCurrentPeriod() {
		return currentPeriodErrorCounter.get() >= errorsPerPeriodThreshold;
	}

	@Override
	public void notifyCallFailed(ClientSideCallContext clientSideCallContext) {
		currentPeriodErrorCounter.incrementAndGet();
	}

	@Override
	public void setConfiguration(GenericRouterConfiguration configuration) {

	}

	public void setErrorsPerPeriodThreshold(int errorsPerPeriodThreshold) {
		this.errorsPerPeriodThreshold = errorsPerPeriodThreshold;
	}

	public void setPeriodDurationInSeconds(int periodDurationInSeconds) {
		this.periodDurationInSeconds = periodDurationInSeconds;
	}

	void setTimeProvider(TimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	public void setRequiredNumberOfPeriodsWithErrors(int requiredNumberOfPeriodsWithErrors) {
		this.requiredNumberOfPeriodsWithErrors = requiredNumberOfPeriodsWithErrors;
	}

	void timerTick() {
		if(isTimePeriodExceeded()) {
			setNonStopPeriodsWithErrorCounter();
			currentPeriodErrorCounter.set(0);
		}
		resetStartTime();
	}

	private boolean isTimePeriodExceeded() {
		return ((timeProvider.getCurrentTimeMillis() - startTime) / 1000) > periodDurationInSeconds;
	}

	private void setNonStopPeriodsWithErrorCounter() {
		if(reachedThresholdWithinCurrentPeriod()) {
			nonStopPeriodsWithErrorsCounter.incrementAndGet();
		} else {
			nonStopPeriodsWithErrorsCounter.set(0);
		}
	}

	private void resetStartTime() {
		if(currentPeriodErrorCounter.get() == 0) {
			startTime = timeProvider.getCurrentTimeMillis();
		}
	}

}
