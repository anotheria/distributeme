package org.distributeme.core;
/**
 * Constants class for holding constants used for transmission of parameters to VM that distributeme understands.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class SystemPropertyNames {
	/**
	 * This is the prefix for all parameters.
	 */
	public static final String PREFIX = "dime";
	/**
	 * Registration name provider allows to customize the registration name of the instance. This is useful for failing/routing.
	 */
	public static final String REGISTRATION_NAME_PROVIDER = PREFIX+"RegistrationNameProvider";
}
