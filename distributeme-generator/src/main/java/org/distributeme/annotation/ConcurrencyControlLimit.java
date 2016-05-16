package org.distributeme.annotation;

import java.lang.annotation.*;
/**
 * This annotation configures easy to use version of the concurrency control by specifying hard call limits on both client and server side.
 * @author lrosenberg
 *
 */
@Retention (RetentionPolicy.SOURCE)
@Target({ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface ConcurrencyControlLimit {
	/**
	 * Client side concurrent call limit.
	 * @return
	 */
	int client() default 0;
	/**
	 * Server side concurrent call limit.
	 * @return
	 */
	int server() default 0;

	/**
	 * Alternative configuration name, if this is used, value is ignored.
	 * @return
	 */
	String configurationName() default "";

}
