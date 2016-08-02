package org.distributeme.registry.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.distributeme.registry.esregistry.ChannelDescriptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Shows details of an event channel.
 * @author lrosenberg
 *
 */
public class ShowESChannelAction extends ESRegistryListAction{

	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

		super.execute(mapping, formBean, req, res);
		
		String channelname = req.getParameter("channelname");
		req.setAttribute("selectedname", channelname);
		ChannelDescriptor channel = getRegistry().getChannel(channelname);
		
		if (channel!=null){
			if (channel.getSuppliers()!=null && !channel.getSuppliers().isEmpty())
			req.setAttribute("suppliers", channel.getSuppliers());
			if (channel.getConsumers()!=null && !channel.getConsumers().isEmpty())
			req.setAttribute("consumers", channel.getConsumers());
		}
		
		
		return mapping.success();
	}

}
