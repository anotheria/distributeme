package org.distributeme.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for client/server call contextes. This class is actually for internal use only. However, due to convenience to use this class in an interceptor
 * if you use the same interceptor on client/server side, it has been made public.
 *
 * @author lrosenberg
 * @version $Id: $Id
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

	/**
	 * <p>Constructor for AbstractCallContext.</p>
	 *
	 * @param aMethodName a {@link java.lang.String} object.
	 */
	public AbstractCallContext(String aMethodName){
		methodName = aMethodName;
		currentCallContext.set(this);
	}
	
	/**
	 * <p>Constructor for AbstractCallContext.</p>
	 *
	 * @param aServiceId a {@link java.lang.String} object.
	 * @param aMethodName a {@link java.lang.String} object.
	 * @param someParameters a {@link java.util.List} object.
	 */
	public AbstractCallContext(String aServiceId, String aMethodName, List<?> someParameters){
		serviceId = aServiceId;
		methodName = aMethodName;
		parameters = someParameters;
		currentCallContext.set(this);
	}

	/**
	 * <p>Getter for the field <code>parameters</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	@SuppressWarnings("rawtypes")
	public List getParameters() {
		return parameters;
	}

	/**
	 * <p>Setter for the field <code>parameters</code>.</p>
	 *
	 * @param parameters a {@link java.util.List} object.
	 */
	@SuppressWarnings("rawtypes")
	public void setParameters(List parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * <p>Getter for the field <code>methodName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * <p>Setter for the field <code>methodName</code>.</p>
	 *
	 * @param methodName a {@link java.lang.String} object.
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * <p>Getter for the field <code>serviceId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getServiceId() {
		return serviceId;
	}
	/**
	 * <p>Setter for the field <code>serviceId</code>.</p>
	 *
	 * @param serviceId a {@link java.lang.String} object.
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * <p>Getter for the field <code>transportableCallContext</code>.</p>
	 *
	 * @return a {@link java.util.HashMap} object.
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getTransportableCallContext() {
		return transportableCallContext;
	}
	
	/**
	 * <p>setIncomingTransportableCallContext.</p>
	 *
	 * @param map a {@link java.util.Map} object.
	 */
	@SuppressWarnings("rawtypes")
	protected void setIncomingTransportableCallContext(Map map){
		transportableCallContext.putAll(map);
	}

	/** {@inheritDoc} */
	@Override public String toString(){
		return getMethodName()+"("+getParameters()+") --> "+getServiceId();
	}

	/**
	 * CurrentCallContext - ThreadLocal copy of the context.
	 */
	private static ThreadLocal<AbstractCallContext> currentCallContext = new ThreadLocal<AbstractCallContext>();

	/**
	 * <p>getCurrentTransportableCallContext.</p>
	 *
	 * @return a {@link java.util.HashMap} object.
	 */
	public static HashMap getCurrentTransportableCallContext(){
		AbstractCallContext currentContext = currentCallContext.get();
		if (currentContext == null)
			return null;
		return currentContext.getTransportableCallContext();

	}




}
