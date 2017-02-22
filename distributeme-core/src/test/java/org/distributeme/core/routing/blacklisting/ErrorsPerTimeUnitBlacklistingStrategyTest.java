package org.distributeme.core.routing.blacklisting;

import java.util.Collections;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.util.TestTimeProvider;
import org.distributeme.core.util.TimeProvider;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by rboehling on 2/22/17.
 */
public class ErrorsPerTimeUnitBlacklistingStrategyTest {

	private TestTimeProvider timeProvider = new TestTimeProvider();
	private ErrorsPerTimeUnitBlacklistingStrategy strategy = new ErrorsPerTimeUnitBlacklistingStrategy();
	private static final String SERVICE_ID = "serviceId";
	private ClientSideCallContext clientSideCallContext = new ClientSideCallContext(SERVICE_ID, "someMethod", Collections.emptyList());

	@Before
	public void setUp() throws Exception {
		strategy.setTimeProvider(timeProvider);
		strategy.setPeriodDurationInSeconds(10);

	}

	@Test
	public void isBlacklistedIfErrorsPerTimeUnitExceedsLimit() {
		boolean blacklisted = strategy.isBlacklisted(SERVICE_ID);

		assertFalse("ServiceId should not be blacklisted before failure call", blacklisted);

		strategy.setErrorsPerPeriodThreshold(1);
		timeProvider.setCurrentMillis(0);
		strategy.notifyCallFailed(clientSideCallContext);
		strategy.notifyCallFailed(clientSideCallContext);
		timeProvider.setCurrentMillis(9000);
		blacklisted = strategy.isBlacklisted(SERVICE_ID);

		assertTrue("ServiceId should be blacklisted", blacklisted);
	}

	@Test
	public void isBlacklistedIfErrorsPerTimeUnitReachesLimit() {
		boolean blacklisted = strategy.isBlacklisted(SERVICE_ID);

		assertFalse("ServiceId should not be blacklisted before failure call", blacklisted);


		strategy.setErrorsPerPeriodThreshold(1);
		timeProvider.setCurrentMillis(0);
		strategy.notifyCallFailed(clientSideCallContext);
		timeProvider.setCurrentMillis(9000);
		blacklisted = strategy.isBlacklisted(SERVICE_ID);

		assertTrue("ServiceId should be blacklisted", blacklisted);
	}

	@Test
	public void isNotBlacklistedIfThresholdIsNotReached() {
		boolean blacklisted = strategy.isBlacklisted(SERVICE_ID);

		assertFalse("ServiceId should not be blacklisted before failure call", blacklisted);


		strategy.setErrorsPerPeriodThreshold(2);
		strategy.notifyCallFailed(clientSideCallContext);

		blacklisted = strategy.isBlacklisted(SERVICE_ID);

		assertFalse("ServiceId should be blacklisted, because threshold is not reached", blacklisted);
	}

	@Test
	public void isNotBlacklistedIfErrorsPerTimeUnitDoNotExceedLimit() {
		boolean blacklisted = strategy.isBlacklisted(SERVICE_ID);

		assertFalse("ServiceId should not be blacklisted before failure call", blacklisted);

		strategy.setErrorsPerPeriodThreshold(1);
		timeProvider.setCurrentMillis(0);
		strategy.notifyCallFailed(clientSideCallContext);
		strategy.notifyCallFailed(clientSideCallContext);
		timeProvider.setCurrentMillis(11000);
		assertFalse("ServiceId should not be blacklisted, because not enough errors in one period", strategy.isBlacklisted(SERVICE_ID));

		strategy.notifyCallFailed(clientSideCallContext);
		strategy.notifyCallFailed(clientSideCallContext);
		timeProvider.setCurrentMillis(15000);

		assertTrue("ServiceId should be blacklisted, because too many errors in one period", strategy.isBlacklisted(SERVICE_ID));
	}

	@Ignore
	@Test
	public void isNotBlacklistedIfErrorsPerTimeUnitDoNotExceedLimitWithinTwoLastPeriods() {
		boolean blacklisted = strategy.isBlacklisted(SERVICE_ID);

		assertFalse("ServiceId should not be blacklisted before failure call", blacklisted);

		strategy.setErrorsPerPeriodThreshold(1);
		timeProvider.setCurrentMillis(0);
		strategy.notifyCallFailed(clientSideCallContext);
		strategy.notifyCallFailed(clientSideCallContext);
		timeProvider.setCurrentMillis(11000);
		assertFalse("ServiceId should not be blacklisted, because not enough errors in first period", strategy.isBlacklisted(SERVICE_ID));

		strategy.notifyCallFailed(clientSideCallContext);
		strategy.notifyCallFailed(clientSideCallContext);
		timeProvider.setCurrentMillis(20000);

		assertFalse("ServiceId should not be blacklisted, because not enough errors in second period", strategy.isBlacklisted(SERVICE_ID));
	}

}