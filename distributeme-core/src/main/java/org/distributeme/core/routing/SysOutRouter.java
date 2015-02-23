package org.distributeme.core.routing;

import org.distributeme.core.ClientSideCallContext;


/**
 * This is an example implementation of a Router which actually do not route anything but simply prints out each call. Its usefull 
 * for debugging and demonstration purposes.
 * @author lrosenberg.
 *
 */
public class SysOutRouter implements Router{

	@Override
	public void customize(String parameter) {
	}

	@Override
	public String getServiceIdForCall(ClientSideCallContext callContext) {
		System.out.println("Router sees service call to "+callContext.getServiceId()+" method: "+callContext.getMethodName()+"("+callContext.getParameters()+")");
		return callContext.getServiceId();
	}
	
}
