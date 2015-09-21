package org.distributeme.core.stats;

import net.anotheria.moskito.core.producers.IStats;
import net.anotheria.moskito.core.stats.TimeUnit;
import net.anotheria.moskito.webui.decorators.AbstractDecorator;
import net.anotheria.moskito.webui.producers.api.LongValueAO;
import net.anotheria.moskito.webui.producers.api.StatValueAO;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 */
public class RoutingStatsDecorator extends AbstractDecorator {

	/**
	 * Captions.
	 */
	static final String CAPTIONS[] = {
			"RRT",
			"FC",
			"FD",
			"RD",
			"BL",
	};

	/**
	 * Short explanations.
	 */
	static final String SHORT_EXPLANATIONS[] = {
			"RequestRoutedTo",
			"Failed Call",
			"Fail Decision",
			"Retry Decision",
			"Blacklisted",
	};

	/**
	 * Explanations.
	 */
	static final String EXPLANATIONS[] = {
			"Requests routed to this instance",
			"Failed calls by this instance",
			"Fail decision after a failed call",
			"Retry decision after a failed call",
			"Call that has been aborted due to blacklisting of an instance",
	};

	/**
	 * Creates a new decorator object with given name.
	 */
	public RoutingStatsDecorator(){
		super("Routing", CAPTIONS, SHORT_EXPLANATIONS, EXPLANATIONS);
	}

	@Override
	public List<StatValueAO> getValues(IStats statsObject, String interval, TimeUnit unit) {
		RoutingStats stats = (RoutingStats)statsObject;
		List<StatValueAO> ret = new ArrayList<StatValueAO>(CAPTIONS.length);
		int i = 0;
		ret.add(new LongValueAO(CAPTIONS[i++], stats.getRequestRoutedToCount(interval)));
		ret.add(new LongValueAO(CAPTIONS[i++], stats.getFailedCallCount(interval)));
		ret.add(new LongValueAO(CAPTIONS[i++], stats.getFailDecisionCount(interval)));
		ret.add(new LongValueAO(CAPTIONS[i++], stats.getRetryDecisionCount(interval)));
		ret.add(new LongValueAO(CAPTIONS[i++], stats.getBlacklistedCount(interval)));

		return ret;
	}
}
