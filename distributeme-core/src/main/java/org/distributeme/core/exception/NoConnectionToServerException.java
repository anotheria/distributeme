package org.distributeme.core.exception;

/**
 * Exception that is used internally to signalize that a connection to the server couldn't be established.
 *
 * @author another
 * @version $Id: $Id
 */
public class NoConnectionToServerException extends Exception{
	/**
	 * SerialVersionUID. 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>Constructor for NoConnectionToServerException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public NoConnectionToServerException(String message){
		super(message);
	}
	/**
	 * <p>Constructor for NoConnectionToServerException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param source a {@link java.lang.Exception} object.
	 */
	public NoConnectionToServerException(String message, Exception source){
		super(message, source);
	}
}
