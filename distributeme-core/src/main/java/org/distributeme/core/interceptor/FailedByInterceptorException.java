package org.distributeme.core.interceptor;

import org.distributeme.core.exception.DistributemeRuntimeException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.02.15 08:48
 */
public class FailedByInterceptorException extends DistributemeRuntimeException {
	public FailedByInterceptorException(){
		super("Interceptor forced call to fail");
	}
}
