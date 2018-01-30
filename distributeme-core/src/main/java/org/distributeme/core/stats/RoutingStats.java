package org.distributeme.core.stats;

import net.anotheria.moskito.core.predefined.Constants;
import net.anotheria.moskito.core.producers.AbstractStats;
import net.anotheria.moskito.core.stats.StatValue;
import net.anotheria.moskito.core.stats.TimeUnit;
import net.anotheria.moskito.core.stats.impl.StatValueFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.anotheria.moskito.core.decorators.DecoratorRegistryFactory.getDecoratorRegistry;

/**
 * Stats for Routing behaviour of the router.
 *
 * @author lrosenberg
 * @since 21.09.15 00:26
 * @version $Id: $Id
 */
public class RoutingStats extends AbstractStats implements RoutingStatsCollector {

	public static enum StatDef {
		/**
		 * How many requests has been directed to this router.
		 */
		RequestRoutedTo("RRT"),
		/**
		 * How many calls has failed.
		 */
		FailedCall("FC"),
		/**
		 * How many fail decisions have been returned.
		 */
		FailDecision("FD"),
		/**
		 * How many retry decisions have been returned.
		 */
		RetryDecision("RD"),
		/**
		 * How many instances have been blacklisted.
		 */
		Blacklisted("BL"),
		;

		private String statName;

		private StatDef(final String aStatName) {
			statName = aStatName;
		}

		public String getStatName() {
			return statName;
		}

		public static List<String> getStatNames() {
			List<String> ret = new ArrayList<String>(StatDef.values().length);
			for (StatDef value : StatDef.values()) {
				ret.add(value.getStatName());
			}
			return ret;
		}

		public static StatDef getValueByName(String statName) {
			for (StatDef value : StatDef.values()) {
				if (value.getStatName().equals(statName)) {
					return value;
				}
			}
			throw new IllegalArgumentException("No such value with name: " + statName);
		}
	}

	static{
		getDecoratorRegistry().addDecorator(RoutingStats.class, new RoutingStatsDecorator());
	}

	/**
	 * How many requests has been directed to this router.
	 */
	private StatValue requestRoutedTo;
	/**
	 * Number of calls for fail/retry decision.
	 */
	private StatValue failedCall;
	/**
	 * Number of fail decisions.
	 */
	private StatValue failDecision;
	/**
	 * Number of retry decisions.
	 */
	private StatValue retryDecision;
	/**
	 * Number of blacklisted instances.
	 */
	private StatValue blackListed;

	private HashMap<String, StatValue> name2value = new HashMap<String, StatValue>();

	/**
	 * <p>Constructor for RoutingStats.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public RoutingStats(String name) {
		super(name);

		requestRoutedTo = StatValueFactory.createStatValue(Long.valueOf(0), StatDef.RequestRoutedTo.getStatName(), Constants.getDefaultIntervals());
		failedCall = StatValueFactory.createStatValue(Long.valueOf(0), StatDef.FailedCall.getStatName(), Constants.getDefaultIntervals());
		failDecision = StatValueFactory.createStatValue(Long.valueOf(0), StatDef.FailDecision.getStatName(), Constants.getDefaultIntervals());
		retryDecision = StatValueFactory.createStatValue(Long.valueOf(0), StatDef.RetryDecision.getStatName(), Constants.getDefaultIntervals());
		blackListed = StatValueFactory.createStatValue(Long.valueOf(0), StatDef.Blacklisted.getStatName(), Constants.getDefaultIntervals());

		name2value.put(StatDef.RequestRoutedTo.getStatName(), requestRoutedTo);
		name2value.put(StatDef.FailedCall.getStatName(), failedCall);
		name2value.put(StatDef.FailDecision.getStatName(), failDecision);
		name2value.put(StatDef.RetryDecision.getStatName(), retryDecision);
		name2value.put(StatDef.Blacklisted.getStatName(), blackListed);

	}


	/** {@inheritDoc} */
	@Override
	public String toStatsString(String s, TimeUnit timeUnit) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String getValueByNameAsString(String valueName, String intervalName, TimeUnit timeUnit) {
		StatValue value = name2value.get(valueName);
		return value == null ?
				super.getValueByNameAsString(valueName, intervalName, timeUnit) :
				value.getValueAsString(intervalName);
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getAvailableValueNames() {
		return StatDef.getStatNames();
	}


	/**
	 * <p>addFailedCall.</p>
	 */
	public void addFailedCall(){
		failedCall.increase();
	}

	/**
	 * <p>addFailDecision.</p>
	 */
	public void addFailDecision(){
		failDecision.increase();

	}

	/**
	 * <p>addRetryDecision.</p>
	 */
	public void addRetryDecision(){
		retryDecision.increase();
	}

	/**
	 * <p>addRequestRoutedTo.</p>
	 */
	public void addRequestRoutedTo() {
		requestRoutedTo.increase();
	}

	/**
	 * <p>addBlacklisted.</p>
	 */
	public void addBlacklisted() {
		blackListed.increase();
	}


	/**
	 * <p>getFailedCallCount.</p>
	 *
	 * @param intervalName a {@link java.lang.String} object.
	 * @return a long.
	 */
	public long getFailedCallCount(String intervalName){
		return failedCall.getValueAsLong(intervalName);
	}
	/**
	 * <p>getFailDecisionCount.</p>
	 *
	 * @param intervalName a {@link java.lang.String} object.
	 * @return a long.
	 */
	public long getFailDecisionCount(String intervalName){
		return failDecision.getValueAsLong(intervalName);
	}
	/**
	 * <p>getRetryDecisionCount.</p>
	 *
	 * @param intervalName a {@link java.lang.String} object.
	 * @return a long.
	 */
	public long getRetryDecisionCount(String intervalName){
		return retryDecision.getValueAsLong(intervalName);
	}
	/**
	 * <p>getRequestRoutedToCount.</p>
	 *
	 * @param intervalName a {@link java.lang.String} object.
	 * @return a long.
	 */
	public long getRequestRoutedToCount(String intervalName){
		return requestRoutedTo.getValueAsLong(intervalName);
	}
	/**
	 * <p>getBlacklistedCount.</p>
	 *
	 * @param intervalName a {@link java.lang.String} object.
	 * @return a long.
	 */
	public long getBlacklistedCount(String intervalName){ return blackListed.getValueAsLong(intervalName);}

	/** {@inheritDoc} */
	@Override public String toString(){
		StringBuilder ret = new StringBuilder();

		Set<Map.Entry<String, StatValue>> entries = name2value.entrySet();
		for (Map.Entry<String, StatValue> entry : entries){
			if (ret.length()>0)
				ret.append(" ");
			ret.append(entry.getKey()).append(": ").append(entry.getValue().getValueAsLong());
		}

		return ret.toString();
	}


}
