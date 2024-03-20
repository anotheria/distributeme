package org.distributeme.registry.servlet;

import net.anotheria.moskito.core.logging.LoggerUtil;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.IProducerRegistryAPI;
import net.anotheria.moskito.core.registry.ProducerRegistryAPIFactory;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.util.List;


public class MoskitoLoggerInitializer implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ProducerRegistryFactory.getProducerRegistryInstance();
		// //ADD LOGGING FOR ALL BUILTIN PRODUCERS
		IProducerRegistryAPI api = new ProducerRegistryAPIFactory().createProducerRegistryAPI();
		List<IStatsProducer> stats = api.getAllProducersBySubsystem("builtin");
		for (IStatsProducer producer : stats){
			LoggerUtil.createSLF4JDefaultAndIntervalStatsLogger(producer);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	
}
