package org.distributeme.core;

/**
 * Interface which defines host and port. Part of RegistryUtil. 
 * @author Oliver Hoogvliet
 *
 */
public interface Location {
	/**
	 * Returns the host of the registry.
	 * @return
	 */
	String getHost();
	/**
	 * Returns the port of the registry.
	 * @return
	 */
	int getPort();
}
