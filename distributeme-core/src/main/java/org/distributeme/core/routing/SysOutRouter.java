package org.distributeme.core.routing;

import org.distributeme.core.ClientSideCallContext;


/**
 * This is an example implementation of a Router which actually do not route anything but simply prints out each call. Its usefull
 * for debugging and demonstration purposes.
 *
 * @author lrosenberg.
 * @version $Id: $Id
 */
public class SysOutRouter extends AbstractRouter implements Router{

	/** {@inheritDoc} */
	@Override
	public void customize(String parameter) {
	}

	/** {@inheritDoc} */
	@Override
	public String getServiceIdForCall(ClientSideCallContext callContext) {
		System.out.println("Router sees service call to "+callContext.getServiceId()+" method: "+callContext.getMethodName()+"("+callContext.getParameters()+")");
		return callContext.getServiceId();
	}
	
}
