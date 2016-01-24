package org.distributeme.core;

import org.distributeme.core.conventions.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Utils for handling the RMIRegistry object.
 * @author lrosenberg
 */
public class RMIRegistryUtil {

	/**
	 * Marker for fatal log messages.
	 */
	private static Marker fatal = MarkerFactory.getMarker("FATAL");
	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(RMIRegistryUtil.class);
	/**
	 * Reference to the rmi registry.
	 */
	private static final AtomicReference<Registry> reference = new AtomicReference<Registry>();
	/**
	 * Port on which the rmi registry exports its public api.
	 */
	private static int rmiRegistryPort;

	/**
	 * Finds or creates a new registry.
	 * @return Returns created registry instance.
	 * @throws RemoteException
	 */
	public static Registry findOrCreateRegistry() throws RemoteException {
		return findOrCreateRegistry(-1);
	}

	/**
	 * Finds or creates a new registry.
	 * @param port port to listen to. If port is below zero, it will be ignored.
	 * @return Returns created registry instance.
	 * @throws RemoteException
	 */
	public static Registry findOrCreateRegistry(int port) throws RemoteException{
		
		if (reference.get()!=null){
			return reference.get();
		}
		
		synchronized (reference) {
			log.info("Creating local registry");
			Registry registry;

			if (port >0 || SystemProperties.LOCAL_RMI_REGISTRY_PORT.isSet()){
				//we are hardcore bound to a local port
				try{
					if (port <= 0) {
						port = SystemProperties.LOCAL_RMI_REGISTRY_PORT.getAsInt();
					}
					log.info("Tying to bind to "+port);
					registry = LocateRegistry.createRegistry(port);
					log.info("Started local registry at port "+port);
					reference.set(registry);
					rmiRegistryPort = port;
					return registry;
				}catch(ExportException e){
					log.error(fatal, "local rmi registry port specified but not useable", e);
					throw new AssertionError("Have to halt due to misconfiguration: local rmi registry port: "+SystemProperties.LOCAL_RMI_REGISTRY_PORT.get()+", port is not free or not bind-able "+e.getMessage());
				}catch(NumberFormatException e){
					log.error(fatal, "local rmi registry port specified but not parseable", e);
					throw new AssertionError("Have to halt due to misconfiguration: local rmi registry port: "+SystemProperties.LOCAL_RMI_REGISTRY_PORT.get()+", expected numeric value.");
				}
			}//... end SystemProperties.LOCAL_RMI_REGISTRY_PORT.isSet()
			
			
			RegistryLocation location = RegistryLocation.create();
			int minPort = location.getRmiRegistryMinPort();
			int maxPort = location.getRmiRegistryMaxPort();
			int currentPort = minPort;

			while (currentPort <= maxPort){
				try{
					registry = LocateRegistry.createRegistry(currentPort);
					log.info("Started local registry at port "+currentPort);
					reference.set(registry);
					rmiRegistryPort = currentPort;
					//System.out.println("Reference "+registry+", port "+rmiRegistryPort);
					return registry;
				}catch(ExportException e){
					currentPort++;
				}
			} //...while
			throw new RemoteException("Couldn't obtain free port for a local rmi registry");
		}
	}

	public static int getRmiRegistryPort() {
		return rmiRegistryPort;
	}

}
