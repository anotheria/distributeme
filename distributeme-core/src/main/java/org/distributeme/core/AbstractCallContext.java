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
	@SuppressWarnings("rawtypes")
	private List parameters;
	/**
	 * Name of the called method.
	 */
	private String methodName;
	
	/**
	 * Writeable context. This is a place where different involved components can write and/or share information.
	 */
	@SuppressWarnings("rawtypes")
	private HashMap transportableCallContext = new HashMap();

	public AbstractCallContext(String aMethodName){
		methodName = aMethodName;
	}
	
	public AbstractCallContext(String aServiceId, String aMethodName, List<?> someParameters){
		serviceId = aServiceId;
		methodName = aMethodName;
		parameters = someParameters;
	}
	
	@SuppressWarnings("rawtypes")
	public List getParameters() {
		return parameters;
	}

	@SuppressWarnings("rawtypes")
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

	@SuppressWarnings("rawtypes")
	public HashMap getTransportableCallContext() {
		return transportableCallContext;
	}
	
	@SuppressWarnings("rawtypes")
	protected void setIncomingTransportableCallContext(Map map){
		transportableCallContext.putAll(map);
	}

	@Override public String toString(){
		return getMethodName()+"("+getParameters()+") --> "+getServiceId();
	}




}
