package org.distributeme.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for client/server call contextes. This class is actually for internal use only. However, due to convenience to use this class in an interceptor
 * if you use the same interceptor on client/server side, it has been made public. 
 * @author lrosenberg
 *
 */
public abstract class AbstractCallContext {
	/**
	 * Id of the service associated with this call. This can be altered by one of the strategies.
	 */
	private String serviceId;
	/**
	 * Parameters to the call if set.
	 */
	private List parameters;
	/**
	 * Name of the called method.
	 */
	private String methodName;
	
	/**
	 * Writeable context. This is a place where different involved components can write and/or share information.
	 */
	private HashMap transportableCallContext = new HashMap();

	public AbstractCallContext(String aMethodName){
		methodName = aMethodName;
		currentCallContext.set(this);
	}
	
	public AbstractCallContext(String aServiceId, String aMethodName, List<?> someParameters){
		serviceId = aServiceId;
		methodName = aMethodName;
		parameters = someParameters;
		currentCallContext.set(this);
	}

	public List getParameters() {
		return parameters;
	}

	public void setParameters(List parameters) {
		this.parameters = parameters;
	}
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public HashMap getTransportableCallContext() {
		return transportableCallContext;
	}
	
	protected void setIncomingTransportableCallContext(Map map){
		transportableCallContext.putAll(map);
	}

	@Override public String toString(){
		return methodName + '(' + parameters +") --> "+ serviceId;
	}

	private static ThreadLocal<AbstractCallContext> currentCallContext = new ThreadLocal<>();

	public static HashMap getCurrentTransportableCallContext(){
		AbstractCallContext currentContext = currentCallContext.get();
		if (currentContext == null)
			return null;
		return currentContext.transportableCallContext;

	}




}
