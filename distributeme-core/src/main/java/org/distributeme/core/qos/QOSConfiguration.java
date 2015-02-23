package org.distributeme.core.qos;

import org.configureme.annotations.ConfigureMe;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 22.02.15 23:32
 */
@ConfigureMe(allfields = true, name="distributeme-qos")
public class QOSConfiguration {
	private long timeoutBeforeBlackList = 2000;
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
