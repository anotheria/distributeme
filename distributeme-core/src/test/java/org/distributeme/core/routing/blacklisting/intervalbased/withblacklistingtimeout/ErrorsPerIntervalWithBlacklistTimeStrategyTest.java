package org.distributeme.core.routing.blacklisting.intervalbased.withblacklistingtimeout;

import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.GenericRouterConfiguration;
import org.distributeme.core.util.TestTimeProvider;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class ErrorsPerIntervalWithBlacklistTimeStrategyTest {

	private static final String SERVICE_ID_0 = "serviceId_0";
	private static final String SERVICE_ID_1 = "serviceId_1";
	private static final int INTERVAL_DURATION = 10;
	private static final int INTERVAL_DURATION_MINUS_ONE = INTERVAL_DURATION - 1;
	private static final int INTERVAL_DURATION_PLUS_TWO = INTERVAL_DURATION + 2;
	private static final int WHITELIST_AFTER_SECONDS = 20;
	private static final int WHITELIST_AFTER_SECONDS_PLUS_ONE = WHITELIST_AFTER_SECONDS + 1;
	private static final String CONFIG_FILE_DIR = "org/distributeme/core/routing/blacklisting/intervalbased/withblacklistingtimeout/";
	private TestTimeProvider timeProvider = new TestTimeProvider();
	private ScheduledExecutorService dummyScheduledExecutorService = mock(ScheduledExecutorService.class);
	private GenericRouterConfiguration routerConfiguration = new GenericRouterConfiguration();
	private Logger logger = mock(Logger.class);
	private ErrorsPerIntervalWithBlacklistTimeStrategy strategy = new ErrorsPerIntervalWithBlacklistTimeStrategy(dummyScheduledExecutorService, timeProvider, logger);

	@Before
	public void setUp() throws Exception {
		timeProvider.setCurrentMillis(0);
		assertFalse("ServiceId should not be blacklisted before failure call", strategy.isBlacklisted(SERVICE_ID_0));
	}

	@Test
	public void isWhitelistedAfterBlacklistingTimeIsUpIfErrorsPerIntervalExceedsThreshold_ForOneRequiredInterval_WithinInterval() {
		givenConfigWith("epbs_1RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold_whitelistAfter20Seconds");

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);

		thenServiceIsBlacklisted(SERVICE_ID_0);

		whenJumpInTimePlusSeconds(WHITELIST_AFTER_SECONDS_PLUS_ONE);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isBlacklistedIfWhiteListAfterSecondsIsNotReached() {
		givenConfigWith("epbs_1RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold_whitelistAfter20Seconds");

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);

		thenServiceIsBlacklisted(SERVICE_ID_0);

		whenJumpInTimePlusSeconds(WHITELIST_AFTER_SECONDS);
		thenServiceIsBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isWhitelistedAfterMaxBlackListTimeReached_After1ErrorInterval() {
		givenConfigWith("epbs_1RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold_whitelistAfter20Seconds");

		whenNotifiyCallFailed(SERVICE_ID_0, 1);

		whenJumpInTimePlusSeconds(INTERVAL_DURATION);

		thenServiceIsBlacklisted(SERVICE_ID_0);

		whenJumpInTimePlusSeconds(WHITELIST_AFTER_SECONDS_PLUS_ONE);

		thenServiceIsNotBlacklisted(SERVICE_ID_0);
	}


	@Test
	public void isBlacklistedAgainAfterItWasWhitelisted() {
		givenConfigWith("epbs_1RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold_whitelistAfter20Seconds");
		whenNotifiyCallFailed(SERVICE_ID_0, 1);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);
		thenServiceIsBlacklisted(SERVICE_ID_0);

		whenJumpInTimePlusSeconds(WHITELIST_AFTER_SECONDS_PLUS_ONE);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 1);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);
		thenServiceIsBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isBlacklistedIfWhiteListAfterSecondsIsNotReachedWhenBlacklistingOccuredWithinInterval() {
		givenConfigWith("epbs_1RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold_whitelistAfter20Seconds");

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);

		thenServiceIsBlacklisted(SERVICE_ID_0);

		whenJumpInTimePlusSeconds(WHITELIST_AFTER_SECONDS);
		thenServiceIsBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isWhitelistedAfterMaxBlackListTimeReached_After1ErrorIntervalWhenBlacklistingOccuredWithinInterval() {
		givenConfigWith("epbs_1RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold_whitelistAfter20Seconds");

		whenNotifiyCallFailed(SERVICE_ID_0, 1);

		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);

		thenServiceIsBlacklisted(SERVICE_ID_0);

		whenJumpInTimePlusSeconds(WHITELIST_AFTER_SECONDS_PLUS_ONE);

		thenServiceIsNotBlacklisted(SERVICE_ID_0);
	}


	@Test
	public void isWhitelistedAfterMaxBlacklistingTimeReached_AfterBlacklistedIfErrorsPerIntervalExceedsThreshold_InFirstInterval_AndCurrentInterval() {
		givenConfigWith("epbs_2RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold_whitelistAfter20Seconds");

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE); // <- means the second interval marks the service as blacklisted in the 19th second

		thenServiceIsBlacklisted(SERVICE_ID_0);

		whenJumpInTimePlusSeconds(WHITELIST_AFTER_SECONDS_PLUS_ONE);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isNotBlacklistedIfErrorsExceedThreshold_InFirstInterval_ButZeroInCurrent() {
		givenConfigWith("epbs_2RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold_whitelistAfter20Seconds");

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 0);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isNotBlacklistedIfErrorsPerIntervalDoNotExceedThreshold_WithinTwoLastIntervals() {
		givenConfigWith("epbs_2RequiredNumberOfIntervalsWithErrors_2ErrorsPerIntervalThreshold_whitelistAfter20Seconds");

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 1);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isNotBlacklisted_IfFirstAndThirdIntervalHaveErrors_ButSecondIntervalHasNot() {
		givenConfigWith("epbs_2RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold_whitelistAfter20Seconds");

		whenNotifiyCallFailed(SERVICE_ID_0, 1);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 0);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 1);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isNotBlacklisted_IfFirstAndSecondIntervalHaveErrorsButThirdHasNot() {
		givenConfigWith("epbs_3RequiredNumberOfIntervalsWithErrors_3ErrorsPerIntervalThreshold_whitelistAfter20Seconds");

		whenNotifiyCallFailed(SERVICE_ID_0, 3);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 4);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 1);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isBlacklisted_IfThreeRequiredIntervalsHaveErrors_ThirdIntervalNotOnBorder() {
		givenConfigWith("epbs_3RequiredNumberOfIntervalsWithErrors_3ErrorsPerIntervalThreshold_whitelistAfter20Seconds");

		whenNotifiyCallFailed(SERVICE_ID_0, 3);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 4);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 3);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);
		thenServiceIsBlacklisted(SERVICE_ID_0);

		whenJumpInTimePlusSeconds(WHITELIST_AFTER_SECONDS_PLUS_ONE);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isBlacklisted_IfThreeRequiredIntervalsHaveErrors_ThirdIntervalOnBorder() {
		givenConfigWith("epbs_3RequiredNumberOfIntervalsWithErrors_3ErrorsPerIntervalThreshold_whitelistAfter20Seconds");

		whenNotifiyCallFailed(SERVICE_ID_0, 3);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 4);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 3);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsBlacklisted(SERVICE_ID_0);

		whenJumpInTimePlusSeconds(WHITELIST_AFTER_SECONDS_PLUS_ONE);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isNotBlackListedIfAllThresholdsAreSetToZero() {
		givenConfigWith("epbs_0RequiredNumberOfIntervalsWithErrors_0ErrorsPerIntervalThreshold_whitelistAfter20Seconds");
		whenNotifiyCallFailed(SERVICE_ID_0, 0);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isNotBlacklistedIfStrategyConfigurationIsFaulty() {
		givenConfigWith("epbs_Minus1RequiredNumberOfIntervalsWithErrors_Minus1ErrorsPerIntervalThreshold_whitelistAfter20Seconds");

		whenNotifiyCallFailed(SERVICE_ID_0, 0);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
		thenConfigurationErrorIsLoggedAsError("epbs_Minus1RequiredNumberOfIntervalsWithErrors_Minus1ErrorsPerIntervalThreshold_whitelistAfter20Seconds ErrorsPerIntervalBlacklistingStrategyConfig{errorsPerIntervalThreshold=-1, intervalDurationInSeconds=-1, requiredNumberOfIntervalsWithErrors=-1, blacklistTime=-1}");
	}



	@Test
	public void isNotBlacklistedIfStrategyConfigurationIsNotExistend() {
		givenConfigWith("notExists");

		whenNotifiyCallFailed(SERVICE_ID_0, 0);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
		thenConfigurationErrorIsLoggedAsWarning("notExists");
	}

	private void thenConfigurationErrorIsLoggedAsWarning(String configFileName) {
		verify(logger).warn("Could not load configuration " + CONFIG_FILE_DIR + configFileName);
	}

	private void thenConfigurationErrorIsLoggedAsError(String configFileName) {
		verify(logger).error("Invalid configuration " + CONFIG_FILE_DIR + configFileName);
	}

	@Test
	public void whenOneServiceInstanceIsBlacklistedThenOtherHealthyInstanceIsNotBlacklisted() {
		givenConfigWith("epbs_1RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold_whitelistAfter20Seconds");

		whenNotifiyCallFailed(SERVICE_ID_0, 1);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);

		thenServiceIsBlacklisted(SERVICE_ID_0);
		thenServiceIsNotBlacklisted(SERVICE_ID_1);
	}

	private void thenServiceIsBlacklisted(String serviceID) {
		assertTrue(serviceID + "  should be blacklisted", strategy.isBlacklisted(serviceID));
	}

	private void thenServiceIsNotBlacklisted(String serviceID) {
		assertFalse(serviceID + " should not be blacklisted", strategy.isBlacklisted(serviceID));
	}

	private void whenJumpInTimePlusSeconds(int seconds) {
		for(int i = 1; i <= seconds; i++) {
			timeProvider.increaseByOneSecond();
			strategy.timerTick();
		}
	}

	private void whenNotifiyCallFailed(String serviceID, int times) {
		for (int i = 0; i < times; i++) {
			strategy.notifyCallFailed(new ClientSideCallContext(serviceID, "someMethod", Collections.emptyList()));
		}
	}

	private void givenConfigWith(String configName) {
		routerConfiguration.setBlacklistStrategyConfigurationName(CONFIG_FILE_DIR + configName);
		strategy.setConfiguration(routerConfiguration);
	}
}