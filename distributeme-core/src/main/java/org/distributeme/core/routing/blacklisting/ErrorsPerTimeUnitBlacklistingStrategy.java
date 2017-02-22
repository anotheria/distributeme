package org.distributeme.core.routing.blacklisting;

import java.util.concurrent.atomic.AtomicInteger;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.GenericRouterConfiguration;
import org.distributeme.core.util.SystemTimeProvider;
import org.distributeme.core.util.TimeProvider;


/**
 * Created by rboehling on 2/22/17.
 */
public class ErrorsPerTimeUnitBlacklistingStrategy implements BlacklistingStrategy {


	private int errorsPerPeriodThreshold = 10;
	private int periodDurationInSeconds = 10;
	private AtomicInteger currentPeriodErrorCounter = new AtomicInteger();
	private TimeProvider timeProvider = new SystemTimeProvider();
	private long startTime;
	private AtomicInteger nonStopPeriodsWithErrorsCounter = new AtomicInteger(0);
	private int requiredNumberOfPeriodsWithErrors = 1;




	@Override
	public boolean isBlacklisted(String selectedServiceId) {
		if(errorCountReachesThreshold() && reachedThresholdsInPreviousPeriods()) {
			return true;
		}
		return false;
	}

	private boolean reachedThresholdsInPreviousPeriods() {
		return nonStopPeriodsWithErrorsCounter.get() >= (requiredNumberOfPeriodsWithErrors - 1);
	}

	private boolean errorCountReachesThreshold() {
		return currentPeriodErrorCounter.get() >= errorsPerPeriodThreshold;
	}

	private boolean isTimePeriodExceeded() {
		return ((timeProvider.getCurrentTimeMillis() - startTime) / 1000) > periodDurationInSeconds;
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

	private void setNonStopPeriodsWithErrorCounter() {
		if(errorCountReachesThreshold()) {
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
