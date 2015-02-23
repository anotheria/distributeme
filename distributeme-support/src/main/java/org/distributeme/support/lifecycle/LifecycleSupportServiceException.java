package org.distributeme.support.lifecycle;

/**
 * Base exception class for all LifecycleSupportService related exceptions.
 * @author another
 *
 */
public class LifecycleSupportServiceException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public LifecycleSupportServiceException(String message){
		super(message);
	}
}
