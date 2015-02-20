package org.distributeme.core.routing;

import org.configureme.annotations.ConfigureMe;

/**
 * Generic configuration file that will work with most routers, even many routers most probably won't support all of the options.
 *
 * @author lrosenberg
 * @since 20.02.15 23:07
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

	public long getBlacklistTime() {
		return blacklistTime;
	}

	public void setBlacklistTime(long blacklistTime) {
		this.blacklistTime = blacklistTime;
	}

	public int getNumberOfInstances() {
		return numberOfInstances;
	}

	public void setNumberOfInstances(int numberOfInstances) {
		this.numberOfInstances = numberOfInstances;
	}

	@Override
	public String toString(){
		return "NumberOfInstances: "+getNumberOfInstances()+", blacklistTime: "+getBlacklistTime();
	}
}
