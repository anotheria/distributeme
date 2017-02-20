package org.distributeme.core.stats;

import org.distributeme.core.routing.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This helper class works as delegate between a RoutingStats object and the caller.
 *
 * @author lrosenberg
 * @since 21.09.15 01:18
 * @version $Id: $Id
 */
public class RoutingStatsWrapper implements RoutingStatsCollector{

	private static Logger log = LoggerFactory.getLogger(Constants.ROUTING_LOGGER_NAME);

	/**
	 * Case specific stats.
	 */
	private RoutingStats caseStats;
	/**
	 * Default stats for all cases.
	 */
	private RoutingStats defaultStats;

	private String serviceId;

	/**
	 * <p>Constructor for RoutingStatsWrapper.</p>
	 *
	 * @param aServiceId a {@link java.lang.String} object.
	 * @param aCaseStats a {@link org.distributeme.core.stats.RoutingStats} object.
	 * @param aDefaultStats a {@link org.distributeme.core.stats.RoutingStats} object.
	 */
	public RoutingStatsWrapper(String aServiceId, RoutingStats aCaseStats, RoutingStats aDefaultStats){
		caseStats = aCaseStats;
		defaultStats = aDefaultStats;
		serviceId = aServiceId;
	}

	/** {@inheritDoc} */
	@Override
	public void addFailDecision() {
		if (log.isDebugEnabled()){
			log.debug(""+serviceId+" - added decision to fail");
		}
		if (caseStats != null)
			caseStats.addFailDecision();
		if (defaultStats != null)
			defaultStats.addFailDecision();
	}

	/** {@inheritDoc} */
	@Override
	public void addFailedCall() {
		if (log.isDebugEnabled()){
			log.debug(""+serviceId+" - call failed");
		}
		if (caseStats != null)
			caseStats.addFailedCall();
		if (defaultStats != null)
			defaultStats.addFailedCall();
	}

	/** {@inheritDoc} */
	@Override
	public void addRetryDecision() {
		if (log.isDebugEnabled()){
			log.debug(""+serviceId+" - added decision to retry call");
		}

		if (caseStats != null)
			caseStats.addRetryDecision();
		if (defaultStats != null)
			defaultStats.addRetryDecision();
	}

	/** {@inheritDoc} */
	@Override
	public void addBlacklisted() {
		if (log.isDebugEnabled()){
			log.debug(""+serviceId+" - call canceled due to blacklisting");
		}

		if (caseStats != null)
			caseStats.addBlacklisted();
		if (defaultStats != null)
			defaultStats.addBlacklisted();
	}

	/** {@inheritDoc} */
	@Override
	public void addRequestRoutedTo() {
		if (log.isDebugEnabled()){
			log.debug(""+serviceId+" - new request will be sent to");
		}

		if (caseStats != null)
			caseStats.addRequestRoutedTo();
		if (defaultStats != null)
			defaultStats.addRequestRoutedTo();
	}
}
