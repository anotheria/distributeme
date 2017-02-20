package org.distributeme.core.interceptor.moskito;

import net.anotheria.moskito.core.producers.IStats;
import net.anotheria.moskito.core.producers.IStatsProducer;

import java.util.Collections;
import java.util.List;

/**
 * This producer is a dummy class that is used for grouping of requests in journey analyzes.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public enum MoskitoJourneyInterceptorStatsProducer implements IStatsProducer{
	/**
	 * Producer for network.
	 */
	NETWORK,
	/**
	 * Producer on the skeleton side.
	 */
	SKELETON;
	
	/** {@inheritDoc} */
	@Override
	public List<IStats> getStats() {
		return Collections.EMPTY_LIST;
	}

	/** {@inheritDoc} */
	@Override
	public String getProducerId() {
		return name();
	}

	/** {@inheritDoc} */
	@Override
	public String getCategory() {
		return "default";
	}

	/** {@inheritDoc} */
	@Override
	public String getSubsystem() {
		return "default";
	}

}
