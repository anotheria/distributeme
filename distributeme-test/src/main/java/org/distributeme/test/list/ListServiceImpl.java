package org.distributeme.test.list;

import org.distributeme.core.routing.RoutingAware;
import org.distributeme.core.util.ServerSideUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 22.09.15 23:44
 */
public class ListServiceImpl implements ListService, RoutingAware{

	private HashMap<ListObjectId, ListObject> objects;

	public ListServiceImpl(){
		objects = new HashMap<>();
		for (int i=0; i<100;i++){
			objects.put(new ListObjectId(i), new ListObject(i));
		}
	}

	@Override
	public ListObject getListObject(ListObjectId id) {
		System.out.println(failoverInfoString()+"%%% Called getListObject("+id+ ')');
		return objects.get(id);
	}

	@Override
	public Collection<ListObject> getListObjects() {
		System.out.println(failoverInfoString()+"%%% Called getListObjects()");
		List<ListObject> ret = new ArrayList<>();
		Collection<ListObject> all = objects.values();
		ret.addAll(all);
		return ret;
	}

	@Override
	public Collection<ListObject> getSomeListObjects(Collection<ListObjectId> ids)  {
		System.out.println(failoverInfoString()+"%%% Called getListObjects() with "+ids.size()+" params, "+ids);
		List<ListObject> ret = new ArrayList<>();
		for (ListObjectId id : ids){
			ret.add(objects.get(id));
		}
		System.out.println("%%% Returning " + ret);
		return ret;
	}

	@Override
	public Collection<ListObject> getListObjectsSharded() {
		System.out.println(failoverInfoString()+"%%% Called getListObjectsSharded()");
		return null;
	}

	@Override
	public Collection<ListObject> getSomeListObjectsSharded(Collection<ListObjectId> ids) {
		System.out.println(failoverInfoString()+"%%% Called getSomeListObjectsSharded() with "+ids.size()+" params, "+ids);
		return null;
	}

	private String failoverInfoString(){
		return isFailoverCall() ? "FAILOVER " +
				(ServerSideUtils.isBlacklisted() ? "-BLACKLIST-" : "")
				+ " - "
				: "";
	}

	private boolean isFailoverCall(){
		return ServerSideUtils.isFailoverCall();
	}

	@Override
	public void notifyServiceId(String definedServiceId, String registeredAsServiceId, String routingParameter, String configurationName) {
		System.out.println("I am "+definedServiceId+" registered as "+registeredAsServiceId);
		System.out.println("My Routing Param is "+routingParameter+", myConfig name "+configurationName);
	}
}
