package org.distributeme.core;

import java.util.List;
import java.util.Map;


/**
 * Interface to allow multiple registry implementations and appropriate connectors.
 */
public interface RegistryConnector {

	String describeRegistry();

	/**
	 * Bind a service at the registry.
	 * @param service
	 * @return
	 */
	boolean bind(ServiceDescriptor service);

	boolean notifyBind(Location location, ServiceDescriptor descriptor);

	boolean notifyUnbind(Location location, ServiceDescriptor descriptor);

	/**
	 * Unbind a service from the registry.
	 * @param service
	 * @return
	 */
	boolean unbind(ServiceDescriptor service);

	/**
	 * Resolve a service in the registry.
	 * @param toResolve
	 * @param loc
	 * @return
	 */
	ServiceDescriptor resolve(ServiceDescriptor toResolve, Location loc);

	/**
	 * Set tagable system properties (only Consul).
	 * @param tagableSystemProperties
	 */
	void setTagableSystemProperties(Map<String, String> tagableSystemProperties);

	/**
	 * Set custom tag provider class list (only Consul).
	 * @param customTagProviderClassList
	 */
	void setCustomTagProviderClassList(List<String> customTagProviderClassList);
}
