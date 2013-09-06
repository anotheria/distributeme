package org.distributeme.generator;

import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.TypeDeclaration;
import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.moskito.core.dynamic.ProxyUtils;
import org.distributeme.annotation.DistributeMe;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Generator for RMI factory (stub factory).
 * @author lrosenberg
 */
public class RemoteFactoryGenerator extends AbstractGenerator implements Generator{

	@Override
	public void generate(TypeDeclaration type, Filer filer, Map<String,String> options) throws IOException{
		PrintWriter writer = filer.createSourceFile(getPackageName(type)+"."+getFactoryName(type));
		setWriter(writer);
		
		
		writePackage(type);
		writeAnalyzerComments(type);
		emptyline();
		writeImport(ServiceFactory.class);
		DistributeMe ann = type.getAnnotation(DistributeMe.class);
		if (ann.moskitoSupport()){
			writeImport(ProxyUtils.class);
		}
		emptyline();
		
		writeString("public class "+getFactoryName(type)+" implements ServiceFactory<"+type.getQualifiedName()+">{");
		increaseIdent();
		emptyline();

		writeString("public "+type.getQualifiedName()+" create(){");
		increaseIdent();
		writeStatement(type.getQualifiedName()+" instance = new "+getStubName(type)+"()");
		if (!ann.moskitoSupport()){
			writeStatement("return instance");
		}else{
			String name = type.getSimpleName()+"DiMe";
			writeStatement("return ProxyUtils.createServiceInstance(instance, "+quote(name)+", \"remote-service\", \"default\", "+getImplementedInterfacesAsString(type)+")");
		}
		closeBlock("create");
		closeBlock();
		
		
		writer.flush();
		writer.close();
	}
}
