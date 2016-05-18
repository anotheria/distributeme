package org.distributeme.core.stats;

import net.anotheria.moskito.core.predefined.Constants;
import net.anotheria.moskito.core.producers.AbstractStats;
import net.anotheria.moskito.core.stats.StatValue;
import net.anotheria.moskito.core.stats.TimeUnit;
import net.anotheria.moskito.core.stats.impl.StatValueFactory;

import java.util.*;

import static net.anotheria.moskito.core.decorators.DecoratorRegistryFactory.getDecoratorRegistry;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 21.09.15 00:26
 */
public class RoutingStats extends AbstractStats implements RoutingStatsCollector {



	public static enum StatDef {
		RequestRoutedTo("RRT"),
		FailedCall("FC"),
		FailDecision("FD"),
		RetryDecision("RD"),
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

	private StatValue requestRoutedTo;
	private StatValue failedCall;
	private StatValue failDecision;
	private StatValue retryDecision;
	private StatValue blackListed;

	private HashMap<String, StatValue> name2value = new HashMap<String, StatValue>();

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


	@Override
	public String toStatsString(String s, TimeUnit timeUnit) {
		return null;
	}

	@Override
	public String getValueByNameAsString(String valueName, String intervalName, TimeUnit timeUnit) {
		StatValue value = name2value.get(valueName);
		return value == null ?
				super.getValueByNameAsString(valueName, intervalName, timeUnit) :
				value.getValueAsString(intervalName);
	}

	@Override
	public List<String> getAvailableValueNames() {
		return StatDef.getStatNames();
	}


	public void addFailedCall(){
		failedCall.increase();
	}

	public void addFailDecision(){
		failDecision.increase();

	}

	public void addRetryDecision(){
		retryDecision.increase();
	}

	public void addRequestRoutedTo() {
		requestRoutedTo.increase();
	}

	public void addBlacklisted() {
		blackListed.increase();
	}


	public long getFailedCallCount(String intervalName){
		return failedCall.getValueAsLong(intervalName);
	}
	public long getFailDecisionCount(String intervalName){
		return failDecision.getValueAsLong(intervalName);
	}
	public long getRetryDecisionCount(String intervalName){
		return retryDecision.getValueAsLong(intervalName);
	}
	public long getRequestRoutedToCount(String intervalName){
		return requestRoutedTo.getValueAsLong(intervalName);
	}
	public long getBlacklistedCount(String intervalName){ return blackListed.getValueAsLong(intervalName);}

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