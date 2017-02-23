package org.distributeme.core.routing.blacklisting;

import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.util.TestTimeProvider;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


/**
 * Created by rboehling on 2/22/17.
 */
public class ErrorsPerIntervalBlacklistingStrategyTest {

	private static final int INTERVAL_DURATION = 10;
	private static final int INTERVAL = INTERVAL_DURATION + 1;
	private static final int INTERVAL_DURATION_MINUS_ONE = INTERVAL_DURATION - 1;
	private TestTimeProvider timeProvider = new TestTimeProvider();
	private ErrorsPerIntervalBlacklistingStrategy strategy = new ErrorsPerIntervalBlacklistingStrategy();
	private static final String SERVICE_ID = "serviceId";
	private ClientSideCallContext clientSideCallContext = new ClientSideCallContext(SERVICE_ID, "someMethod", Collections.emptyList());
	private ScheduledExecutorService dummyScheduledExecutorService = mock(ScheduledExecutorService.class);

	@Before
	public void setUp() throws Exception {
		strategy.setTimeProvider(timeProvider);
		strategy.setIntervalDurationInSeconds(INTERVAL_DURATION);
		strategy.setErrorsPerIntervalThreshold(10);
		strategy.setScheduledExecutorService(dummyScheduledExecutorService);
		timeProvider.setCurrentMillis(0);
		assertFalse("ServiceId should not be blacklisted before failure call", strategy.isBlacklisted(SERVICE_ID));
	}

	@Test
	public void isBlacklisted_IfErrorsPerIntervalExceedsThreshold_ForOneRequiredInterval_WithinInterval() {
		strategy.setRequiredNumberOfIntervalsWithErrors(1);
		strategy.setErrorsPerIntervalThreshold(1);

		whenNotifiyCallFailed(2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);

		thenServiceIsBlacklisted();

	}

	@Test
	public void isBlacklistedIfErrorsPerIntervalExceedsThreshold_ForOneRequiredInterval_OnIntervalBorder() {
		strategy.setRequiredNumberOfIntervalsWithErrors(1);
		strategy.setErrorsPerIntervalThreshold(1);

		whenNotifiyCallFailed(1);

		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsBlacklisted();
	}

	@Test
	public void isBlacklistedIfErrorsPerIntervalExceedsThreshold_InFirstInterval_AndCurrentInterval() {
		strategy.setRequiredNumberOfIntervalsWithErrors(2);
		strategy.setErrorsPerIntervalThreshold(1);

		whenNotifiyCallFailed(2);
		whenJumpInTimePlusSeconds(INTERVAL);

		whenNotifiyCallFailed(2);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);

		thenServiceIsBlacklisted();
	}

	@Test
	public void isNotBlacklistedIfErrorsExceedThreshold_InFirstInterval_ButZeroInCurrent() {
		strategy.setRequiredNumberOfIntervalsWithErrors(2);
		strategy.setErrorsPerIntervalThreshold(1);

		whenNotifiyCallFailed(2);
		whenJumpInTimePlusSeconds(INTERVAL);

		thenServiceIsNotBlacklisted();
	}

	@Test
	public void isNotBlacklistedIfThresholdIsNotReached() {
		strategy.setRequiredNumberOfIntervalsWithErrors(2);
		strategy.setErrorsPerIntervalThreshold(2);

		whenNotifiyCallFailed(1);

		thenServiceIsNotBlacklisted();
	}

	@Test
	public void isNotBlacklistedInFirstIntervalButInSecond() {
		strategy.setRequiredNumberOfIntervalsWithErrors(2);
		strategy.setErrorsPerIntervalThreshold(1);

		whenNotifiyCallFailed(1);
		whenJumpInTimePlusSeconds(INTERVAL);
		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(1);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);

		thenServiceIsBlacklisted();
	}

	@Test
	public void isNotBlacklistedIfErrorsPerIntervalDoNotExceedThreshold_WithinTwoLastIntervals() {
		strategy.setRequiredNumberOfIntervalsWithErrors(2);
		strategy.setErrorsPerIntervalThreshold(2);

		whenNotifiyCallFailed(2);
		whenJumpInTimePlusSeconds(INTERVAL);

		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(1);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);

		thenServiceIsNotBlacklisted();
	}

	@Test
	public void isNotBlacklisted_IfFirstAndThridIntervalHaveErrors_ButSecondIntervalHasNot() {
		strategy.setRequiredNumberOfIntervalsWithErrors(2);
		strategy.setErrorsPerIntervalThreshold(3);

		whenNotifiyCallFailed(1);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(0);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(1);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted();
	}

	@Test
	public void isNotBlacklisted_IfFirstAndSecondIntervalHaveErrorsButThirdHasNot() {
		strategy.setErrorsPerIntervalThreshold(3);
		strategy.setRequiredNumberOfIntervalsWithErrors(3);

		whenNotifiyCallFailed(3);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(4);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(1);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);
		thenServiceIsNotBlacklisted();
	}

	@Ignore
	@Test
	public void isBlacklisted_IfThreeRequiredIntervalsHaveErrors() {
		strategy.setErrorsPerIntervalThreshold(3);
		strategy.setRequiredNumberOfIntervalsWithErrors(3);

		whenNotifiyCallFailed(3);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(4);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION);
		thenServiceIsNotBlacklisted();

		whenNotifiyCallFailed(3);
		whenJumpInTimePlusSeconds(INTERVAL_DURATION_MINUS_ONE);
		thenServiceIsBlacklisted();
	}

	@Test
	public void isBlackListedIfAllThresholdsAreSetToZero() {
		strategy.setErrorsPerIntervalThreshold(0);
		strategy.setRequiredNumberOfIntervalsWithErrors(0);
		strategy.setIntervalDurationInSeconds(0);

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
		for (int i = 0; i < times; i++) {
			strategy.notifyCallFailed(clientSideCallContext);
		}
	}

}