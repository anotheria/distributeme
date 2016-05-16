package org.distributeme.annotation;

import java.lang.annotation.*;

/**
 * This annotation marks a method as not being routed. This is useful if you want to exclude a single method from the routing of a class.
 */
@Retention (RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Documented
public @interface DontRoute {

}
