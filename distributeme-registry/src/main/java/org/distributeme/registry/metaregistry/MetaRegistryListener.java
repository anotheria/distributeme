package org.distributeme.registry.metaregistry;

import org.distributeme.core.ServiceDescriptor;

/**
 * This interface is used to become notified by the MetaRegistry to announce its bind/unbind events. 
 * @author lrosenberg
 */
public interface MetaRegistryListener {
	/**
	 * Called whenever a new service is bind in the registry.
	 * @param service
	 */
	void onBind(ServiceDescriptor service);
	
	/**
	 * Called whenever a previusly bind service is unbind in the registry.
	 * @param service
	 */
	void onUnbind(ServiceDescriptor service);
}
