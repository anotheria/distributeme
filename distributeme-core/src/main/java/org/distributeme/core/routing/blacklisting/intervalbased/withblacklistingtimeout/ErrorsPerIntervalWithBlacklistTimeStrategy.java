package org.distributeme.core.routing.blacklisting.intervalbased.withblacklistingtimeout;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.configureme.ConfigurationManager;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.GenericRouterConfiguration;
import org.distributeme.core.routing.blacklisting.BlacklistDecision;
import org.distributeme.core.routing.blacklisting.BlacklistingStrategy;
import org.distributeme.core.util.SystemTimeProvider;
import org.distributeme.core.util.TimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A service instance is blacklisted after x successive intervals of length y, in which
 * z failures occur. The blacklisting condition is fullfilled as soon as the error count is reached within
 * the current interval.
 * After blacklisting b seconds the service instance is going to be whitelisted again. During b no further
 * error checks are done. After whitelisting, the error detection starts again.
 * b=20, x=2, y=10, z=1
 * <pre>{@code
 * 0-----4------10-------16-----20------------30------36-----40(Seconds)->
 *       ^               ^                            ^
 *       |               |----------------------------|
 *     Error        Error ->  Blacklisted           Whitelisted again
 * }</pre>
 * b, x, y and z are configurable via configureme.
 * b: blacklistTime
 * x: requiredNumberOfIntervalsWithErrors
 * y: intervalDurationInSeconds
 * z: errorsPerIntervalThreshold
 * If no valid configuration is given, then getDecision() always returns false.
 *
 * @author rboehling
 */
public class ErrorsPerIntervalWithBlacklistTimeStrategy implements BlacklistingStrategy {

	private Logger logger = LoggerFactory.getLogger(ErrorsPerIntervalWithBlacklistTimeStrategy.class);
	private static final long ONE_SECOND = TimeUnit.SECONDS.toMillis(1);
	/**
	 * Map with blacklisting decider for each service instance
	 */
	private ConcurrentHashMap<String, BlacklistingDecider> deciderMap = new ConcurrentHashMap<>();
	/**
	 * timeProvider abstracts System.currentTimeMillis() to enable unit testing
	 */
	private TimeProvider timeProvider = new SystemTimeProvider();
	private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	private ErrorsPerIntervalWithBlacklistingTimeConfig config = new ErrorsPerIntervalWithBlacklistingTimeConfig();
	private AtomicBoolean validConfiguration = new AtomicBoolean(false);

	public ErrorsPerIntervalWithBlacklistTimeStrategy() {
		scheduledExecutorService.scheduleAtFixedRate(new TimeTicker(), ONE_SECOND, ONE_SECOND, TimeUnit.MILLISECONDS);
	}

	/**
	 * package private construtor intended for unit tests
	 *
	 * @param scheduledExecutorService
	 * @param timeProvider
	 * @param logger
	 */
	ErrorsPerIntervalWithBlacklistTimeStrategy(ScheduledExecutorService scheduledExecutorService, TimeProvider timeProvider, Logger logger) {
		this.scheduledExecutorService = scheduledExecutorService;
		this.timeProvider = timeProvider;
		this.logger = logger;
	}

	private class TimeTicker implements Runnable {

		@Override
		public void run() {
			timerTick();
		}
	}

	@Override
	public boolean isBlacklisted(String instanceId) {
		if (!validConfiguration.get()) {
			return false;
		}
		BlacklistingDecider blacklistingDeciderForService = getBlacklistingDecider(instanceId);
		BlacklistDecision decision = blacklistingDeciderForService.getDecision();
		if (decision.statusChanged()) {
			logger.info(instanceId + " blacklisting changed  to " + decision);
		}
		return decision.isBlacklisted();
	}

	private BlacklistingDecider getBlacklistingDecider(String selectedServiceId) {
		BlacklistingDecider decider = deciderMap.get(selectedServiceId);
		if (decider == null) {
			decider = new BlacklistingDecider(config);
			BlacklistingDecider existingDecider = deciderMap.putIfAbsent(selectedServiceId, decider);
			if (existingDecider != null) {
				decider = existingDecider;
			}
		}
		return decider;
	}

	private boolean isValidConfiguration() {
		return config.getErrorsPerIntervalThreshold() > 0 && config.getIntervalDurationInSeconds() > 0 && config.getRequiredNumberOfIntervalsWithErrors() > 0;
	}

	@Override
	public void notifyCallFailed(ClientSideCallContext clientSideCallContext) {
		BlacklistingDecider blacklistingDecider = getBlacklistingDecider(clientSideCallContext.getServiceId());
		blacklistingDecider.notifyCallFailed();
	}

	@Override
	public void setConfiguration(GenericRouterConfiguration configuration) {
		try {
			ConfigurationManager.INSTANCE.configureAs(config, configuration.getBlacklistStrategyConfigurationName());
			if (isValidConfiguration()) {
				validConfiguration.set(true);
				logger.info("New configuration " + configuration.getBlacklistStrategyConfigurationName() + " loaded " + config);
			} else {
				validConfiguration.set(false);
				logger.error("Invalid configuration " + configuration.getBlacklistStrategyConfigurationName() + " " + config);
			}
		} catch (Exception e) {
			logger.warn("Could not load configuration " + configuration.getBlacklistStrategyConfigurationName());
		}
	}

	void timerTick() {
		for (Map.Entry<String, BlacklistingDecider> entry : deciderMap.entrySet()) {
			entry.getValue().timerTick(timeProvider.getCurrentTimeMillis());
		}
	}
}