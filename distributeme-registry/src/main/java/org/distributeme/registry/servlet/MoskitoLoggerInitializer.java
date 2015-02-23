package org.distributeme.registry.servlet;

import net.anotheria.moskito.core.logging.DefaultStatsLogger;
import net.anotheria.moskito.core.logging.IntervalStatsLogger;
import net.anotheria.moskito.core.logging.SL4JLogOutput;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.IProducerRegistryAPI;
import net.anotheria.moskito.core.registry.ProducerRegistryAPIFactory;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.anotheria.moskito.core.stats.DefaultIntervals;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;


public class MoskitoLoggerInitializer implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ProducerRegistryFactory.getProducerRegistryInstance();
		// //ADD LOGGING FOR ALL BUILTIN PRODUCERS
		IProducerRegistryAPI api = new ProducerRegistryAPIFactory().createProducerRegistryAPI();
		List<IStatsProducer> stats = api.getAllProducersBySubsystem("builtin");
		for (IStatsProducer producer : stats){
			new DefaultStatsLogger(producer, new SL4JLogOutput(LoggerFactory.getLogger("MoskitoBIDefault")));
			new IntervalStatsLogger(producer, DefaultIntervals.FIVE_MINUTES, new SL4JLogOutput(LoggerFactory.getLogger("MoskitoBI5m")));
			new IntervalStatsLogger(producer, DefaultIntervals.FIFTEEN_MINUTES, new SL4JLogOutput(LoggerFactory.getLogger("MoskitoBI15m")));
			new IntervalStatsLogger(producer, DefaultIntervals.ONE_HOUR, new SL4JLogOutput(LoggerFactory.getLogger("MoskitoBI1h")));
			new IntervalStatsLogger(producer, DefaultIntervals.ONE_DAY, new SL4JLogOutput(LoggerFactory.getLogger("MoskitoBI1d")));
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	
}
