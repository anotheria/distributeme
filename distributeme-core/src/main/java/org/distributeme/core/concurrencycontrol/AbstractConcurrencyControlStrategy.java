package org.distributeme.core.concurrencycontrol;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.ServerSideCallContext;

/**
 * Base implementation of a ConcurrencyControlStrategy which is not doing anything. You can use it as base adapter and overwrite classes you need.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public abstract class AbstractConcurrencyControlStrategy implements ConcurrencyControlStrategy{

	/** {@inheritDoc} */
	@Override
	public void notifyClientSideCallStarted(ClientSideCallContext context){
		
	}
	/** {@inheritDoc} */
	@Override
	public void notifyClientSideCallFinished(ClientSideCallContext context){
		
	}
	
	/** {@inheritDoc} */
	@Override
	public void notifyServerSideCallStarted(ServerSideCallContext context){
		
	}
	/** {@inheritDoc} */
	@Override
	public void notifyServerSideCallFinished(ServerSideCallContext context){
		
	}

	/** {@inheritDoc} */
	@Override
	public void customize(String parameter) {
	}

}
