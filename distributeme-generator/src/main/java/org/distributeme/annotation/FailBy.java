package org.distributeme.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.distributeme.core.failing.FailingStrategy;

/**
 * This annotation configures custom failing behaviour.
 * @author lrosenberg
 *
 */
@Retention (RetentionPolicy.SOURCE)
@Target({ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface FailBy {
	/**
	 * Implementation of the FailingStrategy class which will control failing @ runtime.
	 * @return
	 */
	Class<? extends FailingStrategy> strategyClass();
	
	/**
	 * If true failover strategy will use the router instance. This is useful and possible when failing and routing is performing by the same class, for example RoundRobinRouterWithFailoverToNextNode.
	 * In this case the generated stub will reuse the previously created router instance for failover. Of course a router must be declated for the method or class annotated and the router must implemented
	 * FailingStrategy.
	 * @return
	 */
	boolean reuseRouter() default false;
}
