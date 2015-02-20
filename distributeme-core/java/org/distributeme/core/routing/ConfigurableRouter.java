package org.distributeme.core.routing;

/**
 * A router that implements this interface will be mostly configured by a configuration file.
 * The configuration itself can differ.
 *
 * @author lrosenberg
 * @since 20.02.15 23:05
 */
public interface ConfigurableRouter extends Router{
	/**
	 * Sets the configuration name.
	 */
	void setConfigurationName(String configurationName);
}
