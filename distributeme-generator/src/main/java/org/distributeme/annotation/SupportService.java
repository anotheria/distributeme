package org.distributeme.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotations marks a service as support service. This is an instruction for the generator not to generate main method for this service.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
@Retention (RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface SupportService {

}
