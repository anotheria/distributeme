package org.distributeme.test.list;

import org.distributeme.core.util.ServerSideUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 22.09.15 23:44
 */
public class ListServiceImpl implements ListService{

	private HashMap<ListObjectId, ListObject> objects;

	public ListServiceImpl(){
		objects = new HashMap<ListObjectId, ListObject>();
		for (int i=0; i<100;i++){
			objects.put(new ListObjectId(i), new ListObject(i));
		}
	}

	@Override
	public ListObject getListObject(ListObjectId id) {
		System.out.println(failoverInfoString()+"%%% Called getListObject("+id+")");
		return objects.get(id);
	}

	@Override
	public Collection<ListObject> getListObjects() {
		System.out.println(failoverInfoString()+"%%% Called getListObjects()");
		ArrayList<ListObject> ret = new ArrayList<ListObject>();
		Collection<ListObject> all = objects.values();
		ret.addAll(all);
		return ret;
	}

	@Override
	public Collection<ListObject> getSomeListObjects(Collection<ListObjectId> ids)  {
		System.out.println(failoverInfoString()+"%%% Called getListObjects() with "+ids.size()+" params, "+ids);
		ArrayList<ListObject> ret = new ArrayList<ListObject>();
		for (ListObjectId id : ids){
			ret.add(objects.get(id));
		}
		System.out.println("%%% Returning " + ret);
		return ret;
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
}
