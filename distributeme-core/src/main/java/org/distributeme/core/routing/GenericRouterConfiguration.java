package org.distributeme.core.routing;

import java.util.ArrayList;
import java.util.List;

import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.AfterInitialConfiguration;
import org.configureme.annotations.AfterReConfiguration;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Generic configuration file that will work with most routers, even many routers most probably won't support all of the options.
 *
 * @author lrosenberg
 * @since 20.02.15 23:07
 * @version $Id: $Id
 */
@ConfigureMe (allfields = true)
public class GenericRouterConfiguration {


	private static Logger logger = LoggerFactory.getLogger(GenericRouterConfiguration.class);
	/**
	 * Number of instances of this service.
	 */
	private int numberOfInstances;
	/**
	 * Amount of time in milliseconds a service instance will be blacklisted.
	 */
	private long blacklistTime;

	/**
	 * If true and all instances are blacklisted, then blacklisting for all instances will be cancelled.
	 */
	private boolean overrideBlacklistIfAllBlacklisted;

	/**
	 * Fully qualified class name of blacklist strategy.
	 *  If not set org.distributeme.core.routing.blacklisting.DefaultBlacklistingStrategy will be used
	 */
	private String blacklistStrategyClazz;

	/**
	 * Name of configureme configuration for blacklist strategy. Json file sufffix is not part of the name.
	 */
	private String blacklistStrategyConfigurationName;

	/**
	 * Observer wich can react onf configuration changes.
	 */
	private List<RouterConfigurationObserver> routerConfigurationObservers = new ArrayList<>();

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



	public boolean isOverrideBlacklistIfAllBlacklisted() {
		return overrideBlacklistIfAllBlacklisted;
	}

	public void setOverrideBlacklistIfAllBlacklisted(boolean overrideBlacklistIfAllBlacklisted) {
		this.overrideBlacklistIfAllBlacklisted = overrideBlacklistIfAllBlacklisted;
	}

	public String getBlacklistStrategyClazz() {
		return blacklistStrategyClazz;
	}

	public void setBlacklistStrategyClazz(String blacklistStrategyClazz) {
		this.blacklistStrategyClazz = blacklistStrategyClazz;
	}

	public void setBlacklistStrategyConfigurationName(String blacklistStrategyConfigurationName) {
		this.blacklistStrategyConfigurationName = blacklistStrategyConfigurationName;
	}

	public String getBlacklistStrategyConfigurationName() {
		return blacklistStrategyConfigurationName;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "GenericRouterConfiguration{" +
				"numberOfInstances=" + numberOfInstances +
				", blacklistTime=" + blacklistTime +
				", overrideBlacklistIfAllBlacklisted=" + overrideBlacklistIfAllBlacklisted +
				", blacklistStrategyClazz='" + blacklistStrategyClazz + '\'' +
				", blacklistStrategyConfigurationName='" + blacklistStrategyConfigurationName + '\'' +
				", routerConfigurationObservers=" + routerConfigurationObservers +
				'}';
	}


	public void addRouterConfigurationObserver(RouterConfigurationObserver routerConfigurationObserver) {
		routerConfigurationObservers.add(routerConfigurationObserver);
	}

	@AfterInitialConfiguration
	@SuppressWarnings("checkstyle:IllegalCatchCheck")
	public void afterInitialConfiguration() {
		for(RouterConfigurationObserver observer: routerConfigurationObservers) {
			try {
				observer.routerConfigurationInitialChange(this);
			} catch (Exception e) {
				logger.warn("Could not call routerConfigurationInitialChange in " + observer, e);
			}
		}
	}

	@AfterReConfiguration
	@SuppressWarnings("checkstyle:IllegalCatchCheck")
	public void afterReConfiguration() {
		for(RouterConfigurationObserver observer: routerConfigurationObservers) {
			try {
				observer.routerConfigurationFollowupChange(this);
			} catch (Exception e) {
				logger.warn("Could not call routerConfigurationFollowupChange in " + observer, e);
			}
		}
	}

	@AfterConfiguration
	@SuppressWarnings("checkstyle:IllegalCatchCheck")
	public void afterConfiguration() {
		for(RouterConfigurationObserver observer: routerConfigurationObservers) {
			try {
				observer.routerConfigurationChange(this);
			} catch (Exception e) {
				logger.warn("Could not call routerConfigurationChange in " + observer, e);
			}
		}
	}


}
