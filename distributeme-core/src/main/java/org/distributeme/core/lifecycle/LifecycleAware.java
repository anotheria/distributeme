package org.distributeme.core.lifecycle;

/**
 * A service can implement this to signal distributeme that it is aware of the lifecycles and wants to be asked about it status.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public interface LifecycleAware {
	/**
	 * Returns the custom health status. This health status can include stuff like cache state, db connection, etc.
	 *
	 * @return a {@link org.distributeme.core.lifecycle.HealthStatus} object.
	 */
	HealthStatus getHealthStatus();

}
