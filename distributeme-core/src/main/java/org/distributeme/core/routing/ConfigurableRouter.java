package org.distributeme.core.routing;

/**
 * A router that implements this interface will be mostly configured by a configuration file.
 * The configuration itself can differ.
 *
 * @author lrosenberg
 * @since 20.02.15 23:05
 * @version $Id: $Id
 */
public interface ConfigurableRouter extends Router{
	/**
	 * Sets the configuration name and the service id.
	 *
	 * @param serviceId a {@link java.lang.String} object.
	 * @param configurationName a {@link java.lang.String} object.
	 */
	void setConfigurationName(String serviceId, String configurationName);
}
