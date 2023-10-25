package org.distributeme.registry.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.registry.ui.bean.NodeServiceData;
import org.distributeme.registry.ui.bean.ServiceDescriptorFormBeanSortType;
import org.distributeme.registry.ui.bean.ServiceDescriptorFormNode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Action that prepares model for showing registry content in a tree sorted by instance ids, hosts, ports.
 * @author pshenai
 */
public class RegistryTreeViewAction extends BaseRegistryTreeViewAction {

    /**
     * Attribute name for nodes.
     */
    private static final String NODES_ATTRIBUTE_NAME = "nodes";

    @Override
    public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {

        List<ServiceDescriptor> descriptors = getRegistry().list();
        Map<String, List<NodeServiceData>> nodeMap = new HashMap<>(descriptors.size());
        List<ServiceDescriptorFormNode> nodes = new ArrayList<>();

        for (ServiceDescriptor descriptor : descriptors){
            String nodeTitle = descriptor.getInstanceId() + " " + descriptor.getHost() + "/" + descriptor.getPort();

            if(nodeMap.containsKey(nodeTitle)){
                List<NodeServiceData> servicesData = nodeMap.get(nodeTitle);
                servicesData.add(new NodeServiceData(descriptor.getServiceId(), descriptor.getRegistrationString()));
            } else {
                List<NodeServiceData> servicesData = new ArrayList<>();
                servicesData.add(new NodeServiceData(descriptor.getServiceId(), descriptor.getRegistrationString()));
                nodeMap.put(nodeTitle, servicesData);
            }
        }

        for (String key: nodeMap.keySet()) {
            ServiceDescriptorFormNode node = new ServiceDescriptorFormNode(key, nodeMap.get(key));
            nodes.add(node);
        }


        int pSortBy = ServiceDescriptorFormBeanSortType.SORT_BY_TITLE;
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
        nodes = net.anotheria.util.sorter.StaticQuickSorter.sort(nodes, st);

        req.setAttribute(NODES_ATTRIBUTE_NAME, nodes);
        req.setAttribute("numberOfNodes", nodes.size());

        return mapping.success();
    }
}
