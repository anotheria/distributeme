package org.distributeme.generator;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Map;

/**
 * Generator interface.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public interface Generator {
	/**
	 * <p>generate.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @param filer a {@link javax.annotation.processing.Filer} object.
	 * @param options a {@link java.util.Map} object.
	 * @throws java.io.IOException if any.
	 */
	void generate(TypeElement type, Filer filer, Map<String,String> options) throws IOException;
}
