package org.distributeme.core.lifecycle;

import java.rmi.RemoteException;
/**
 * Interface that is implemented by the generated skeletons and that provide additional information about services.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public interface ServiceAdapter {
	/**
	 * Returns the timestamp of the service creation.
	 *
	 * @throws java.rmi.RemoteException if any.
	 * @return a long.
	 */
	long getCreationTimestamp() throws RemoteException;

	/**
	 * Returns the timestamp of the service last update.
	 *
	 * @throws java.rmi.RemoteException if any.
	 * @return a long.
	 */
	long getLastAccessTimestamp() throws RemoteException;
}
