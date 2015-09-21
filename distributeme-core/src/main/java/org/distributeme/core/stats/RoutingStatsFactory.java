package org.distributeme.core.stats;

import net.anotheria.moskito.core.dynamic.IOnDemandStatsFactory;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 08.07.15 21:56
 */
public class RoutingStatsFactory implements IOnDemandStatsFactory<RoutingStats> {

	public static final RoutingStatsFactory DEFAULT_INSTANCE = new RoutingStatsFactory();

	@Override
	public RoutingStats createStatsObject(String name) {
		return new RoutingStats(name);
	}
}