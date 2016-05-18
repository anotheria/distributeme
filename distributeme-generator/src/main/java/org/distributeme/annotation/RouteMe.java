package org.distributeme.annotation;

import org.distributeme.core.routing.RegistrationNameProvider;

import java.lang.annotation.*;

/**
 * This annotation is needed by the Routing mechanism. The purpose of this annotation is to indicate to the server at runtime that is has to register himself under other name as default.
 * An example: @RouteMe(providerClass=PropertyBasedRegistrationNameProvider.class, providerParameter="instanceId") means that an instance of PropertyBasedRegistrationNameProvider will be created
 * at the start of the service, the value 'instanceId' will be provided as parameter to the above instance, and that the instance of PropertyBasedRegistrationNameProvider will be asked
 * under which name the service should be registered (which in this name means an extension to the natural service id). Example:
 * ./start.sh -DinstanceId=0 o.distributeme.test.failingandrr.generated.TestServer -> org_distributeme_test_failingandrr_TestService_0
 * ./start.sh -DinstanceId=1 o.distributeme.test.failingandrr.generated.TestServer -> org_distributeme_test_failingandrr_TestService_1
 * and so on.
 */
@Retention (RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface RouteMe {
	/**
	 * The registration name provider class, that is a class that defines under which name the service should register itself. 
	 * @return
	 */
	Class<? extends RegistrationNameProvider> providerClass();
	/**
	 * A parameter to the RegistrationNameProvider. 
	 * @return
	 */
	String providerParameter() default "";
}
