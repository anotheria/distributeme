package org.distributeme.registry.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Base action class that removes consumer or supplier from a channel (actually all channels).
 * @author lrosenberg
 *
 */
abstract class RemoveESParticipantAction extends BaseESRegistryAction{
	@Override
	public ActionCommand execute(ActionMapping mapping,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String channelname = req.getParameter("channel");
		String id = req.getParameter("id");
		dothejob(id);
		
		return mapping.redirect().addParameter("channelname", channelname);
	}
	
	protected abstract void dothejob(String id);

}
