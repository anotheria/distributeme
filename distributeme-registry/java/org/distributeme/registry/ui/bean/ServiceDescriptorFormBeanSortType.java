package org.distributeme.registry.ui.bean;

import net.anotheria.util.sorter.SortType;

public class ServiceDescriptorFormBeanSortType extends SortType{
	public static final int SORT_BY_SERVICEID = 1;
	public static final int SORT_BY_HOST = 2;
	public static final int SORT_BY_PORT = 3;
	public static final int SORT_BY_PROTOCOL = 4;
	public static final int SORT_BY_INSTANCEID = 5;
	public static final int SORT_BY_GLOBALSERVICEID = 6;
	
	public static final int SORT_DEFAULT = SORT_BY_SERVICEID;
	
	public ServiceDescriptorFormBeanSortType(){
		super(SORT_DEFAULT);
	}
	
	public ServiceDescriptorFormBeanSortType(int aSortBy, boolean aSortOrder){
		super(aSortBy, aSortOrder);
	}
	
}
