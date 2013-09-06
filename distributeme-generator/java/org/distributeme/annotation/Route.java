package org.distributeme.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.distributeme.core.routing.Router;

@Retention (RetentionPolicy.SOURCE)
@Target({ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface Route {
	/**
	 * Class implementing the router interface.
	 * @return
	 */
	Class<? extends Router> routerClass();
	/**
	 * Custom parameter which is used for proper router initialization/customization. Default value is empty string.
	 * @return
	 */
	String routerParameter() default  "";
}
