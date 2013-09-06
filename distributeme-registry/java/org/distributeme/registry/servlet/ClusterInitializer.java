package org.distributeme.registry.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.distributeme.registry.metaregistry.Cluster;
import org.distributeme.registry.metaregistry.ClusterChecker;

/**
 * ServletContextListener which is used to start up the cluster properly.
 * @author lrosenberg
 *
 */
public class ClusterInitializer implements ServletContextListener{
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Cluster.INSTANCE.init();
		if (Cluster.INSTANCE.isClusterActive()){
			ClusterChecker.start();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ClusterChecker.stop();
	}

}
