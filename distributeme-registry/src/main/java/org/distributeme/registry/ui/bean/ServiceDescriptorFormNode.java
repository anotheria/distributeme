package org.distributeme.registry.ui.bean;

import net.anotheria.util.sorter.IComparable;

import java.util.List;

import static org.distributeme.registry.ui.bean.ServiceDescriptorFormBeanSortType.SORT_BY_TITLE;

public class ServiceDescriptorFormNode implements net.anotheria.util.sorter.IComparable{

    /**
     * The name of the node - usually consists of (instance id + port + host)
     */
    private String title;

    /**
     * The list of service ids and registration strings which have the same node title
     */
    private List<NodeServiceData> nodeDataList;

    /**
     * Default constructor.
     */
    public ServiceDescriptorFormNode() {
    }

    public ServiceDescriptorFormNode(String title, List<NodeServiceData> nodeDataList) {
        this.title = title;
        this.nodeDataList = nodeDataList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<NodeServiceData> getNodeDataList() {
        return nodeDataList;
    }

    public void setNodeDataList(List<NodeServiceData> nodeDataList) {
        this.nodeDataList = nodeDataList;
    }

    @Override
    public String toString() {
        return "ServiceDescriptorFormNode{" +
                "title='" + title + '\'' +
                ", serviceIds=" + nodeDataList +
                '}';
    }

    @Override
    public int compareTo(IComparable anotherComprable, int method) {
        ServiceDescriptorFormNode node = (ServiceDescriptorFormNode)anotherComprable;
        switch(method){
            case SORT_BY_TITLE:
                return net.anotheria.util.BasicComparable.compareString(title, node.title);
            default:
                throw new IllegalArgumentException("Unsupported method for sorting "+method);

        }
    }
}
