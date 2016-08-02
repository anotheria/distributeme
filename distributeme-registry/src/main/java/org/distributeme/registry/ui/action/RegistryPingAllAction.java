package org.distributeme.registry.ui.action;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.lifecycle.HealthStatus;
import org.distributeme.core.lifecycle.LifecycleComponent;
import org.distributeme.support.lifecycle.generated.LifecycleSupportServiceConstants;
import org.distributeme.support.lifecycle.generated.RemoteLifecycleSupportServiceStub;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Action that pings all registered services.
 * @author lrosenberg
 */
public class RegistryPingAllAction extends BaseRegistryAction implements Action {

	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		List<ServiceDescriptor> descriptors = getRegistry().list();
		for (ServiceDescriptor toPing : descriptors){
			try{
				ServiceDescriptor target = toPing.changeServiceId(LifecycleSupportServiceConstants.getServiceId());
				addFlashMessage(req, "Pinging "+toPing.getSystemWideUniqueId());
				LifecycleComponent service = new RemoteLifecycleSupportServiceStub(target);

				long start = System.currentTimeMillis();
				boolean isOnline = service.isOnline();
				long end = System.currentTimeMillis();
				addFlashMessage(req, "Ping  successful ("+isOnline+") - "+(end - start)+" ms");
				HealthStatus healthStatus = service.getHealthStatus(toPing.getServiceId());
				addFlashMessage(req, "HealthStatus: "+healthStatus);
			}catch(Exception e){
				addFlashMessage(req, "ping and/or getHealthStatus failed - "+e.getMessage());
			}
		}
		return mapping.redirect();
	}

}
