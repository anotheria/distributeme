package org.distributeme.registry.ui.action;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.support.lifecycle.LifecycleSupportService;
import org.distributeme.support.lifecycle.generated.LifecycleSupportServiceConstants;
import org.distributeme.support.lifecycle.generated.RemoteLifecycleSupportServiceStub;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * This action sends shutdown request to all registered services.
 * @author dsilenko
 */
public class RegistryShutdownAllAction extends BaseRegistryAction implements Action {

	@Override
	public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		List<ServiceDescriptor> descriptors = getRegistry().list();
		for (ServiceDescriptor toShutDown : descriptors){
			try{
				ServiceDescriptor target = toShutDown.changeServiceId(LifecycleSupportServiceConstants.getServiceId());
				LifecycleSupportService service = new RemoteLifecycleSupportServiceStub(target);
				service.shutdown("Shut down by registry on behalf of user from "+req.getRemoteAddr());
				addFlashMessage(req, "Shutdown request sent to "+toShutDown);
			}catch(Exception e){
				addFlashMessage(req, "Shutdown attempt on "+toShutDown+" failed.");
			}
		}
		return mapping.redirect();
	}

}
