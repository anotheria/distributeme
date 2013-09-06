package org.distributeme.core.concurrencycontrol;

import org.distributeme.core.exception.DistributemeRuntimeException;

/**
 * Exception thrown if concurrency control decides that the amount of acceptable requests is reached and no further requests can pass through.
 * @author lrosenberg
 *
 */
public class ConcurrencyControlException extends DistributemeRuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConcurrencyControlException(String message){
		super(message);
	}
	
	public ConcurrencyControlException(String message, Throwable cause){
		super(message, cause);
	}

}
