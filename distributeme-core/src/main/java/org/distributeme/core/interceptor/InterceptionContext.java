package org.distributeme.core.interceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * The interception context indicates current progress and phase of the interception and also allows to store some local information to this call.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class InterceptionContext {
	/**
	 * What is the current interception phase. Useful if the interceptor is configured to act in multiple phases.
	 */
	private InterceptionPhase currentPhase;
	/**
	 * Return value if already known.
	 */
	private Object returnValue;
	/**
	 * Exception if already thrown.
	 */
	private Exception exception;
	/**
	 * A store for interceptors to communicate with each other and to store data between phases.
	 */
	private Map localStore = new HashMap();
	
	/**
	 * <p>Getter for the field <code>currentPhase</code>.</p>
	 *
	 * @return a {@link org.distributeme.core.interceptor.InterceptionPhase} object.
	 */
	public InterceptionPhase getCurrentPhase() {
		return currentPhase;
	}
	/**
	 * <p>Setter for the field <code>currentPhase</code>.</p>
	 *
	 * @param currentPhase a {@link org.distributeme.core.interceptor.InterceptionPhase} object.
	 */
	public void setCurrentPhase(InterceptionPhase currentPhase) {
		this.currentPhase = currentPhase;
	}
	/**
	 * <p>Getter for the field <code>returnValue</code>.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getReturnValue() {
		return returnValue;
	}
	/**
	 * <p>Setter for the field <code>returnValue</code>.</p>
	 *
	 * @param returnValue a {@link java.lang.Object} object.
	 */
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
	/**
	 * <p>Getter for the field <code>exception</code>.</p>
	 *
	 * @return a {@link java.lang.Exception} object.
	 */
	public Exception getException() {
		return exception;
	}
	/**
	 * <p>Setter for the field <code>exception</code>.</p>
	 *
	 * @param exception a {@link java.lang.Exception} object.
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	/**
	 * <p>Getter for the field <code>localStore</code>.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map getLocalStore(){
		return localStore;
	}

	/** {@inheritDoc} */
	@Override public String toString(){
		return "Phase: "+currentPhase+", returnValue: "+returnValue+", localStore: "+localStore;
	}
	
}
