package org.distributeme.core.concurrencycontrol;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.ServerSideCallContext;

/**
 * Base implementation of a ConcurrencyControlStrategy which is not doing anything. You can use it as base adapter and overwrite classes you need.
 * @author lrosenberg
 *
 */
public abstract class AbstractConcurrencyControlStrategy implements ConcurrencyControlStrategy{

	@Override
	public void notifyClientSideCallStarted(ClientSideCallContext context){
		
	}
	@Override
	public void notifyClientSideCallFinished(ClientSideCallContext context){
		
	}
	
	@Override
	public void notifyServerSideCallStarted(ServerSideCallContext context){
		
	}
	@Override
	public void notifyServerSideCallFinished(ServerSideCallContext context){
		
	}

	@Override
	public void customize(String parameter) {
	}

}
