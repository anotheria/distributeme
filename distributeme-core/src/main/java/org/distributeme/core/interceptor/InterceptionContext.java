package org.distributeme.core.interceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * The interception context indicates current progress and phase of the interception and also allows to store some local information to this call.
 * @author lrosenberg
 *
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
	
	public InterceptionPhase getCurrentPhase() {
		return currentPhase;
	}
	public void setCurrentPhase(InterceptionPhase currentPhase) {
		this.currentPhase = currentPhase;
	}
	public Object getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	public Map getLocalStore(){
		return localStore;
	}

	@Override public String toString(){
		return "Phase: "+currentPhase+", returnValue: "+returnValue+", localStore: "+localStore;
	}
	
}
