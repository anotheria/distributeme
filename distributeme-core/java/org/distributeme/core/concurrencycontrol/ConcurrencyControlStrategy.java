package org.distributeme.core.concurrencycontrol;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.ServerSideCallContext;
/**
 * This interface allows the customer to control concurrent access to the services. 
 * @author lrosenberg
 */
public interface ConcurrencyControlStrategy {
	/**
	 * Called by the stub before the call is executed.
	 * @param context
	 */
	void notifyClientSideCallStarted(ClientSideCallContext context);
	/**
	 * Called by the stub after the call is finished.
	 * @param context
	 */
	void notifyClientSideCallFinished(ClientSideCallContext context);
	
	/**
	 * Called by the skeleton before the call is executed.
	 * @param context
	 */
	void notifyServerSideCallStarted(ServerSideCallContext context);
	
	/**
	 * Called by the skeleton after the call is finished.
	 * @param context
	 */
	void notifyServerSideCallFinished(ServerSideCallContext context);
	
	/**
	 * Called shortly after the initialization to customize this strategy according to the parameter in the annotation.
	 * @param parameter
	 */
	void customize(String parameter);

}
