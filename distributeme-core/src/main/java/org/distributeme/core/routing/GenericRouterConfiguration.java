package org.distributeme.core.routing;

import org.configureme.annotations.ConfigureMe;

/**
 * Generic configuration file that will work with most routers, even many routers most probably won't support all of the options.
 *
 * @author lrosenberg
 * @since 20.02.15 23:07
 * @version $Id: $Id
 */
@ConfigureMe (allfields = true)
public class GenericRouterConfiguration {
	/**
	 * Number of instances of this service.
	 */
	private int numberOfInstances;
	/**
	 * Amount of time in milliseconds a service instance will be blacklisted.
	 */
	private long blacklistTime;

	/**
	 * <p>Getter for the field <code>blacklistTime</code>.</p>
	 *
	 * @return a long.
	 */
	public long getBlacklistTime() {
		return blacklistTime;
	}

	/**
	 * <p>Setter for the field <code>blacklistTime</code>.</p>
	 *
	 * @param blacklistTime a long.
	 */
	public void setBlacklistTime(long blacklistTime) {
		this.blacklistTime = blacklistTime;
	}

	/**
	 * <p>Getter for the field <code>numberOfInstances</code>.</p>
	 *
	 * @return a int.
	 */
	public int getNumberOfInstances() {
		return numberOfInstances;
	}

	/**
	 * <p>Setter for the field <code>numberOfInstances</code>.</p>
	 *
	 * @param numberOfInstances a int.
	 */
	public void setNumberOfInstances(int numberOfInstances) {
		this.numberOfInstances = numberOfInstances;
	}

	/** {@inheritDoc} */
	@Override
	public String toString(){
		return "NumberOfInstances: "+getNumberOfInstances()+", blacklistTime: "+getBlacklistTime();
	}
}
