package org.distributeme.core.conventions;
/**
 * Defines operations supported by the EventServiceRegistryServlet.
 * @author lrosenberg
 */
public enum WebOperations {
	/**
	 * Adds a consumer to the registry.
	 */
	ADD_CONSUMER,
	/**
	 * Adds a supplier to the registry.
	 */
	ADD_SUPPLIER,
	/**
	 * Notifies that a consumer became unavailable.
	 */
	NOTIFY_CONSUMER,
	/**
	 * Notifies that a supplier became unavailable.
	 */
	NOTIFY_SUPPLIER,
	/**
	 * Returns the list of channels.
	 */
	LIST,
	/**
	 * Resets the registry.
	 */
	RESET,
	/**
	 * Returns info for a channel.
	 */
	CHANNEL_INFO;
	/**
	 * Returns the presentation of the constant as it's used in the web. 
	 * @return the lowercase of the string value.
	 */
	public String toWeb(){
		return toString().toLowerCase();
	}
}
