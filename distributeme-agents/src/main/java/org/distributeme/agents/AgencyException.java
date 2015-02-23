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
	
	public AgencyException(String message){
		super(message);
	}
	public AgencyException(String message, Exception cause){
		super(message, cause);
	}
}
