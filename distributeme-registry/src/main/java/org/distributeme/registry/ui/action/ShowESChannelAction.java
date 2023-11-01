package org.distributeme.registry.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import org.distributeme.registry.esregistry.ChannelDescriptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Shows details of an event channel.
 * @author lrosenberg
 *
 */
public class ShowESChannelAction extends ESRegistryListAction{

	@Override
	public ActionCommand execute(ActionMapping mapping,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

		super.execute(mapping, req, res);
		
		String channelname = req.getParameter("channelname");
		req.setAttribute("selectedname", channelname);
		ChannelDescriptor channel = getRegistry().getChannel(channelname);
		
		if (channel!=null){
			if (channel.getSuppliers()!=null && channel.getSuppliers().size()>0)
			req.setAttribute("suppliers", channel.getSuppliers());
			if (channel.getConsumers()!=null && channel.getConsumers().size()>0)
			req.setAttribute("consumers", channel.getConsumers());
		}
		
		
		return mapping.success();
	}

}
