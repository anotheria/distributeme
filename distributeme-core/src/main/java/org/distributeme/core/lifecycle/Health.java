package org.distributeme.core.lifecycle;

/**
 * Health state of a service.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public enum Health {
	/**
	 * The service is ok.
	 */
	GREEN, 
	/**
	 * The service is functioning but has some problems.
	 */
	YELLOW, 
	/**
	 * The service is malfunctioning.
	 */
	RED;
}
