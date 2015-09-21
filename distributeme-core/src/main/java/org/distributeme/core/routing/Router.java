package org.distributeme.core.routing;

import org.distributeme.core.ClientSideCallContext;
/**
 * This interface defines a call router, which influences how calls are distributed to different servers in the same system.
 * @see https://confluence.opensource.anotheria.net/display/DISTRIBUTEME/Routing+and+Failing+Strategies
 * @see https://confluence.opensource.anotheria.net/display/DISTRIBUTEME/Routing
 * @author lrosenberg
 *
 */
public interface Router {
	/**
	 * Returns the serviceId for this particular call to route to.
	 * @param callContext context of the call with all call related data.
	 * @return
	 */
	String getServiceIdForCall(ClientSideCallContext callContext);
	
	/**
	 * Called shortly after the initialization to customize this router according to the parameter in the annotation.
	 * @param serviceId id of the service we are routing.
	 * @param parameter value of the annotation parameter.
	 */
	void customize(String serviceId, String parameter);
}
