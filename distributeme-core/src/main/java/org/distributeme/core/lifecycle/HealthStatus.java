package org.distributeme.core.lifecycle;

import java.io.Serializable;

/**
 * HealthStatus of a service contains of a state and a message.
 *
 * @author lrosenberg
 * @version $Id: $Id
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
	 *
	 * @param aHealth a {@link org.distributeme.core.lifecycle.Health} object.
	 * @param aMessage a {@link java.lang.String} object.
	 */
	public HealthStatus(Health aHealth, String aMessage){
		health = aHealth;
		message = aMessage;
	}
	
	/**
	 * <p>Getter for the field <code>health</code>.</p>
	 *
	 * @return a {@link org.distributeme.core.lifecycle.Health} object.
	 */
	public Health getHealth(){
		return health;
	}
	
	/**
	 * <p>Getter for the field <code>message</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getMessage(){
		return message;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return getHealth()+" "+getMessage();
	}
	
	/**
	 * Factory method for default status OK.
	 *
	 * @return a {@link org.distributeme.core.lifecycle.HealthStatus} object.
	 */
	public static final HealthStatus OK(){
		return OK;
	}
}
