package org.distributeme.core.routing.blacklisting;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
	private ConcurrentHashMap<String, ErrorCounter> errorCountersForServices = new ConcurrentHashMap<>();
	private TimeProvider timeProvider = new SystemTimeProvider();
	private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	private ErrorsPerIntervalBlacklistingStrategyConfig config = new ErrorsPerIntervalBlacklistingStrategyConfig();
	private AtomicBoolean validConfiguration = new AtomicBoolean(false);


	public ErrorsPerIntervalBlacklistingStrategy() {
		scheduledExecutorService.scheduleAtFixedRate(new TimeTicker(), ONE_SECOND, ONE_SECOND, TimeUnit.MILLISECONDS);
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
		if(!validConfiguration.get()) {
			return false;
		}
		ErrorCounter errorCounterForService = getErrorCounters(selectedServiceId);
		BlacklistDecision blacklisted = errorCounterForService.isBlacklisted();
		if(blacklisted.statusChanged()) {
			logger.info(selectedServiceId + " blacklisting changed  to " + blacklisted);
		}
		return blacklisted.isBlacklisted();
	}


	private ErrorCounter getErrorCounters(String selectedServiceId) {
		ErrorCounter errorCounters = errorCountersForServices.get(selectedServiceId);
		if(errorCounters == null) {
			errorCounters = new ErrorCounter(config);
			ErrorCounter errorCountersOld = errorCountersForServices.putIfAbsent(selectedServiceId, errorCounters);
			if(errorCountersOld != null) {
				errorCounters = errorCountersOld;
			}
		}
		return errorCounters;
	}

	private boolean isValidConfiguration() {
		return config.getErrorsPerIntervalThreshold() > 0 && config.getIntervalDurationInSeconds() > 0 && config.getRequiredNumberOfIntervalsWithErrors() > 0;
	}


	@Override
	public void notifyCallFailed(ClientSideCallContext clientSideCallContext) {
		ErrorCounter errorCounters = getErrorCounters(clientSideCallContext.getServiceId());
		errorCounters.notifyCallFailed();
	}

	@Override
	public void setConfiguration(GenericRouterConfiguration configuration) {
		try {
			ConfigurationManager.INSTANCE.configureAs(config, configuration.getBlacklistStrategyConfigurationName());
			if(isValidConfiguration()) {
				validConfiguration.set(true);
			} else {
				validConfiguration.set(false);
				logger.warn("Invalid configuration " + configuration.getBlacklistStrategyConfigurationName() + " " + config);
			}
		} catch (Exception e) {
			logger.warn("Could not load configuration " + configuration.getBlacklistStrategyConfigurationName());
		}
	}

	void timerTick() {
		for(Map.Entry<String, ErrorCounter> entry: errorCountersForServices.entrySet()) {
			entry.getValue().timerTick(timeProvider.getCurrentTimeMillis());
		}
	}

	void setLogger(Logger logger) {
		this.logger = logger;
	}
}
