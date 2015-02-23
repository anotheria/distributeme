package org.distributeme.registry.ui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;

import org.distributeme.core.ServiceDescriptor;

/**
 * Action that unbinds item from registry.
 * @author dsilenko
 */
public class RegistryUnbindAction extends BaseRegistryAction implements Action {

	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {
		String unbindId = req.getParameter(ID_PARAMETER_NAME);

		ServiceDescriptor toUnBind = ServiceDescriptor.fromRegistrationString(unbindId);
		getRegistry().unbind(toUnBind);

		return mapping.redirect();
	}

}
