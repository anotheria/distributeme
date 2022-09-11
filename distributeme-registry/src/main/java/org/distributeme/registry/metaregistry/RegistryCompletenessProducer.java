package org.distributeme.registry.metaregistry;

import net.anotheria.moskito.core.counter.CounterStats;
import net.anotheria.moskito.core.counter.CounterStatsFactory;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.anotheria.moskito.core.threshold.CustomThresholdProvider;
import net.anotheria.moskito.core.threshold.CustomThresholdStatus;
import net.anotheria.moskito.core.threshold.ThresholdStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * If configured through the registy.conf (or MetaRegistryConfig class) the RegistryCompletenessProducer will produce moskito thresholds in case there are less or more  services than expected.
 *
 * @author lrosenberg
 * @since 26.02.18 18:18
 */
public class RegistryCompletenessProducer implements CustomThresholdProvider, IStatsProducer<CounterStats> {

	/**
	 * Instance of distributeme registry config.
	 */
	private MetaRegistryConfig config;
	/**
	 * Counter for producers.
	 */
	private AtomicInteger currentProducerCount = new AtomicInteger();
	/**
	 * My stats object.
	 */
	private CounterStats stats ;
	/**
	 * Stats list.
	 */
	private List<CounterStats> statsList = new ArrayList<>(1);

	/**
	 * Names of thresholds.
	 */
	private static final String THRESHOLDS_NAMES[] = {"RegistryCompleteness"};

	RegistryCompletenessProducer(){
		config = MetaRegistryConfig.create();
		stats = CounterStatsFactory.DEFAULT_INSTANCE.createStatsObject("Services");
		statsList.add(stats);
		ProducerRegistryFactory.getProducerRegistryInstance().registerProducer(this);
	}

	@Override
	public List<String> getCustomThresholdNames() {
		return Arrays.asList(THRESHOLDS_NAMES);
	}

	@Override
	public CustomThresholdStatus getCustomThresholdStatus(String thresholdName) {
		if (!config.isMonitorRequiredServiceAmount())
			return new CustomThresholdStatus(ThresholdStatus.OFF, "OFF");

		return currentProducerCount.get() == config.getRequiredServiceAmount() ?
				new CustomThresholdStatus(ThresholdStatus.GREEN, ""+currentProducerCount.get()) :
				new CustomThresholdStatus(ThresholdStatus.RED, "Expected "+config.getRequiredServiceAmount()
				+", has "+currentProducerCount.get());
	}


	public void setServiceCount(int count) {
		currentProducerCount.set(count);
		stats.set(count);
	}

	@Override
	public List<CounterStats> getStats() {
		return statsList;
	}

	@Override
	public String getProducerId() {
		return getClass().getSimpleName();
	}

	@Override
	public String getCategory() {
		return "registry";
	}

	@Override
	public String getSubsystem() {
		return "distributeme";
	}
}
