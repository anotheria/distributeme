package org.distributeme.registry.ui.action;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.lifecycle.LifecycleComponent;
import org.distributeme.support.lifecycle.generated.LifecycleSupportServiceConstants;
import org.distributeme.support.lifecycle.generated.RemoteLifecycleSupportServiceStub;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * This action sends shutdown request to all registered services.
 * @author dsilenko
 */
public class RegistryShutdownAllAction extends BaseRegistryAction implements Action {

	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		List<ServiceDescriptor> descriptors = getRegistry().list();
		for (ServiceDescriptor toShutDown : descriptors){
			try{
				ServiceDescriptor target = toShutDown.changeServiceId(LifecycleSupportServiceConstants.getServiceId());
				LifecycleComponent service = new RemoteLifecycleSupportServiceStub(target);
				service.shutdown("Shut down by registry on behalf of user from "+req.getRemoteAddr());
				addFlashMessage(req, "Shutdown request sent to "+toShutDown);
			}catch(Exception e){
				addFlashMessage(req, "Shutdown attempt on "+toShutDown+" failed.");
			}
		}
		return mapping.redirect();
	}

}
