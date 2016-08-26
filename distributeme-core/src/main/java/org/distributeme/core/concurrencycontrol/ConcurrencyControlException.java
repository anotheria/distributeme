package org.distributeme.core.concurrencycontrol;

import org.distributeme.core.exception.DistributemeRuntimeException;

/**
 * Exception thrown if concurrency control decides that the amount of acceptable requests is reached and no further requests can pass through.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class ConcurrencyControlException extends DistributemeRuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for ConcurrencyControlException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public ConcurrencyControlException(String message){
		super(message);
	}
	
	/**
	 * <p>Constructor for ConcurrencyControlException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public ConcurrencyControlException(String message, Throwable cause){
		super(message, cause);
	}

}
