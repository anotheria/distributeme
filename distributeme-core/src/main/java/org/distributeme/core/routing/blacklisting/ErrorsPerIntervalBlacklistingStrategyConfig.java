package org.distributeme.core.routing.blacklisting;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;


/**
 * Created by rboehling on 2/23/17.
 */
@ConfigureMe (allfields = true)
public class ErrorsPerIntervalBlacklistingStrategyConfig {

	/**
	 * Number of errors that must occur in one time interval
	 * before a service instance is blacklisted.
	 */
	private int errorsPerIntervalThreshold;
	/**
	 * Interval duration in which error are counted
	 */
	private int intervalDurationInSeconds;
	/**
	 * Amount of successive intervals in which the errorsPerIntervalThreshold must be reached
	 * in order to blacklist the service instance.
	 */
	private int requiredNumberOfIntervalsWithErrors;

	public int getErrorsPerIntervalThreshold() {
		return errorsPerIntervalThreshold;
	}

	public int getIntervalDurationInSeconds() {
		return intervalDurationInSeconds;
	}

	public int getRequiredNumberOfIntervalsWithErrors() {
		return requiredNumberOfIntervalsWithErrors;
	}

	public void setErrorsPerIntervalThreshold(int errorsPerIntervalThreshold) {
		this.errorsPerIntervalThreshold = errorsPerIntervalThreshold;
	}

	public void setIntervalDurationInSeconds(int intervalDurationInSeconds) {
		this.intervalDurationInSeconds = intervalDurationInSeconds;
	}

	public void setRequiredNumberOfIntervalsWithErrors(int requiredNumberOfIntervalsWithErrors) {
		this.requiredNumberOfIntervalsWithErrors = requiredNumberOfIntervalsWithErrors;
	}

	@Override
	public String toString() {
		return "ErrorsPerIntervalBlacklistingStrategyConfig{" +
				"errorsPerIntervalThreshold=" + errorsPerIntervalThreshold +
				", intervalDurationInSeconds=" + intervalDurationInSeconds +
				", requiredNumberOfIntervalsWithErrors=" + requiredNumberOfIntervalsWithErrors +
				'}';
	}
}
