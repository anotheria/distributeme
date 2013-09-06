package org.distributeme.registry.metaregistry;

import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.ConfigureMe;

@ConfigureMe(allfields=true, name="registryconfig")
public class ClusterConfiguration {
	/**
	 * Cluster configuration string contains a host:port list.
	 */
	private String clusterInstances;
	
	/**
	 * Cluster check period. Default is once a minute, which is not appropriate for production environments.
	 */
	private long clusterCheckPeriod = 1000L*60;
	
	public void setClusterInstances(String clusterInstances) {
		this.clusterInstances = clusterInstances;
	}

	@AfterConfiguration public void reconfigure(){
		Cluster.INSTANCE.reconfigure(clusterInstances);
	}

	public long getClusterCheckPeriod() {
		return clusterCheckPeriod;
	}

	public void setClusterCheckPeriod(long clusterCheckPeriod) {
		this.clusterCheckPeriod = clusterCheckPeriod;
	}

}
