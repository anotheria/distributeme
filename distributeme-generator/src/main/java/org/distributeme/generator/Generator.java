package org.distributeme.generator;

import java.io.IOException;
import java.util.Map;

import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.TypeDeclaration;
/**
 * Generator interface.
 * @author lrosenberg
 */
public interface Generator {
	void generate(TypeDeclaration type, Filer filer, Map<String,String> options) throws IOException;
}
