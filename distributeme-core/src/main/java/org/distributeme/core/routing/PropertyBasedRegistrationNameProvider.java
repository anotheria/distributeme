package org.distributeme.core.routing;

/**
 * This registration name provider is based on a submitted property to the start.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class PropertyBasedRegistrationNameProvider implements RegistrationNameProvider{
	/**
	 * Name of the property.
	 */
	private String propertyName;
	/**
	 * Value of the property.
	 */
	private String propertyValue;
	
	/** {@inheritDoc} */
	@Override public String getRegistrationName(String serviceId) {
		return propertyValue!=null && propertyValue.length()>0 ? 
				serviceId + "_"+propertyValue : serviceId;
	}

	/** {@inheritDoc} */
	@Override public void customize(String parameter){
		propertyName = parameter;
		propertyValue = System.getProperty(propertyName);
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return "PropertyBasedRegistrationNameProvider "+ propertyName+"="+propertyValue;
	}
	
}
