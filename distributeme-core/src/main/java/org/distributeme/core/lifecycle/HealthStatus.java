package org.distributeme.core.lifecycle;

import java.io.Serializable;

/**
 * HealthStatus of a service contains of a state and a message.
 * @author lrosenberg
 *
 */
public class HealthStatus implements Serializable {
	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Health state.
	 */
	private Health health;
	/**
	 * Custom (info/debug) message.
	 */
	private String message;
	/**
	 * Constant for status OK.
	 */
	private static final HealthStatus OK = new HealthStatus(Health.GREEN, "ok");
	/**
	 * Creates a new health status.
	 * @param aHealth
	 * @param aMessage
	 */
	public HealthStatus(Health aHealth, String aMessage){
		health = aHealth;
		message = aMessage;
	}
	
	public Health getHealth(){
		return health;
	}
	
	public String getMessage(){
		return message;
	}
	
	@Override public String toString(){
        return health +" "+ message;
	}
	
	/**
	 * Factory method for default status OK.
	 * @return
	 */
	public static final HealthStatus OK(){
		return OK;
	}
}
