package org.distributeme.generator.jsonrpc;

import com.sun.mirror.declaration.TypeDeclaration;
import org.distributeme.generator.AbstractStubGenerator;

/**
 * Base generator class.
 *
 * @author lrosenberg
 */
public abstract class AbstractJsonRpcGenerator extends AbstractStubGenerator {
    protected String getServerInterfaceName(final TypeDeclaration type) {
        return "Client" + type.getSimpleName();
    }

    /**
     * Get client stub implementation class name.
     *
     * @param type generated type
     * @return class name
     */
    protected String getClientStubName(final TypeDeclaration type) {
        return "Client" + type.getSimpleName() + "Impl";
    }


    protected static String getClientStubFactoryName(TypeDeclaration type) {
        return "Client" + type.getSimpleName() + "Factory";
    }

    protected String getInterfaceFullName(final TypeDeclaration type) {
        return type.getQualifiedName();
    }

    protected static String getPackageName(TypeDeclaration type) {
        return type.getPackage().getQualifiedName() + ".jsonrpc.generated";
    }

    protected void writePackage(TypeDeclaration type) {
        writeString("package " + getPackageName(type) + ";");
    }

    protected String getServerImplName(TypeDeclaration type) {
        return "Server"  + type.getSimpleName() + "Impl";
    }
}
