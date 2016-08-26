package org.distributeme.core.concurrencycontrol;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.ServerSideCallContext;
/**
 * This interface allows the customer to control concurrent access to the services.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public interface ConcurrencyControlStrategy {
	/**
	 * Called by the stub before the call is executed.
	 *
	 * @param context a {@link org.distributeme.core.ClientSideCallContext} object.
	 */
	void notifyClientSideCallStarted(ClientSideCallContext context);
	/**
	 * Called by the stub after the call is finished.
	 *
	 * @param context a {@link org.distributeme.core.ClientSideCallContext} object.
	 */
	void notifyClientSideCallFinished(ClientSideCallContext context);
	
	/**
	 * Called by the skeleton before the call is executed.
	 *
	 * @param context a {@link org.distributeme.core.ServerSideCallContext} object.
	 */
	void notifyServerSideCallStarted(ServerSideCallContext context);
	
	/**
	 * Called by the skeleton after the call is finished.
	 *
	 * @param context a {@link org.distributeme.core.ServerSideCallContext} object.
	 */
	void notifyServerSideCallFinished(ServerSideCallContext context);
	
	/**
	 * Called shortly after the initialization to customize this strategy according to the parameter in the annotation.
	 *
	 * @param parameter a {@link java.lang.String} object.
	 */
	void customize(String parameter);

}
