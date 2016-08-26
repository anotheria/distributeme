package org.distributeme.core.concurrencycontrol;

/**
 * This exception is thrown if a server refuses to accept more incoming requests.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class ServerRefusedRequestException extends ConcurrencyControlException{
	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for ServerRefusedRequestException.</p>
	 */
	public ServerRefusedRequestException(){
		super("Server refused request due to overloading");
	}

	/**
	 * <p>Constructor for ServerRefusedRequestException.</p>
	 *
	 * @param currentRequests a int.
	 */
	public ServerRefusedRequestException(int currentRequests){
		super("Server refused request due to overloading, currently "+currentRequests+" requests pending.");
	}
}
