package org.distributeme.core.asynch;

import org.distributeme.core.exception.DistributemeRuntimeException;

/**
 * This exception is thrown whenever a call is aborted due timeout.
 * @author lrosenberg
 *
 */
public class CallTimeoutedException extends DistributemeRuntimeException{
	/**
	 * SerialversionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public CallTimeoutedException(){
		super("Call aborted due timeout");
	}
}	
