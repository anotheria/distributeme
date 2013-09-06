package org.distributeme.core;

import java.util.Map;

/**
 * Contains all required infromation in the context of the current call on the server side.
 * @author lrosenberg
 */
public class ServerSideCallContext extends AbstractCallContext{
	public ServerSideCallContext(String aMethodName, Map<?,?> transportableCallContext){
		super(aMethodName);
		setIncomingTransportableCallContext(transportableCallContext);
	}
}
