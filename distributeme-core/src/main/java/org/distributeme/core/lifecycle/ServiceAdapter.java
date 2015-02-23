package org.distributeme.core.lifecycle;

import java.rmi.RemoteException;
/**
 * Interface that is implemented by the generated skeletons and that provide additional information about services.
 * @author lrosenberg
 *
 */
public interface ServiceAdapter {
	/**
	 * Returns the timestamp of the service creation. 
	 * @return
	 * @throws RemoteException
	 */
	long getCreationTimestamp() throws RemoteException;

	/**
	 * Returns the timestamp of the service last update. 
	 * @return
	 * @throws RemoteException
	 */
	long getLastAccessTimestamp() throws RemoteException;
}
