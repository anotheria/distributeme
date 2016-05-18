package org.distributeme.annotation;

import java.lang.annotation.*;

@Retention (RetentionPolicy.SOURCE)
@Target({ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface ConcurrencyControlClientSideLimit {
	/**
	 * Client side concurrent call limit.
	 * @return
	 */
	int value() default 0;

	/**
	 * Alternative configuration name, if this is used, value is ignored.
	 * @return
	 */
	String configurationName() default "";

}
