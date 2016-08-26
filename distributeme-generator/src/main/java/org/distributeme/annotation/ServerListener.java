package org.distributeme.annotation;

import org.distributeme.core.listener.ServerLifecycleListener;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotations advices generator to add the specified class as listener to the generated server.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
@Retention (RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface ServerListener {
	Class<? extends ServerLifecycleListener> listenerClass();
}
