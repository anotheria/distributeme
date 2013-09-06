package org.distributeme.core.exception;

/**
 * Exception that is used internally to signalize that a connection to the server couldn't be established.
 * @author another
 *
 */
public class NoConnectionToServerException extends Exception{
	/**
	 * SerialVersionUID. 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoConnectionToServerException(String message){
		super(message);
	}
	public NoConnectionToServerException(String message, Exception source){
		super(message, source);
	}
}
