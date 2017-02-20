package org.distributeme.core.exception;

/**
 * Thrown if no connection to a remote service could be established.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class ServiceUnavailableException extends DistributemeRuntimeException{
	/**
	 * Serial version uid.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Creates new instance of the exception.
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public ServiceUnavailableException(String message){
		super(message);
	}
	/**
	 * Creates new instance of the exception.
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Exception} object.
	 */
	public ServiceUnavailableException(String message, Exception cause){
		super(message, cause);
	}
}
