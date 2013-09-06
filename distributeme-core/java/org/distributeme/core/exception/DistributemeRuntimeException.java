package org.distributeme.core.exception;

/**
 * Base class for distributeme runtime exceptions.
 * @author lrosenberg
 *
 */
public class DistributemeRuntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DistributemeRuntimeException(String message){
		super(message);
	}
	
	public DistributemeRuntimeException(String message, Throwable cause){
		super(message, cause);
	}
}
