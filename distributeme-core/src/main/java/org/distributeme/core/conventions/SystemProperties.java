package org.distributeme.core.conventions;

/**
 * Helper class for accessing and accounting for supported system properties. 
 */
public enum SystemProperties {
	/**
	 * SkipCentralRegistry registration, default false.
	 */
	SKIP_CENTRAL_REGISTRY("skipCentralRegistry", "false"),
	/**
	 * Set custom rmi registration port.
	 */
	LOCAL_RMI_REGISTRY_PORT("localRmiRegistryPort", null),
	/**
	 * Host where the registry is running.
	 */
	CENTRAL_REGISTRY_HOST("registryContainerHost", null),
	/**
	 * Port where the registry is running.
	 */
	CENTRAL_REGISTRY_PORT("registryContainerPort", "9229"),
	/**
	 * Minimal port to use for the local rmi registry.
	 */
	LOCAL_RMI_REGISTRY_MIN_PORT("localRmiRegistryMinPort", "9250"),
	/**
	 * Max port to use for the local rmi registry.
	 */
	LOCAL_RMI_REGISTRY_MAX_PORT("localRmiRegistryMaxPort", "9299"),
	/**
	 * Port to which the services should be bound.
	 */
	SERVICE_BINDING_PORT("serviceBindingPort", "0");
	
	/**
	 * Name of the property.
	 */
	private String propertyName;
	/**
	 * Default value.
	 */
	private String defaultValue;
	
	/**
	 * Creates a new system properties.
	 * @param aPropertyName
	 * @param aDefaultValue
	 */
	private SystemProperties(String aPropertyName, String aDefaultValue){
		propertyName = aPropertyName;
		defaultValue = aDefaultValue;
	}
	
	/**
	 * Returns true if the property is set.
	 * @return
	 */
	public boolean isSet(){
		return get()!=null;
	}
	
	
	/**
	 * Returns the set value.
	 * @return
	 */
	public String get(){
		return System.getProperty(propertyName, defaultValue);
	}
	
	/**
	 * Returns the system property value as boolean.
	 * @return
	 */
	public boolean getAsBoolean(){
		return Boolean.parseBoolean(get());
	}
	
	/**
	 * Returns the system property value as int.
	 * @return
	 */
	public int getAsInt(){
		return Integer.parseInt(get());
	}
}
