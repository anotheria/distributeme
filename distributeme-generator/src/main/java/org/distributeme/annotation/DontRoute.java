package org.distributeme.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a method as not being routed. This is useful if you want to exclude a single method from the routing of a class.
 *
 * @author another
 * @version $Id: $Id
 */
@Retention (RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Documented
public @interface DontRoute {

}
