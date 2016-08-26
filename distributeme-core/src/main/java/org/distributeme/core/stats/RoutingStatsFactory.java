package org.distributeme.core.stats;

import net.anotheria.moskito.core.dynamic.IOnDemandStatsFactory;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 08.07.15 21:56
 * @version $Id: $Id
 */
public class RoutingStatsFactory implements IOnDemandStatsFactory<RoutingStats> {

	/** Constant <code>DEFAULT_INSTANCE</code> */
	public static final RoutingStatsFactory DEFAULT_INSTANCE = new RoutingStatsFactory();

	/** {@inheritDoc} */
	@Override
	public RoutingStats createStatsObject(String name) {
		return new RoutingStats(name);
	}
}
