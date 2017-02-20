package org.distributeme.annotation;

import org.distributeme.core.concurrencycontrol.ConcurrencyControlStrategy;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>ConcurrencyControl class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
@Retention (RetentionPolicy.SOURCE)
@Target({ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface ConcurrencyControl {
	/**
	 * Class that implements ConcurrencyControlStategy and should be used at runtime.
	 * @return
	 */
	Class<? extends ConcurrencyControlStrategy> strategyClass();
	/**
	 * Custom parameter which is used for proper router initialization/customization. Default value is empty string.
	 * @return
	 */
	String strategyParameter() default  "";

}
