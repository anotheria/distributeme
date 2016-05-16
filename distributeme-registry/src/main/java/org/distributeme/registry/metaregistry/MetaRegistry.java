package org.distributeme.registry.metaregistry;

import org.distributeme.core.ServiceDescriptor;

import java.util.List;

/**
 * This interface describes the meta registry which is used to connect multiple hosts in a common system.
 * @author lrosenberg.
 */
public interface MetaRegistry {
	/**
	 * Resolves a service to host. Returns null if there is no binded service.
	 * @param serviceId id of the service to resolve.
	 * @return
	 */
	ServiceDescriptor resolve(String serviceId);
	/**
	 * Binds a service to a host.
	 * @param service servicedescriptor object representing the service.
	 * @return always true sofar.
	 */
	boolean bind(ServiceDescriptor service);
	/**
	 * Unbinds a service from a host. Succeeds only if host is equal to previously bind host.
	 * @param service servicedescriptor object representing the service.
	 * @return true if action has been taken.
	 */
	boolean unbind(ServiceDescriptor service);
	
	/**
	 * Returns a list of bindings.
	 * @return
	 */
	List<ServiceDescriptor> list();
	
	/**
	 * Adds a service listener to the registry.
	 * @param listener
	 */
	void addListener(MetaRegistryListener listener);
	
	/**
	 * Removes a service listener from the registry. 
	 * @param listener
	 */
	void removeListener(MetaRegistryListener listener);
	
	/**
	 * This operation is called whenever a bind has been executing on another instance.
	 * @param service
	 */
	void remoteBind(ServiceDescriptor service);
	
	/**
	 * This operation is called whenever a unbind has been executing on another instance.
	 * @param service
	 */
	void remoteUnbind(ServiceDescriptor service);
	
}
