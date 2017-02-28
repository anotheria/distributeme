package org.distributeme.core;

/**
 * Created by rboehling on 2/28/17.
 */
public interface RegistryConnector {

	boolean bind(ServiceDescriptor service);

	boolean notifyBind(Location location, ServiceDescriptor descriptor);

	boolean notifyUnbind(Location location, ServiceDescriptor descriptor);

	boolean unbind(ServiceDescriptor service);

	ServiceDescriptor resolve(ServiceDescriptor toResolve, Location loc);
}
