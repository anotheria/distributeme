package org.distributeme.core.interceptor;
/**
 * Describes various interception phases during a call.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public enum InterceptionPhase {
	/**
	 * Before the servant is called by the skeleton on the server side.
	 */
	BEFORE_SERVANT_CALL, 
	/**
	 * Before the service is called by the stub on the client side.
	 */
	BEFORE_SERVICE_CALL, 
	/**
	 * After the service is called by the stub on the client side.
	 */
	AFTER_SERVICE_CALL, 
	/**
	 * After the servant is called by the skeleton on the server side.
	 */
	AFTER_SERVANT_CALL
}
