package org.distributeme.core.routing.blacklisting;

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


public class ErrorsPerIntervalBlacklistingStrategyTest {

	private static final String SERVICE_ID_0 = "serviceId_0";
	private static final String SERVICE_ID_1 = "serviceId_1";
	private static final int INTERVAL_DURATION = 10;
	private static final int INTERVAL_DURATION_MINUS_ONE = INTERVAL_DURATION - 1;
	private TestTimeProvider timeProvider = new TestTimeProvider();
	private ClientSideCallContext clientSideCallContext = new ClientSideCallContext(SERVICE_ID_0, "someMethod", Collections.emptyList());
	private ScheduledExecutorService dummyScheduledExecutorService = mock(ScheduledExecutorService.class);
	private GenericRouterConfiguration routerConfiguration = new GenericRouterConfiguration();
	private Logger logger = mock(Logger.class);
	private ErrorsPerIntervalBlacklistingStrategy strategy = new ErrorsPerIntervalBlacklistingStrategy(dummyScheduledExecutorService, timeProvider, logger);

	@Before
	public void setUp() throws Exception {
		timeProvider.setCurrentMillis(0);
		givenConfigWith("epbs_2RequiredNumberOfIntervalsWithErrors_2ErrorsPerIntervalThreshold");
		assertFalse("ServiceId should not be blacklisted before failure call", strategy.isBlacklisted(SERVICE_ID_0));
	}

	@Test
	public void isBlacklisted_IfErrorsPerIntervalExceedsThreshold_ForOneRequiredInterval_WithinInterval() {
		givenConfigWith("epbs_1RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold");

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);

		thenServiceIsBlacklisted(SERVICE_ID_0);

	}

	@Test
	public void isBlacklistedIfErrorsPerIntervalExceedsThreshold_ForOneRequiredInterval_OnIntervalBorder() {
		givenConfigWith("epbs_1RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold");

		whenNotifiyCallFailed(SERVICE_ID_0, 1);

		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isBlacklistedIfErrorsPerIntervalExceedsThreshold_InFirstInterval_AndCurrentInterval() {
		givenConfigWith("epbs_2RequiredNumberOfIntervalsWithErrors_1ErrorsPerIntervalThreshold");

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);

		whenNotifiyCallFailed(SERVICE_ID_0, 2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);

		thenServiceIsBlacklisted(SERVICE_ID_0);
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
	}

	@Test
	public void isNotBlackListedIfAllThresholdsAreSetToZero() {
		givenConfigWith("epbs_0RequiredNumberOfIntervalsWithErrors_0ErrorsPerIntervalThreshold");
		whenNotifiyCallFailed(SERVICE_ID_0, 0);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
	}

	@Test
	public void isNotBlacklistedIfStrategyConfigurationIsFaulty() {
		givenConfigWith("epbs_Miuns1RequiredNumberOfIntervalsWithErrors_Minus1ErrorsPerIntervalThreshold");

		whenNotifiyCallFailed(SERVICE_ID_0, 0);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
		thenConfigurationErrorIsLogged("Invalid configuration epbs_Miuns1RequiredNumberOfIntervalsWithErrors_Minus1ErrorsPerIntervalThreshold ErrorsPerIntervalBlacklistingStrategyConfig{errorsPerIntervalThreshold=-1, intervalDurationInSeconds=-1, requiredNumberOfIntervalsWithErrors=-1}");
	}


	@Test
	public void isNotBlacklistedIfStrategyIfConfigurationIsNotExistend() {
		givenConfigWith("notExists");

		whenNotifiyCallFailed(SERVICE_ID_0, 0);
		thenServiceIsNotBlacklisted(SERVICE_ID_0);
		thenConfigurationErrorIsLogged("Could not load configuration notExists");
	}

	private void thenConfigurationErrorIsLogged(String message) {
		verify(logger).warn(message);
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
		assertTrue(serviceID + "  should be blacklisted", strategy.isBlacklisted(serviceID));
	}

	private void thenServiceIsNotBlacklisted(String serviceID) {
		assertFalse(serviceID + " should not be blacklisted", strategy.isBlacklisted(serviceID));
	}

	private void whenJumpInTimePlusSeconds(int seconds) {
		timeProvider.increaseBySeconds(seconds);
		strategy.timerTick();
	}

	private void whenNotifiyCallFailed(String serviceID, int times) {
		for (int i = 0; i < times; i++) {
			strategy.notifyCallFailed(clientSideCallContext);
		}
	}

	private void givenConfigWith(String configName) {
		routerConfiguration.setBlacklistStrategyConfigurationName(configName);
		strategy.setConfiguration(routerConfiguration);
	}
}