package org.distributeme.core.routing.blacklisting;

import java.util.Collections;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.util.TestTimeProvider;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by rboehling on 2/22/17.
 */
public class ErrorsPerPeriodBlacklistingStrategyTest {

	private static final int PERIOD_DURATION = 10;
	private static final int PERIOD_DURATION_PLUS_ONE = PERIOD_DURATION + 1;
	private static final int PERIOD_DURATION_MINUS_ONE = PERIOD_DURATION - 1;
	private TestTimeProvider timeProvider = new TestTimeProvider();
	private ErrorsPerPeriodBlacklistingStrategy strategy = new ErrorsPerPeriodBlacklistingStrategy();
	private static final String SERVICE_ID = "serviceId";
	private ClientSideCallContext clientSideCallContext = new ClientSideCallContext(SERVICE_ID, "someMethod", Collections.emptyList());


	@Before
	public void setUp() throws Exception {
		strategy.setTimeProvider(timeProvider);
		strategy.setPeriodDurationInSeconds(PERIOD_DURATION);
		strategy.setErrorsPerPeriodThreshold(10);
		timeProvider.setCurrentMillis(0);
		assertFalse("ServiceId should not be blacklisted before failure call", strategy.isBlacklisted(SERVICE_ID));
	}

	@Test
	public void isBlacklisted_IfErrorsPerPeriodExceedsThreshold_ForOneRequiredPeriod_WithinPeriod() {
		strategy.setRequiredNumberOfPeriodsWithErrors(1);
		strategy.setErrorsPerPeriodThreshold(1);

		whenNotifiyCallFailed(2);
		whenJumpInTimePlusSeconds(PERIOD_DURATION_MINUS_ONE);

		thenServiceIsBlacklisted();

	}

	@Test
	public void isBlacklistedIfErrorsPerPeriodExceedsThreshold_ForOneRequiredPeriod_OnPeriodBorder() {
		strategy.setRequiredNumberOfPeriodsWithErrors(1);
		strategy.setErrorsPerPeriodThreshold(1);

		whenNotifiyCallFailed(1);

		whenJumpInTimePlusSeconds(PERIOD_DURATION);
		thenServiceIsBlacklisted();
	}

	@Test
	public void isBlacklistedIfErrorsPerPeriodExceedsThreshold_InFirstPeriod_AndCurrentPeriod() {
		strategy.setRequiredNumberOfPeriodsWithErrors(2);
		strategy.setErrorsPerPeriodThreshold(1);

		whenNotifiyCallFailed(2);
		whenJumpInTimePlusSeconds(PERIOD_DURATION_PLUS_ONE);

		whenNotifiyCallFailed(2);
		whenJumpInTimePlusSeconds(PERIOD_DURATION_MINUS_ONE);

		thenServiceIsBlacklisted();
	}

	@Test
	public void isNotBlacklistedIfErrorsExceedThreshold_InFirstPeriod_ButZeroInCurrent() {
		strategy.setRequiredNumberOfPeriodsWithErrors(2);
		strategy.setErrorsPerPeriodThreshold(1);

		whenNotifiyCallFailed(2);
		whenJumpInTimePlusSeconds(PERIOD_DURATION_PLUS_ONE);

		thenServiceIsNotBlacklisted();
	}

	@Test
	public void isNotBlacklistedIfThresholdIsNotReached() {
		strategy.setRequiredNumberOfPeriodsWithErrors(2);
		strategy.setErrorsPerPeriodThreshold(2);

		whenNotifiyCallFailed(1);

		thenServiceIsNotBlacklisted();
	}

	@Test
	public void isNotBlacklistedInFirstPeriodButInSecond() {
		strategy.setRequiredNumberOfPeriodsWithErrors(2);
		strategy.setErrorsPerPeriodThreshold(1);

		whenNotifiyCallFailed(1);
		whenJumpInTimePlusSeconds(PERIOD_DURATION_PLUS_ONE);
		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(1);
		whenJumpInTimePlusSeconds(PERIOD_DURATION_MINUS_ONE);

		thenServiceIsBlacklisted();
	}

	@Test
	public void isNotBlacklistedIfErrorsPerPeriodDoNotExceedThreshold_WithinTwoLastPeriods() {
		strategy.setRequiredNumberOfPeriodsWithErrors(2);
		strategy.setErrorsPerPeriodThreshold(2);

		whenNotifiyCallFailed(2);
		whenJumpInTimePlusSeconds(PERIOD_DURATION_PLUS_ONE);

		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(1);
		whenJumpInTimePlusSeconds(PERIOD_DURATION);

		thenServiceIsNotBlacklisted();
	}

	@Test
	public void isNotBlacklisted_IfFirstAndThridPeriodHaveErrors_ButSecondPeriodHasNot() {
		strategy.setRequiredNumberOfPeriodsWithErrors(2);
		strategy.setErrorsPerPeriodThreshold(3);

		whenNotifiyCallFailed(1);
		whenJumpInTimePlusSeconds(PERIOD_DURATION);
		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(0);
		whenJumpInTimePlusSeconds(PERIOD_DURATION);
		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(1);
		whenJumpInTimePlusSeconds(PERIOD_DURATION);
		thenServiceIsNotBlacklisted();
	}

	@Test
	public void isNotBlacklisted_IfFirstAndSecondPeriodHaveErrorsButThirdHasNot() {
		strategy.setErrorsPerPeriodThreshold(3);
		strategy.setRequiredNumberOfPeriodsWithErrors(3);

		whenNotifiyCallFailed(3);
		whenJumpInTimePlusSeconds(PERIOD_DURATION);
		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(4);
		whenJumpInTimePlusSeconds(PERIOD_DURATION);
		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(1);
		whenJumpInTimePlusSeconds(PERIOD_DURATION_MINUS_ONE);
		thenServiceIsNotBlacklisted();
	}

	@Ignore
	@Test
	public void isBlacklisted_IfThreeRequiredIntervalsHaveErrors() {
		strategy.setErrorsPerPeriodThreshold(3);
		strategy.setRequiredNumberOfPeriodsWithErrors(3);

		whenNotifiyCallFailed(3);
		whenJumpInTimePlusSeconds(PERIOD_DURATION);
		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(4);
		whenJumpInTimePlusSeconds(PERIOD_DURATION);
		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(3);
		whenJumpInTimePlusSeconds(PERIOD_DURATION_MINUS_ONE);
		thenServiceIsBlacklisted();
	}

	@Test
	public void isBlackListedIfAllThresholdsAreSetToZero() {
		strategy.setErrorsPerPeriodThreshold(0);
		strategy.setRequiredNumberOfPeriodsWithErrors(0);
		strategy.setPeriodDurationInSeconds(0);

		whenNotifiyCallFailed(0);
	}

	private void thenServiceIsBlacklisted() {
		assertTrue("ServiceId should be blacklisted", strategy.isBlacklisted(SERVICE_ID));
	}

	private void thenServiceIsNotBlacklisted() {
		assertFalse("ServiceId should not be blacklisted", strategy.isBlacklisted(SERVICE_ID));
	}

	private void whenJumpInTimePlusSeconds(int seconds) {
		timeProvider.increaseBySeconds(seconds);
		strategy.timerTick();
	}

	private void whenNotifiyCallFailed(int times) {
		for(int i = 0; i < times; i++) {
			strategy.notifyCallFailed(clientSideCallContext);
		}
	}

}