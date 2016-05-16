package org.distributeme.annotation;

import net.anotheria.anoprise.metafactory.Service;

import java.lang.annotation.*;

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
