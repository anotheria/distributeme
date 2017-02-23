package org.distributeme.core.routing.blacklisting;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;


/**
 * Created by rboehling on 2/23/17.
 */
@ConfigureMe (allfields = true)
public class ErrorsPerIntervalBlacklistingStrategyConfig {


	private int errorsPerIntervalThreshold;
	private int intervalDurationInSeconds;
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
}
