package org.distributeme.registry.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.registry.ui.bean.ServiceDescriptorFormBean;
import org.distributeme.registry.ui.bean.ServiceDescriptorFormBeanSortType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Action that prepares model for showing registry content.
 * @author dsilenko
 */
public class RegistryListAction extends BaseRegistryAction {

	/**
	 * Attribute name for bindings.
	 */
	private static final String BINDINGS_ATTRIBUTE_NAME = "bindings";

	@Override
	public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {

		List<ServiceDescriptor> descriptors = getRegistry().list();
		List<ServiceDescriptorFormBean> bindings = new ArrayList<ServiceDescriptorFormBean>(descriptors.size());

		for (ServiceDescriptor descriptor : descriptors){
			ServiceDescriptorFormBean bean = map(descriptor);
			bindings.add(bean);
		}
		
		//
		int pSortBy = ServiceDescriptorFormBeanSortType.SORT_DEFAULT;
		try{
			pSortBy = Integer.parseInt(req.getParameter("pSortBy"));
		}catch(Exception ignored){}
		boolean pSortOrder = true;
		try{
			String sSortOrder =req.getParameter("pSortOrder"); 
			if (sSortOrder!=null && sSortOrder.equalsIgnoreCase("false"))
				pSortOrder = false;
		}catch(Exception ignored){}
		ServiceDescriptorFormBeanSortType st = new ServiceDescriptorFormBeanSortType(pSortBy, pSortOrder);
		bindings = net.anotheria.util.sorter.StaticQuickSorter.sort(bindings, st);

		req.setAttribute(BINDINGS_ATTRIBUTE_NAME, bindings);
		req.setAttribute("numberOfBindings", bindings.size());

		return mapping.success();
	}

	/**
	 * Maps fields from ServiceDescriptor to new instance of ServiceDescriptorFormBean.
	 * @param serviceDescriptor object to map
	 * @return new instance of ServiceDescriptorFormBean
	 */
	private ServiceDescriptorFormBean map(ServiceDescriptor serviceDescriptor){
		ServiceDescriptorFormBean formBean = new ServiceDescriptorFormBean();

		formBean.setGlobalServiceId(serviceDescriptor.getGlobalServiceId());
		formBean.setServiceId(serviceDescriptor.getServiceId());
		formBean.setInstanceId(serviceDescriptor.getInstanceId());
		formBean.setProtocol(serviceDescriptor.getProtocol());
		formBean.setHost(serviceDescriptor.getHost());
		formBean.setPort(serviceDescriptor.getPort());
		formBean.setRegistrationString(serviceDescriptor.getRegistrationString());

		return formBean;
	}

}
