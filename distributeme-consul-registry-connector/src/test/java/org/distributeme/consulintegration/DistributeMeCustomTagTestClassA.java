package org.distributeme.consulintegration;

/**
 * @author bjochheim
 * @since 3/17/17.
 */
public class DistributeMeCustomTagTestClassA implements CustomTagProvider {

	@Override
	public String getTag() {
		return "my_custom_tag_b";
	}
}
