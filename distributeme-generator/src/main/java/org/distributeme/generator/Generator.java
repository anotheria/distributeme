package org.distributeme.generator;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Map;

/**
 * Generator interface.
 * @author lrosenberg
 */
public interface Generator {
	void generate(TypeElement type, Filer filer, Map<String,String> options) throws IOException;
}
