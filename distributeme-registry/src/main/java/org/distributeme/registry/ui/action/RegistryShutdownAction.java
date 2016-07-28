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

/**
 * Action that sends a shutdown request to a service.
 * @author dsilenko
 */
public class RegistryShutdownAction extends BaseRegistryAction implements Action {

	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {
		String serviceId = req.getParameter(ID_PARAMETER_NAME);

		ServiceDescriptor toShutDown = ServiceDescriptor.fromRegistrationString(serviceId);
		ServiceDescriptor target = toShutDown.changeServiceId(LifecycleSupportServiceConstants.getServiceId());
		LifecycleComponent service = new RemoteLifecycleSupportServiceStub(target);
		service.shutdown("Shut down by registry on behalf of user from "+req.getRemoteAddr());
		addFlashMessage(req, "Shutdown request sent to "+toShutDown+", reload to see if its successful.");
		return mapping.redirect();
	}

}
