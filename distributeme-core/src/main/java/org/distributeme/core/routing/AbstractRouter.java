package org.distributeme.core.routing;

import net.anotheria.moskito.core.dynamic.OnDemandStatsProducer;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducerException;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.anotheria.util.StringUtils;
import org.distributeme.core.stats.RoutingStats;
import org.distributeme.core.stats.RoutingStatsCollector;
import org.distributeme.core.stats.RoutingStatsFactory;
import org.distributeme.core.stats.RoutingStatsWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for Router. Introduced to be able to handle routing statistics centrally.
 *
 * @author lrosenberg
 * @since 21.09.15 00:45
 * @version $Id: $Id
 */
public abstract class AbstractRouter implements Router{

	/**
	 * Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractRouter.class);

	/**
	 * Stats producer.
	 */
	private OnDemandStatsProducer<RoutingStats> producer;

	/**
	 * ServiceId. Provided by the config (or customization).
	 */
	private String serviceId;

	/** {@inheritDoc} */
	@Override
	public void customize(String serviceId, String parameter) {
		setServiceId(serviceId);
		customize(parameter);
	}

	/**
	 * This is old style customize. It is called by the 'new' customize.
	 *
	 * @param parameter a {@link java.lang.String} object.
	 */
	public void customize(String parameter){
		//do nothing.
	}

	/**
	 * <p>Setter for the field <code>serviceId</code>.</p>
	 *
	 * @param serviceId a {@link java.lang.String} object.
	 */
	protected void setServiceId(String serviceId) {
		this.serviceId = serviceId;
		producer = new OnDemandStatsProducer<RoutingStats>(serviceId2ProducerId(serviceId), "router", "distributeme", RoutingStatsFactory.DEFAULT_INSTANCE);
		ProducerRegistryFactory.getProducerRegistryInstance().registerProducer(producer);
	}

	/**
	 * <p>getRoutingStats.</p>
	 *
	 * @param serviceId a {@link java.lang.String} object.
	 * @return a {@link org.distributeme.core.stats.RoutingStatsCollector} object.
	 */
	protected RoutingStatsCollector getRoutingStats(String serviceId){

		//if there is no producer, return empty wrapper, which does nothing.
		if (producer == null)
			return new RoutingStatsWrapper(serviceId, null, null);

		RoutingStats caseStats = null;
		try{
			caseStats = producer.getStats(serviceId2Name(serviceId));
		}catch(OnDemandStatsProducerException e){
			log.warn("Can't obtain case stats for "+serviceId, e);
		}

		RoutingStats defaultStats = producer.getDefaultStats();

		return new RoutingStatsWrapper(serviceId, caseStats, defaultStats);
	}

	private static String serviceId2Name(String serviceId){
		int lastIndexOfUnderscore = serviceId.lastIndexOf('_');
		if (lastIndexOfUnderscore == -1)
			return serviceId;
		lastIndexOfUnderscore = serviceId.lastIndexOf('_', lastIndexOfUnderscore);
		if (lastIndexOfUnderscore == -1)
			return serviceId;
		return serviceId.substring(lastIndexOfUnderscore);
	}

	private static String serviceId2ProducerId(String serviceId){
		String[] tokens = StringUtils.tokenize(serviceId, '_');
		StringBuilder ret = new StringBuilder();
		for (int i =0; i<tokens.length-1; i++){
			if (tokens[i].length()==0)
				continue;
			if (ret.length()>0)
				ret.append('_');
			ret.append(tokens[i].charAt(0));
		}

		ret.append('_').append(tokens[tokens.length-1]).append("-Router");
		return ret.toString();
	}

}
