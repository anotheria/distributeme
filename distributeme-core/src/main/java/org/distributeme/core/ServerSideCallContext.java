package org.distributeme.core;

import java.util.Map;

/**
 * Contains all required infromation in the context of the current call on the server side.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class ServerSideCallContext extends AbstractCallContext{
	/**
	 * <p>Constructor for ServerSideCallContext.</p>
	 *
	 * @param aMethodName a {@link java.lang.String} object.
	 * @param transportableCallContext a {@link java.util.Map} object.
	 */
	public ServerSideCallContext(String aMethodName, Map<?,?> transportableCallContext){
		super(aMethodName);
		setIncomingTransportableCallContext(transportableCallContext);
	}
}
