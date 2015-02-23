package org.distributeme.registry.ui.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionMapping;

/**
 * Base class for all registry actions.
 * @author lrosenberg
 *
 */
public abstract class BaseAction implements Action{

	
	/**
	 * Parameter name with ID of target item..
	 */
	public static final String ID_PARAMETER_NAME = "id";
	
	public static final String SA_MESSAGES = "messages";

	
	@Override
	public void postProcess(ActionMapping arg0, HttpServletRequest arg1,
			HttpServletResponse arg2) throws Exception {
		
	}

	@Override
	public void preProcess(ActionMapping arg0, HttpServletRequest req,
			HttpServletResponse arg2) throws Exception {

		req.setAttribute("section", getMenuSection());
		req.setAttribute("title", getTitle());

		@SuppressWarnings("unchecked")ArrayList<String> messages = (ArrayList<String>)req.getSession().getAttribute(SA_MESSAGES);
		if (messages!=null){
			req.setAttribute(SA_MESSAGES, messages);
			req.getSession().removeAttribute(SA_MESSAGES);
		}
		
	}
	
	/**
	 * Returns highlighted menu section.
	 * @return
	 */
	protected abstract String getMenuSection();
	/**
	 * Returns highlighted title.
	 */
	protected abstract String getTitle();

	protected void addFlashMessage(HttpServletRequest req, String msg){
		@SuppressWarnings("unchecked")ArrayList<String> messages = (ArrayList<String>)req.getSession().getAttribute(SA_MESSAGES);
		if (messages==null){
			messages = new ArrayList<String>();
			req.getSession().setAttribute(SA_MESSAGES, messages);
		}
		messages.add(msg);
	}
}
