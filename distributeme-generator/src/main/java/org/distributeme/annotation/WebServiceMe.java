package org.distributeme.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.anotheria.anoprise.metafactory.Extension;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface WebServiceMe {
	Extension extension() default Extension.LOCAL;

	String[] initcode() default {};

	boolean moskitoSupport() default true;
}
