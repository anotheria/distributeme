package org.distributeme.registry.ui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;

import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.lifecycle.HealthStatus;
import org.distributeme.support.lifecycle.LifecycleSupportService;
import org.distributeme.support.lifecycle.generated.LifecycleSupportServiceConstants;
import org.distributeme.support.lifecycle.generated.RemoteLifecycleSupportServiceStub;

/**
 * Action that pings a service.
 *
 * @author dsilenko, lrosenberg
 */
public class RegistryPingAction extends BaseRegistryAction implements Action {

	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {
		String serviceId = req.getParameter(ID_PARAMETER_NAME);

		ServiceDescriptor toPing = ServiceDescriptor.fromRegistrationString(serviceId);
		ServiceDescriptor target = toPing.changeServiceId(LifecycleSupportServiceConstants.getServiceId());
		
		LifecycleSupportService service = null;
		
		try{
			addFlashMessage(req, "Pinging "+toPing);
			service = new RemoteLifecycleSupportServiceStub(target);
		}catch(Exception e){
			addFlashMessage(req, "Can't create stub, service down? Reason: "+e.getMessage());
		}
		
		
		if (service!=null){
			try{
				long start = System.currentTimeMillis();
				boolean isOnline = service.isOnline();
				long end = System.currentTimeMillis();
				addFlashMessage(req, "Ping successful ("+isOnline+") - "+(end - start)+" ms");
			}catch(Exception e){
				addFlashMessage(req, "Ping failed - "+e.getMessage());
			}
		}
		
		if (service!=null){
			try{
				HealthStatus healthStatus = service.getHealthStatus(toPing.getServiceId());
				//System.out.println("Got service status for "+serviceId+" -> "+healthStatus);
				addFlashMessage(req, "HealthStatus: "+healthStatus);
			}catch(Exception e){
				addFlashMessage(req, "getHealthStatus failed - "+e.getMessage());
			}
		}
		
		return mapping.redirect();
	}

}
