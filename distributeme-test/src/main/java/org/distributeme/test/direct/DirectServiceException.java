package org.distributeme.test.direct;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 25.01.16 00:33
 */
public class DirectServiceException extends Exception {
	public DirectServiceException(String message){
		super(message);
	}

	public DirectServiceException(String message, Throwable cause){
		super(message, cause);
	}
}
