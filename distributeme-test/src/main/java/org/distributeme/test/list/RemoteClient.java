package org.distributeme.test.list;

import net.anotheria.moskito.webui.embedded.StartMoSKitoInspectBackendForRemote;
import org.distributeme.core.ServiceLocator;

import java.util.Collection;
import java.util.LinkedList;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 22.09.15 23:53
 */
public class RemoteClient {



	public static void main(String a[]) throws Exception{
		StartMoSKitoInspectBackendForRemote.startMoSKitoInspectBackend();


		System.out.println("Testing remotely");

		ListService service = ServiceLocator.getRemote(ListService.class);

		System.out.println("===============");
		System.out.println("Getting wrong objects");
		ListObject ret = service.getListObject(new ListObjectId(-1));
		if (ret!=null)
			throw new IllegalArgumentException("ListObject must be null for key -1");
		ret = service.getListObject(new ListObjectId(100));
		if (ret!=null)
			throw new IllegalArgumentException("ListObject must be null for key 100");


		System.out.println("===============");
		System.out.println("Getting 100 objects manually");
		for (int i=0; i<100; i++){
			ret = service.getListObject(new ListObjectId(i));
			if (ret == null)
				throw new IllegalArgumentException("Didn't get object with id "+i);
			if (!ret.toString().endsWith(""+i))
				throw new IllegalArgumentException(ret+" doesn't end with "+i);

		}

		System.out.println("===============");
		System.out.println("Getting all objects at once");
		Collection<ListObject> objects = service.getListObjects();
		if (objects==null || objects.size()!=100)
			throw new IllegalStateException("Operation didn't return 100 objects "+objects);

		System.out.println("===============");
		System.out.println("Getting 10 objects at once");
		LinkedList<ListObjectId> listObjectIds = new LinkedList<ListObjectId>();
		for (int i=0; i<10; i++) {
			listObjectIds.add(new ListObjectId(i));
		}
		Collection<ListObject> someObjects = service.getSomeListObjects(listObjectIds);
		if (someObjects == null)
			throw new IllegalStateException("Return shouldn't be null");
		if (someObjects.size() != 10)
			throw new IllegalStateException("Wrong number of objects ("+someObjects.size()+") in "+someObjects);

		System.out.println("Calling 5 times same object to ensure it lands on same server, check logs");
		ListObjectId anId = new ListObjectId(5);
		for (int i=0;i<5; i++){
			service.getListObject(anId);
		}

		System.out.println("All good");

	}
}
