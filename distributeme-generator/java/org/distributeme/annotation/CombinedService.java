package org.distributeme.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.anotheria.anoprise.metafactory.Service;

@Retention (RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
/**
 * Indicates a combined service interface for services running together.
 * @author lrosenberg
 *
 */
public @interface CombinedService {
	Class<? extends Service>[] services();
}
