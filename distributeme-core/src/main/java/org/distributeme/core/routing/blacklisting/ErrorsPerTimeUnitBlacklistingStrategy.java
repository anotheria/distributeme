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
	private AtomicInteger errorCounter = new AtomicInteger();
	private TimeProvider timeProvider = new SystemTimeProvider();
	private long startTime;



	@Override
	public boolean isBlacklisted(String selectedServiceId) {
		if(errorCounter.get() >= errorsPerPeriodThreshold && !isTimePeriodExceeded()) {
			return true;
		}
		return false;
	}

	private boolean isTimePeriodExceeded() {
		return ((timeProvider.getCurrentTimeMillis() - startTime) / 1000) > periodDurationInSeconds;
	}

	@Override
	public void notifyCallFailed(ClientSideCallContext clientSideCallContext) {
		if(isTimePeriodExceeded()) {
			errorCounter.set(0);
		}
		if(errorCounter.get() == 0) {
			startTime = timeProvider.getCurrentTimeMillis();
		}
		errorCounter.incrementAndGet();
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
}
