package org.distributeme.core;
/**
 * Options for service discovery mode. Discovery modes defines how the stub selects a remote instance to connect to.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public enum DiscoveryMode {
	/**
	 * This mode means that the service discovery will be performed automatically via registry.
	 */
	AUTO,
	/**
	 * This mode means that the service discovery will be tied to a named reference submitted to the stub. This mode is useful if you intend to connect to a 
	 * concrete instance of a service and are not interested in the service itself.
	 */
	MANUAL
}
