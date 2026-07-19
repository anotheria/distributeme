package org.distributeme.core.routing.blacklisting;

import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.GenericRouterConfiguration;
import org.distributeme.core.util.TestTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class ErrorsPerIntervalBlacklistingStrategyTest {

	private static final String SERVICE_ID_0 = "serviceId_0";
	private static final String SERVICE_ID_1 = "serviceId_1";
	private static final int INTERVAL_DURATION = 10;
	private static final int INTERVAL_DURATION_MINUS_ONE = INTERVAL_DURATION - 1;
	private static final int INTERVAL_DURATION_PLUS_TWO = INTERVAL_DURATION + 2;
	private TestTimeProvider timeProvider = new TestTimeProvider();
	private ScheduledExecutorService dummyScheduledExecutorService = mock(ScheduledExecutorService.class);
	private GenericRouterConfiguration routerConfiguration = new GenericRouterConfiguration();
	private Logger logger = mock(Logger.class);
	private ErrorsPerIntervalBlacklistingStrategy strategy = new ErrorsPerIntervalBlacklistingStrategy(dummyScheduledExecutorService, timeProvider, logger);

	@BeforeEach
	public void setUp() throws Exception {
		timeProvider.setCurrentMillis(0);
		assertFalse(strategy.isBlacklisted(SERVICE_ID_0), "ServiceId should not be blacklisted before failure call");
	}

	@Test
	public void isBlacklisted_IfErrorsPerIntervalExceedsThreshold_ForOneRequiredInterval_WithinInterval() {
		givenConfigWith("epbs_1RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold");

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);

		thenServiceIsBlacklisted(SERVICE_ID_0);
		afterOneHealthyIntervallTheServiceIsUnblacklisted(SERVICE_ID_0);
	}

	@Test
	public void isBlacklistedIfErrorsPerIntervalExceedsThreshold_ForOneRequiredInterval_OnIntervalBorder() {
		givenConfigWith("epbs_1RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold");

		whenNotifiyCallFailed(SERVICE_ID_0, 1);

		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsBlacklisted(SERVICE_ID_0);
		afterOneHealthyIntervallTheServiceIsUnblacklisted(SERVICE_ID_0);
	}

	@Test
	public void isBlacklistedIfErrorsPerIntervalExceedsThreshold_InFirstInterval_AndCurrentInterval() {
		givenConfigWith("epbs_2RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold");

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);

		thenServiceIsBlacklisted(SERVICE_ID_0);
		afterOneHealthyIntervallTheServiceIsUnblacklisted(SERVICE_ID_0);
	}

	@Test
	public void isNotBlacklistedIfErrorsExceedThreshold_InFirstInterval_ButZeroInCurrent() {
		givenConfigWith("epbs_2RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold");

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 0);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
		afterOneHealthyIntervallTheServiceIsUnblacklisted(SERVICE_ID_0);
	}

	@Test
	public void isNotBlacklistedIfErrorsPerIntervalDoNotExceedThreshold_WithinTwoLastIntervals() {
		givenConfigWith("epbs_2RequiredNumberOfIntervalsWithErrors_2ErrorsPerIntervalThreshold");

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 1);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
		afterOneHealthyIntervallTheServiceIsUnblacklisted(SERVICE_ID_0);
	}

	@Test
	public void isNotBlacklisted_IfFirstAndThirdIntervalHaveErrors_ButSecondIntervalHasNot() {
		givenConfigWith("epbs_2RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold");

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
		givenConfigWith("epbs_3RequiredNumberOfIntervalsWithErrors_3ErrorsPerIntervalThreshold");

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
		givenConfigWith("epbs_3RequiredNumberOfIntervalsWithErrors_3ErrorsPerIntervalThreshold");

		whenNotifiyCallFailed(SERVICE_ID_0, 3);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 4);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 3);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);
		thenServiceIsBlacklisted(SERVICE_ID_0);
		afterOneHealthyIntervallTheServiceIsUnblacklisted(SERVICE_ID_0);
	}

	@Test
	public void isBlacklisted_IfThreeRequiredIntervalsHaveErrors_ThirdIntervalOnBorder() {
		givenConfigWith("epbs_3RequiredNumberOfIntervalsWithErrors_3ErrorsPerIntervalThreshold");

		whenNotifiyCallFailed(SERVICE_ID_0, 3);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 4);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);

		whenNotifiyCallFailed(SERVICE_ID_0, 3);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsBlacklisted(SERVICE_ID_0);
		afterOneHealthyIntervallTheServiceIsUnblacklisted(SERVICE_ID_0);
	}

	@Test
	public void isNotBlackListedIfAllThresholdsAreSetToZero() {
		givenConfigWith("epbs_0RequiredNumberOfIntervalsWithErrors_0ErrorsPerIntervalThreshold");
		whenNotifiyCallFailed(SERVICE_ID_0, 0);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isNotBlacklistedIfStrategyConfigurationIsFaulty() {
		givenConfigWith("epbs_Minus1RequiredNumberOfIntervalsWithErrors_Minus1ErrorsPerIntervalThreshold");

		whenNotifiyCallFailed(SERVICE_ID_0, 0);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
		thenConfigurationErrorIsLoggedAsError("Invalid configuration epbs_Minus1RequiredNumberOfIntervalsWithErrors_Minus1ErrorsPerIntervalThreshold ErrorsPerIntervalBlacklistingStrategyConfig{errorsPerIntervalThreshold=-1, intervalDurationInSeconds=-1, requiredNumberOfIntervalsWithErrors=-1}");
	}


	@Test
	public void isNotBlacklistedIfStrategyIfConfigurationIsNotExistend() {
		givenConfigWith("notExists");

		whenNotifiyCallFailed(SERVICE_ID_0, 0);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
		thenConfigurationErrorIsLoggedAsWarning("Could not load configuration notExists");
	}

	private void thenConfigurationErrorIsLoggedAsWarning(String message) {
		verify(logger).warn(message);
	}

	private void thenConfigurationErrorIsLoggedAsError(String message) {
		verify(logger).error(message);
	}

	@Test
	public void whenOneServiceInstanceIsBlacklistedThenOtherHealthyInstanceIsNotBlacklisted() {
		givenConfigWith("epbs_1RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold");

		whenNotifiyCallFailed(SERVICE_ID_0, 1);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);

		thenServiceIsBlacklisted(SERVICE_ID_0);
		thenServiceIsNotBlacklisted(SERVICE_ID_1);
	}

	private void thenServiceIsBlacklisted(String serviceID) {
		assertTrue(strategy.isBlacklisted(serviceID), serviceID + "  should be blacklisted");
	}

	private void thenServiceIsNotBlacklisted(String serviceID) {
		assertFalse(strategy.isBlacklisted(serviceID), serviceID + " should not be blacklisted");
	}

	private void afterOneHealthyIntervallTheServiceIsUnblacklisted(String serviceId) {
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_PLUS_TWO);
		thenServiceIsNotBlacklisted(serviceId);
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
		routerConfiguration.setBlacklistStrategyConfigurationName(configName);
		strategy.setConfiguration(routerConfiguration);
	}
}