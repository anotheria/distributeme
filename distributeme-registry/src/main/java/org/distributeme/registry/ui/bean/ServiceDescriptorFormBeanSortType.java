package org.distributeme.registry.ui.bean;

import net.anotheria.util.sorter.SortType;

/**
 * Sort type (helper class for StaticQuickSorter) for sorting of service descriptions.
 */
public class ServiceDescriptorFormBeanSortType extends SortType{
	/**
	 * By service id.
	 */
	public static final int SORT_BY_SERVICEID = 1;
	/**
	 * By host name.
	 */
	public static final int SORT_BY_HOST = 2;
	/**
	 * By port.
	 */
	public static final int SORT_BY_PORT = 3;
	/**
	 * By supported protocol (rmi etc).
	 */
	public static final int SORT_BY_PROTOCOL = 4;
	/**
	 * By instance id.
	 */
	public static final int SORT_BY_INSTANCEID = 5;
	/**
	 * By full/complete service id.
	 */
	public static final int SORT_BY_GLOBALSERVICEID = 6;
	/**
	 * By node title in a tree view
	 */
	public static final int SORT_BY_TITLE = 7;

	/**
	 * The default sort type if no sort type is specified.
	 */
	public static final int SORT_DEFAULT = SORT_BY_SERVICEID;
	
	public ServiceDescriptorFormBeanSortType(){
		super(SORT_DEFAULT);
	}
	
	public ServiceDescriptorFormBeanSortType(int aSortBy, boolean aSortOrder){
		super(aSortBy, aSortOrder);
	}
	
}
