package org.distributeme.registry.ui.action;

import net.anotheria.maf.action.Action;
import org.distributeme.registry.metaregistry.MetaRegistry;
import org.distributeme.registry.metaregistry.MetaRegistryImpl;

/**
 * Base class for registry actions.
 */
public abstract class BaseRegistryAction extends BaseAction implements Action{
	/**
	 * MetaRegistry instance.
	 */
	private static MetaRegistry registry = MetaRegistryImpl.getInstance();
	/**
	 * Returns the MetaRegistry for further use.
	 * @return
	 */
	protected MetaRegistry getRegistry(){
		return registry;
	}

	@Override
	protected String getMenuSection() {
		return "registry";
	}
	
	@Override protected String getTitle(){
		return "Registry";
	}

}
