package org.distributeme.core.listener;

/**
 * This listener allows to perform operations dependent on the lifecycle of a distributeme component.
 *
 * @author another
 * @version $Id: $Id
 */
public interface ServerLifecycleListener {
	/**
	 * Called immediately after start and registration.
	 */
	void afterStart();
	/**
	 * Called immediately before service stops, in the shutdown hook.
	 */
	void beforeShutdown();
}
