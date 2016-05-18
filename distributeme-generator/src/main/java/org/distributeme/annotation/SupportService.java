package org.distributeme.annotation;

import java.lang.annotation.*;

/**
 * This annotations marks a service as support service. This is an instruction for the generator not to generate main method for this service.
 * @author lrosenberg
 *
 */
@Retention (RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface SupportService {

}
