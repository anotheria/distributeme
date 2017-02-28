package org.distributeme.core;

/**
 * Created by rboehling on 2/28/17.
 */
public class DebugRegistryConnector implements RegistryConnector {

	@Override
	public boolean bind(ServiceDescriptor service) {
		System.out.println("bind: "  + service);
		return true;
	}

	@Override
	public boolean notifyBind(Location location, ServiceDescriptor descriptor) {
		System.out.println("notifyBind: "  + location+ ", " + descriptor);
		return true;
	}

	@Override
	public boolean notifyUnbind(Location location, ServiceDescriptor descriptor) {
		System.out.println("notifyUnbind: "  + location+ ", " + descriptor);
		return true;
	}

	@Override
	public boolean unbind(ServiceDescriptor service) {
		System.out.println("unbind: "  + service);
		return true;
	}

	@Override
	public ServiceDescriptor resolve(ServiceDescriptor toResolve, Location loc) {
		System.out.println("resolve: "  +  toResolve+ ", " + loc);
		return toResolve;
	}
}
