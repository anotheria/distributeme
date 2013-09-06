package org.distributeme.registry.metaregistry;

import org.distributeme.core.ServiceDescriptor;

/**
 * This registry listener is used by the cluster to be notified of all bind/unbinds on the registry. 
 * @author lrosenberg
 */
public class ClusterRegistryListener implements MetaRegistryListener{

	@Override
	public void onBind(ServiceDescriptor service) {
		Cluster.INSTANCE.addSyncCommand(ClusterSyncCommand.bind(service));
	}

	@Override
	public void onUnbind(ServiceDescriptor service) {
		Cluster.INSTANCE.addSyncCommand(ClusterSyncCommand.unbind(service));
	}

}
