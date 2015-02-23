package org.distributeme.core.interceptor;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.02.15 08:48
 */
public class FailedByInterceptorException extends RuntimeException {
	public FailedByInterceptorException(){
		super("Interceptor forced call to fail");
	}
}
