package org.distributeme.registry.ui.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
/**
 * Lists registered event channels.
 */
public class ESRegistryListAction extends BaseESRegistryAction{

	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		List<String> channelNames = getRegistry().getChannelNames();
		req.setAttribute("channelnames", channelNames);
		req.setAttribute("numberOfChannels", channelNames.size());
		
		return mapping.success();
	}

}
