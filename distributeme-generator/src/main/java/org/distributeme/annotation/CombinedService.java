package org.distributeme.annotation;

import net.anotheria.anoprise.metafactory.Service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention (RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
/**
 * Indicates a combined service interface for services running together.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public @interface CombinedService {
	Class<? extends Service>[] services();
}
