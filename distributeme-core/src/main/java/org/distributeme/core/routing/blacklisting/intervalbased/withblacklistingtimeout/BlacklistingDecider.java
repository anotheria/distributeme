package org.distributeme.core.routing.blacklisting.intervalbased.withblacklistingtimeout;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.distributeme.core.routing.blacklisting.BlacklistDecision;


/**
 * Created by rboehling on 2/24/17.
 */
class BlacklistingDecider {

	private AtomicInteger currentIntervalErrorCounter = new AtomicInteger();
	private AtomicInteger nonStopIntervalsWithErrorsCounter = new AtomicInteger();
	private final ErrorsPerIntervalWithBlacklistingTimeConfig config;
	private AtomicLong currentTimeMillies = new AtomicLong();
	private AtomicLong intervalStartTimeInMillies = new AtomicLong();
	private AtomicLong blackListingStartTimeMillies = new AtomicLong();
	private volatile BlacklistDecision previousDecision = BlacklistDecision.NOT_BLACKLISTED;

	BlacklistingDecider(ErrorsPerIntervalWithBlacklistingTimeConfig config) {
		this.config = config;
	}

	BlacklistDecision getDecision() {
		BlacklistDecision currentDecision;
		if(previousDecision.isBlacklisted()){
			currentDecision = decideForAlreadyBlacklistedService();
		} else {
			currentDecision = decideForNotBlacklistedService();
		}
		if (currentDecision == BlacklistDecision.GOT_BLACKLISTED) {
			blackListingStartTimeMillies.set(currentTimeMillies.longValue());
		}
		previousDecision = currentDecision;
		return currentDecision;
	}

	private BlacklistDecision decideForNotBlacklistedService() {
		if (mustBeBlacklisted()) {
			return previousDecision.isBlacklisted() ? BlacklistDecision.IS_BLACKLISTED : BlacklistDecision.GOT_BLACKLISTED;
		} else {
			return previousDecision.isBlacklisted() ? BlacklistDecision.UNBLACKLISTED : BlacklistDecision.NOT_BLACKLISTED;
		}
	}

	private boolean mustBeBlacklisted() {
		return reachedRequiredNumberOfIntervalsWithErrors() || reachRequiredNumberOfIntervalsWithErrorsWithCurrentInterval();
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

	private BlacklistDecision decideForAlreadyBlacklistedService() {
		if(shouldServiceBeWhiteListedAgain()) {
			return BlacklistDecision.UNBLACKLISTED;
		} else {
			return BlacklistDecision.IS_BLACKLISTED;
		}
	}

	private boolean shouldServiceBeWhiteListedAgain() {
		return currentTimeMillies.get() - blackListingStartTimeMillies.get() > config.getBlacklistTime() * 1000;
	}

	private void setNonStopIntervalsWithErrorCounter() {
		if (reachedThresholdWithinCurrentInterval()) {
			nonStopIntervalsWithErrorsCounter.incrementAndGet();
		} else {
			nonStopIntervalsWithErrorsCounter.set(0);
		}
	}

	void notifyCallFailed() {
		currentIntervalErrorCounter.incrementAndGet();
	}

	void timerTick(long currentTimeMillis) {
		this.currentTimeMillies.set(currentTimeMillis);
		if (isIntervalDurationReached(currentTimeMillis)) {
			intervalStartTimeInMillies.set(currentTimeMillis);
			setNonStopIntervalsWithErrorCounter();
			currentIntervalErrorCounter.set(0);
		}
	}

	private boolean isIntervalDurationReached(long currentTimeMillis) {
		return ((currentTimeMillis - intervalStartTimeInMillies.get()) / 1000) >= config.getIntervalDurationInSeconds();
	}


}