package org.distributeme.registry.ui.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.distributeme.registry.metaregistry.Cluster;
import org.distributeme.registry.metaregistry.ClusterEntry;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;

/**
 * Displays the current cluster status.
 * @author lrosenberg
 *
 */
public class ShowClusterAction extends BaseClusterAction{

	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean bean,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

		List<ClusterEntry> entries = Cluster.INSTANCE.entries();
		req.setAttribute("entries", entries);
		
		return mapping.success();
	}


}
