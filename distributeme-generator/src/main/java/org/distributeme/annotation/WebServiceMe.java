package org.distributeme.annotation;

import net.anotheria.anoprise.metafactory.Extension;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface WebServiceMe {
	Extension extension() default Extension.LOCAL;

	String[] initcode() default {};

	boolean moskitoSupport() default true;
}
