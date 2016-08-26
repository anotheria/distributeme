package org.distributeme.annotation;

import net.anotheria.anoprise.metafactory.Extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>WebServiceMe class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface WebServiceMe {
	Extension extension() default Extension.LOCAL;

	String[] initcode() default {};

	boolean moskitoSupport() default true;
}
