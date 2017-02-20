package org.distributeme.core.qos;

import org.configureme.annotations.ConfigureMe;

/**
 * Configuration object for quality of service component.
 *
 * @author lrosenberg
 * @since 22.02.15 23:32
 * @version $Id: $Id
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

	/**
	 * <p>Getter for the field <code>timeoutBeforeBlackList</code>.</p>
	 *
	 * @return a long.
	 */
	public long getTimeoutBeforeBlackList() {
		return timeoutBeforeBlackList;
	}

	/**
	 * <p>Setter for the field <code>timeoutBeforeBlackList</code>.</p>
	 *
	 * @param timeoutBeforeBlackList a long.
	 */
	public void setTimeoutBeforeBlackList(long timeoutBeforeBlackList) {
		this.timeoutBeforeBlackList = timeoutBeforeBlackList;
	}

	/**
	 * <p>Getter for the field <code>blacklistDuration</code>.</p>
	 *
	 * @return a long.
	 */
	public long getBlacklistDuration() {
		return blacklistDuration;
	}

	/**
	 * <p>Setter for the field <code>blacklistDuration</code>.</p>
	 *
	 * @param blacklistDuration a long.
	 */
	public void setBlacklistDuration(long blacklistDuration) {
		this.blacklistDuration = blacklistDuration;
	}
}
