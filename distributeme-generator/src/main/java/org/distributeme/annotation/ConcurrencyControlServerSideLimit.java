package org.distributeme.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>ConcurrencyControlServerSideLimit class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
@Retention (RetentionPolicy.SOURCE)
@Target({ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface ConcurrencyControlServerSideLimit {
	/**
	 * Server side concurrent call limit.
	 * @return
	 */
	int value() default 0;

	/**
	 * Alternative configuration name, if this is used, value is ignored.
	 * @return
	 */
	String configurationName() default "";
}
