package org.distributeme.core;

/**
 * Interface which defines host and port. Part of RegistryUtil.
 *
 * @author Oliver Hoogvliet
 * @version $Id: $Id
 */
public interface Location {
	/**
	 * Returns the host of the registry.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getHost();
	/**
	 * Returns the port of the registry.
	 *
	 * @return a int.
	 */
	int getPort();


	/**
	 * <p>getProtocol.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getProtocol();

	/**
	 * <p>getContext.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getContext();

}
