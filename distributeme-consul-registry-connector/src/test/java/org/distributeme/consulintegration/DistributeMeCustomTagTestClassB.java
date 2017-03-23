package org.distributeme.consulintegration;

import org.distributeme.core.CustomTagProvider;

/**
 * @author bjochheim
 * @since 3/17/17.
 */
public class DistributeMeCustomTagTestClassB implements CustomTagProvider {

	@Override
	public String getTag() {
		return "my_custom_tag_a";
	}
}
