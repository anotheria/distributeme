package org.distributeme.core.interceptor.moskito;

import net.anotheria.moskito.core.producers.IStats;
import net.anotheria.moskito.core.producers.IStatsProducer;

import java.util.Collections;
import java.util.List;

/**
 * This producer is a dummy class that is used for grouping of requests in journey analyzes.
 * @author lrosenberg
 *
 */
public enum MoskitoJourneyInterceptorStatsProducer implements IStatsProducer{
	NETWORK, SKELETON;
	

	@Override
	public List<IStats> getStats() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public String getProducerId() {
		return name();
	}

	@Override
	public String getCategory() {
		return "default";
	}

	@Override
	public String getSubsystem() {
		return "default";
	}

}
