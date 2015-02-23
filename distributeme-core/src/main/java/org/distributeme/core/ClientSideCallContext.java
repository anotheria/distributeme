package org.distributeme.core;

import java.util.List;

/**
 * This class represents a collection of infos available on the client side in the stub and stub-related code.
 * This information can be used for Routing or Failing strategies.
 * @author another
 *
 */
public class ClientSideCallContext extends AbstractCallContext{
	/**
	 * Number of repetitions of this call. The first call will have a callCount of zero. 
	 */
	private int callCount;
	
	public ClientSideCallContext(String aMethodName){
		super(aMethodName);
		callCount = 0;
	}
	
	public ClientSideCallContext(String aServiceId, String aMethodName, List<?> someParameters){
		super(aServiceId, aMethodName, someParameters);
		callCount = 0;
	}

	/**
	 * Returns the context call counter, which means how many attempts to call the server has been made.  
	 * @return
	 */
	public int getCallCount() {
		return callCount;
	}

	public void setCallCount(int callCount) {
		this.callCount = callCount;
	}


	public boolean isFirstCall(){
		return callCount==0;
	}
	
	public ClientSideCallContext increaseCallCount(){
		callCount++;
		return this;
	}
	
	@Override public String toString(){
		return getMethodName()+"("+getParameters()+") "+getCallCount()+" --> "+getServiceId();
	}

}
