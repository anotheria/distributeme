package org.distributeme.generator.jsonrpc;

import com.sun.mirror.declaration.TypeDeclaration;

import java.io.IOException;

/**
 * Generator interface.
 * @author lrosenberg
 */
public interface JsonRpcGenerator {
	void generate(TypeDeclaration type) throws IOException;
}
