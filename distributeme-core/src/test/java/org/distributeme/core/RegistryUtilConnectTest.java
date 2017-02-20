package org.distributeme.core;

import org.distributeme.core.ServiceDescriptor.Protocol;
import org.junit.Ignore;
import org.junit.Test;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;

public class RegistryUtilConnectTest {
	@Test @Ignore
	public void testConnect() throws RemoteException{
		int start = 9229; int end = 9235;
		int port = start;
		boolean started = false;
		while (port <= end && !started){
			try{
				LocateRegistry.createRegistry(port);
			}catch(ExportException e){
				port++;
			}
			System.out.println("Started on "+port);
			started = true;
		}
		
		if (!started){
			System.out.println("No free port in range found!");
		}
		
		
		
		ServiceDescriptor test1 = new ServiceDescriptor(Protocol.CORBA, RegistryUtil.class.getName(), "ins123", "localhost", 9345);
		
		System.out.println("BIND --> "+RegistryUtil.bind(test1));
		System.out.println("RESOLVE --> "+RegistryUtil.resolve(test1));
		//System.out.println("UNBIND --> "+RegistryUtil.unbind(test1));
		System.out.println("RESOLVE --> "+RegistryUtil.resolve(test1));
		//System.out.println("UNBIND --> "+RegistryUtil.unbind(test1));

	}
}
