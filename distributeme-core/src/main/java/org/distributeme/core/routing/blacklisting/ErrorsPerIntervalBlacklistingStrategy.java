package org.distributeme.core.routing.blacklisting;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.configureme.ConfigurationManager;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.GenericRouterConfiguration;
import org.distributeme.core.util.SystemTimeProvider;
import org.distributeme.core.util.TimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ErrorsPerIntervalBlacklistingStrategy implements BlacklistingStrategy {

	private Logger logger = LoggerFactory.getLogger(ErrorsPerIntervalBlacklistingStrategy.class);
	private static final int ONE_SECOND = 1000;
	private AtomicInteger currentIntervalErrorCounter = new AtomicInteger();
	private AtomicInteger nonStopIntervalsWithErrorsCounter = new AtomicInteger();
	private AtomicLong startTime = new AtomicLong(0);
	private TimeProvider timeProvider = new SystemTimeProvider();
	private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	private ErrorsPerIntervalBlacklistingStrategyConfig config = new ErrorsPerIntervalBlacklistingStrategyConfig();


	public ErrorsPerIntervalBlacklistingStrategy() {
		scheduledExecutorService.scheduleAtFixedRate(new TimeTicker(), ONE_SECOND, ONE_SECOND, TimeUnit.MILLISECONDS);
		try {
			ConfigurationManager.INSTANCE.configureAs(config, "errorsPerIntervalBlacklistingStrategy");
		} catch (Exception e) {
			logger.warn("Could not load configuration errorsPerIntervalBlacklistingStrategy.json", e);
		}
	}

	ErrorsPerIntervalBlacklistingStrategy(ScheduledExecutorService scheduledExecutorService, TimeProvider timeProvider) {
		this.scheduledExecutorService = scheduledExecutorService;
		this.timeProvider = timeProvider;
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
		return config.getErrorsPerIntervalThreshold() > 0 && config.getIntervalDurationInSeconds() > 0 && config.getRequiredNumberOfIntervalsWithErrors() > 0;
	}

	private boolean reachedRequiredNumberOfIntervalsWithErrors() {
		return nonStopIntervalsWithErrorsCounter.get() >= config.getRequiredNumberOfIntervalsWithErrors();
	}

	private boolean reachRequiredNumberOfIntervalsWithErrorsWithCurrentInterval() {
		return reachedThresholdWithinCurrentInterval() && nonStopIntervalsWithErrorsCounter.get() >= config.getRequiredNumberOfIntervalsWithErrors() - 1;
	}

	private boolean reachedThresholdWithinCurrentInterval() {
		return currentIntervalErrorCounter.get() >= config.getErrorsPerIntervalThreshold();
	}

	@Override
	public void notifyCallFailed(ClientSideCallContext clientSideCallContext) {
		currentIntervalErrorCounter.incrementAndGet();
	}

	@Override
	public void setConfiguration(GenericRouterConfiguration configuration) {

	}

	void setErrorsPerIntervalThreshold(int errorsPerIntervalThreshold) {
		config.setErrorsPerIntervalThreshold(errorsPerIntervalThreshold);
	}

	void setIntervalDurationInSeconds(int intervalDurationInSeconds) {
		config.setIntervalDurationInSeconds(intervalDurationInSeconds);
	}

	void setRequiredNumberOfIntervalsWithErrors(int requiredNumberOfIntervalsWithErrors) {
		config.setRequiredNumberOfIntervalsWithErrors(requiredNumberOfIntervalsWithErrors);
	}

	void timerTick() {
		if (isIntervalDurationReached()) {
			setNonStopIntervalsWithErrorCounter();
			currentIntervalErrorCounter.set(0);
			startTime.set(timeProvider.getCurrentTimeMillis());
		}
	}

	private boolean isIntervalDurationReached() {
		return ((timeProvider.getCurrentTimeMillis() - startTime.get()) / 1000) >= config.getIntervalDurationInSeconds();
	}

	private void setNonStopIntervalsWithErrorCounter() {
		if (reachedThresholdWithinCurrentInterval()) {
			nonStopIntervalsWithErrorsCounter.incrementAndGet();
		} else {
			nonStopIntervalsWithErrorsCounter.set(0);
		}
	}

}
