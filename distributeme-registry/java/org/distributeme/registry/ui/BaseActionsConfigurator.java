package org.distributeme.registry.ui;

import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import net.anotheria.maf.action.CommandForward;
import net.anotheria.maf.action.CommandRedirect;
import org.distributeme.registry.ui.action.ESRegistryListAction;
import org.distributeme.registry.ui.action.RegistryListAction;
import org.distributeme.registry.ui.action.RegistryPingAction;
import org.distributeme.registry.ui.action.RegistryPingAllAction;
import org.distributeme.registry.ui.action.RegistryShutdownAction;
import org.distributeme.registry.ui.action.RegistryShutdownAllAction;
import org.distributeme.registry.ui.action.RegistryUnbindAction;
import org.distributeme.registry.ui.action.RemoveESConsumerAction;
import org.distributeme.registry.ui.action.RemoveESSupplierAction;
import org.distributeme.registry.ui.action.ShowClusterAction;
import org.distributeme.registry.ui.action.ShowESChannelAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base configurator for UI filter.
 *
 * @author dsilenko
 */
public class BaseActionsConfigurator implements ActionMappingsConfigurator {

	/**
	 * Default logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(BaseActionsConfigurator.class);

	@Override
	public void configureActionMappings(ActionMappings mappings) {
		log.debug("Configuring actions mappings...");

		mappings.addMapping("registry", RegistryListAction.class, new CommandForward("success", "/org/distributeme/registry/ui/jsp/RegistryListView.jsp"));
		mappings.addMapping("unbind", RegistryUnbindAction.class, new CommandRedirect("redirect", "registry?"));
		
		mappings.addMapping("ping", RegistryPingAction.class, new CommandRedirect("redirect", "registry?"));
		mappings.addMapping("pingall", RegistryPingAllAction.class, new CommandRedirect("redirect", "registry?"));
		
		mappings.addMapping("shutdown", RegistryShutdownAction.class, new CommandRedirect("redirect", "registry?"));
		mappings.addMapping("shutdownall", RegistryShutdownAllAction.class, new CommandRedirect("redirect", "registry?"));
		
		mappings.addMapping("esregistry", ESRegistryListAction.class, new CommandForward("success", "/org/distributeme/registry/ui/jsp/ESRegistryListView.jsp"));
		mappings.addMapping("eschannel", ShowESChannelAction.class, new CommandForward("success", "/org/distributeme/registry/ui/jsp/ESRegistryListView.jsp"));
		
		mappings.addMapping("removeSupplier", RemoveESSupplierAction.class, new CommandRedirect("redirect", "eschannel"));
		mappings.addMapping("removeConsumer", RemoveESConsumerAction.class, new CommandRedirect("redirect", "eschannel"));
		
		mappings.addMapping("showcluster", ShowClusterAction.class, new CommandForward("success", "/org/distributeme/registry/ui/jsp/Cluster.jsp"));

	}

}
