package org.distributeme.core;

/**
 * @author bjochheim
 * @since 3/17/17.
 */
public interface CustomTagProvider {

	/**
	 *
	 * @return a String that is compatible with Consul tags or null for no tag
	 */
	String getTag();

}
