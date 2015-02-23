package org.distributeme.core.concurrencycontrol;

/**
 * This exception is thrown by the stub if client side concurrency control prohibits further outgoing connections to the server.
 * @author lrosenberg
 *
 */
public class OutgoingRequestRefusedException extends ConcurrencyControlException{
	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	public OutgoingRequestRefusedException(){
		super("No more requests to this server allowed by this stub");
	}
}
