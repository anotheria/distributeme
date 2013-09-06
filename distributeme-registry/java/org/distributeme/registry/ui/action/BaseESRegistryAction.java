package org.distributeme.registry.ui.action;

import org.distributeme.registry.esregistry.EventServiceRegistry;
import org.distributeme.registry.esregistry.EventServiceRegistryImpl;

/**
 * Base class for event service registry related actions.
 * @author lrosenberg
 *
 */
public abstract class BaseESRegistryAction extends BaseAction{
	/**
	 * EventServiceRegistry instance.
	 */
	private EventServiceRegistry registry = EventServiceRegistryImpl.getInstance();
	/**
	 * Returns the eventserviceregistry instance for internal use.
	 * @return
	 */
	protected EventServiceRegistry getRegistry(){
		return registry;
	}
	@Override
	protected String getMenuSection() {
		return "esregistry";
	}
	
	@Override protected String getTitle(){
		return "EventService Registry";
	}
}
