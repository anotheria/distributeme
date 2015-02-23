package org.distributeme.generator.jsonrpc;

import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.TypeDeclaration;
import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.moskito.core.dynamic.ProxyUtils;
import org.distributeme.annotation.DistributeMe;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Generator for RMI factory (stub factory).
 *
 * @author lrosenberg
 */
public class ClientFactoryGenerator extends AbstractJsonRpcGenerator implements JsonRpcGenerator {
    private final Filer filer;

    public ClientFactoryGenerator(final Filer filer) {
        this.filer = filer;
    }

    @Override
    public void generate(TypeDeclaration type) throws IOException {
        PrintWriter writer = filer.createSourceFile(getPackageName(type) + "." + getClientStubFactoryName(type));
        setWriter(writer);


        writePackage(type);
        emptyline();
        writeImport(ServiceFactory.class);
        DistributeMe ann = type.getAnnotation(DistributeMe.class);
        if (ann.moskitoSupport()) {
            writeImport(ProxyUtils.class);
        }
        emptyline();

        writeString("public class " + getClientStubFactoryName(type) + " implements ServiceFactory<" + type.getQualifiedName() + ">{");
        increaseIdent();
        emptyline();

        writeString("public " + type.getQualifiedName() + " create(){");
        increaseIdent();
        writeStatement(type.getQualifiedName() + " instance = new " + getClientStubName(type) + "(null)");
        if (!ann.moskitoSupport()) {
            writeStatement("return instance");
        } else {
            String name = type.getSimpleName() + "DiMe";
            writeStatement("return ProxyUtils.createServiceInstance(instance, " + quote(name) + ", \"remote-service\", \"default\", " + getImplementedInterfacesAsString(type) + ")");
        }
        closeBlock("create");
        closeBlock();


        writer.flush();
        writer.close();
    }
}
