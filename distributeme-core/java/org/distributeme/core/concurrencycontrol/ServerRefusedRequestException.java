package org.distributeme.core.concurrencycontrol;

/**
 * This exception is thrown if a server refuses to accept more incoming requests.
 * @author lrosenberg
 *
 */
public class ServerRefusedRequestException extends ConcurrencyControlException{
	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	public ServerRefusedRequestException(){
		super("Server refused request due to overloading");
	}

	public ServerRefusedRequestException(int currentRequests){
		super("Server refused request due to overloading, currently "+currentRequests+" requests pending.");
	}
}
