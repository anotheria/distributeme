package org.distributeme.annotation;

import org.distributeme.core.listener.ServerLifecycleListener;

import java.lang.annotation.*;


/**
 * This annotations advices generator to add the specified class as listener to the generated server.
 * @author lrosenberg
 *
 */
@Retention (RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface ServerListener {
	Class<? extends ServerLifecycleListener> listenerClass();
}
