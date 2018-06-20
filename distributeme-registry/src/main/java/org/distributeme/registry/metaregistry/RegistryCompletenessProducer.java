package org.distributeme.registry.metaregistry;

import net.anotheria.moskito.core.counter.CounterStats;
import net.anotheria.moskito.core.counter.CounterStatsFactory;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.anotheria.moskito.core.stats.TimeUnit;
import net.anotheria.moskito.core.threshold.CustomThresholdProvider;
import net.anotheria.moskito.core.threshold.ThresholdDefinition;
import net.anotheria.moskito.core.threshold.ThresholdRepository;
import net.anotheria.moskito.core.threshold.ThresholdStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * If configured through the registy.conf (or MetaRegistryConfig class) the RegistryCompletenessProducer will produce moskito thresholds in case there are less or more  services than expected.
 *
 * @author lrosenberg
 * @since 26.02.18 18:18
 */
public class RegistryCompletenessProducer implements CustomThresholdProvider, IStatsProducer<CounterStats> {

	private MetaRegistryConfig config;
	private AtomicInteger currentProducerCount = new AtomicInteger();
	private CounterStats stats ;
	private List<CounterStats> statsList = new ArrayList<>(1);

	RegistryCompletenessProducer(){
		config = MetaRegistryConfig.create();
		stats = CounterStatsFactory.DEFAULT_INSTANCE.createStatsObject("Services");
		statsList.add(stats);
		ProducerRegistryFactory.getProducerRegistryInstance().registerProducer(this);
		if (config.isMonitorRequiredServiceAmount()){
			ThresholdDefinition definition = new ThresholdDefinition();
			definition.setDescription("RegistryCompleteness");
			definition.setIntervalName("default");
			definition.setName("RegistryCompleteness");
			definition.setProducerName(getProducerId());
			definition.setStatName("counter");
			definition.setTimeUnit(TimeUnit.MILLISECONDS); //this doesn't matter.
			ThresholdRepository.getInstance().createThreshold(definition);
		}
	}

	@Override
	public ThresholdStatus getStatus() {

		if (!config.isMonitorRequiredServiceAmount())
			return ThresholdStatus.OFF;

		return currentProducerCount.get() == config.getRequiredServiceAmount() ?
				ThresholdStatus.GREEN : ThresholdStatus.RED;
	}

	@Override
	public String getCurrentValue() {
		return "Services "+currentProducerCount.get();
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
