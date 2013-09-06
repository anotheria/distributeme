package org.distributeme.generator.jsonrpc;

import java.io.IOException;
import java.io.PrintWriter;

import net.anotheria.util.StringUtils;

import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.TypeDeclaration;

/**
 * Generates constants class for rmi service distribution.
 * @author lrosenberg.
 *
 */
public class ConstantsGenerator extends AbstractJsonRpcGenerator implements JsonRpcGenerator{
    private final Filer filer;

    public ConstantsGenerator(final Filer filer) {
        this.filer = filer;
    }

    @Override
	public void generate(TypeDeclaration type) throws IOException{
		PrintWriter writer = filer.createSourceFile(getPackageName(type)+"."+getConstantsName(type));
		setWriter(writer);
		
		
		writePackage(type);
		emptyline();
		
		writeString("public class "+getConstantsName(type)+"{");
		increaseIdent();
		emptyline();

		writeString("public static final String getServiceId(){");
		increaseIdent();
		writeStatement("return "+quote(StringUtils.replace(type.getQualifiedName(), '.', '_')));
		
		//writeStatement("this(new "+type.getAnnotation(DistributeMe.class).implName()+"())");
		closeBlock("getServiceId");
		
		closeBlock();
		
		
		writer.flush();
		writer.close();
	}
}
