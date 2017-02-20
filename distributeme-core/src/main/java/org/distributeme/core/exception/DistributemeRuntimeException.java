package org.distributeme.core.exception;

/**
 * Base class for distributeme runtime exceptions.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class DistributemeRuntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for DistributemeRuntimeException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public DistributemeRuntimeException(String message){
		super(message);
	}
	
	/**
	 * <p>Constructor for DistributemeRuntimeException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public DistributemeRuntimeException(String message, Throwable cause){
		super(message, cause);
	}
}
