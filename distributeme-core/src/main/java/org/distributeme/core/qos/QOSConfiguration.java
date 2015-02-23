package org.distributeme.core.qos;

import org.configureme.annotations.ConfigureMe;

/**
 * Configuration object for quality of service component.
 *
 * @author lrosenberg
 * @since 22.02.15 23:32
 */
@ConfigureMe(allfields = true, name="distributeme-qos")
public class QOSConfiguration {
	/**
	 * Timeout in milliseconds before unanswered request will lead to blacklisting of a service.
	 */
	private long timeoutBeforeBlackList = 2000;
	/**
	 * Duration for how long a service entry will be blacklisted.
	 */
	private long blacklistDuration = 30000;

	public long getTimeoutBeforeBlackList() {
		return timeoutBeforeBlackList;
	}

	public void setTimeoutBeforeBlackList(long timeoutBeforeBlackList) {
		this.timeoutBeforeBlackList = timeoutBeforeBlackList;
	}

	public long getBlacklistDuration() {
		return blacklistDuration;
	}

	public void setBlacklistDuration(long blacklistDuration) {
		this.blacklistDuration = blacklistDuration;
	}
}
