package org.distributeme.tools;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Lists all services running in a VM exposed via specific port.
 *
 * @author lrosenberg
 * @since 02.12.14 01:18
 */
public class List {
	public static void main(String a[]) throws Exception{
		if (a.length != 2){
			System.out.println("Use java ... "+List.class+" host port");
			System.exit(-1);
		}
		String host = a[0];
		int port = Integer.parseInt(a[1]);
		Registry registry = null;
		registry = LocateRegistry.getRegistry(host, port);
		String[] services = registry.list();
		System.out.println("Listing of services  @ \"+host+\":\"+port");
		for (String s : services){
			System.out.println("\t"+s);
		}
	}

}
