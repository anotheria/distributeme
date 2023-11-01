package org.distributeme.registry.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
/**
 * Lists registered event channels.
 */
public class ESRegistryListAction extends BaseESRegistryAction{

	@Override
	public ActionCommand execute(ActionMapping mapping,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		List<String> channelNames = getRegistry().getChannelNames();
		req.setAttribute("channelnames", channelNames);
		req.setAttribute("numberOfChannels", channelNames.size());
		
		return mapping.success();
	}

}
