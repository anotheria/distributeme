package org.distributeme.core;

import org.distributeme.core.routing.Constants;

import java.util.List;

/**
 * This class represents a collection of infos available on the client side in the stub and stub-related code.
 * This information can be used for Routing or Failing strategies.
 *
 * @author another
 * @version $Id: $Id
 */
public class ClientSideCallContext extends AbstractCallContext{

	/**
	 * Number of repetitions of this call. The first call will have a callCount of zero. 
	 */
	private int callCount;
	
	/**
	 * <p>Constructor for ClientSideCallContext.</p>
	 *
	 * @param aMethodName a {@link java.lang.String} object.
	 */
	public ClientSideCallContext(String aMethodName){
		super(aMethodName);
		callCount = 0;
	}
	
	/**
	 * <p>Constructor for ClientSideCallContext.</p>
	 *
	 * @param aServiceId a {@link java.lang.String} object.
	 * @param aMethodName a {@link java.lang.String} object.
	 * @param someParameters a {@link java.util.List} object.
	 */
	public ClientSideCallContext(String aServiceId, String aMethodName, List<?> someParameters){
		super(aServiceId, aMethodName, someParameters);
		callCount = 0;
	}

	/**
	 * Returns the context call counter, which means how many attempts to call the server has been made.
	 *
	 * @return a int.
	 */
	public int getCallCount() {
		return callCount;
	}

	/**
	 * <p>Setter for the field <code>callCount</code>.</p>
	 *
	 * @param callCount a int.
	 */
	public void setCallCount(int callCount) {
		this.callCount = callCount;
		getTransportableCallContext().put(Constants.ATT_CALL_COUNT, callCount);
	}


	/**
	 * <p>isFirstCall.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isFirstCall(){
		return callCount==0;
	}
	
	/**
	 * <p>increaseCallCount.</p>
	 *
	 * @return a {@link org.distributeme.core.ClientSideCallContext} object.
	 */
	public ClientSideCallContext increaseCallCount(){
		callCount++;
		getTransportableCallContext().put(Constants.ATT_CALL_COUNT, callCount);
		return this;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return getMethodName()+"("+getParameters()+") "+getCallCount()+" --> "+getServiceId();
	}

}
