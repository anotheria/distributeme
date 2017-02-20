package org.distributeme.core.routing;

/**
 * Describes a registration name provider which defines the name useable for registration.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public interface RegistrationNameProvider {
	/**
	 * Returns the registration name for this service instance.
	 *
	 * @param serviceId a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	String getRegistrationName(String serviceId);
	
	/**
	 * Called shortly after the initialization to customize this registration name provider according to the parameter in the annotation.
	 *
	 * @param parameter a {@link java.lang.String} object.
	 */
	void customize(String parameter);

}
