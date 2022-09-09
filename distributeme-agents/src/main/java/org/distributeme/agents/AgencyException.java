package org.distributeme.agents;

/**
 * Base exception class for agency operations.
 * @author lrosenberg
 */
public class AgencyException extends Exception{
	/**
	 *Serial Version UID. 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new agency exception.
	 * @param message
	 */
	public AgencyException(String message){
		super(message);
	}

	/**
	 * Creates new agency exception.
	 * @param message
	 * @param cause
	 */
	public AgencyException(String message, Exception cause){
		super(message, cause);
	}
}
