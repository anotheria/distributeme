package org.distributeme.generator;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.moskito.core.dynamic.ProxyUtils;
import org.distributeme.annotation.DistributeMe;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Generator for RMI factory (stub factory).
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class RemoteFactoryGenerator extends AbstractGenerator implements Generator{

	/**
	 * <p>Constructor for RemoteFactoryGenerator.</p>
	 *
	 * @param environment a {@link javax.annotation.processing.ProcessingEnvironment} object.
	 */
	public RemoteFactoryGenerator(ProcessingEnvironment environment) {
		super(environment);
	}

	/** {@inheritDoc} */
	@Override
	public void generate(TypeElement type, Filer filer, Map<String,String> options) throws IOException{
		JavaFileObject sourceFile = filer.createSourceFile(getPackageName(type)+"."+getFactoryName(type));
		PrintWriter writer = new PrintWriter(sourceFile.openWriter());
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

		writeAnalyzeIgnoreAnnotation(type);
		writeString("public class "+getFactoryName(type)+" implements ServiceFactory<"+type.getQualifiedName()+">{");
		increaseIdent();
		emptyline();

		writeString("public "+type.getQualifiedName()+" create(){");
		increaseIdent();
		writeStatement(type.getQualifiedName()+" instance = new "+getStubName(type)+"()");
		if (!ann.moskitoSupport()){
			writeStatement("return instance");
		}else{
			String name = type.getSimpleName().toString()+"DiMe";
			writeStatement("return ProxyUtils.createServiceInstance(instance, "+quote(name)+", \"remote-service\", \"default\", "+getImplementedInterfacesAsString(type)+")");
		}
		closeBlock("create");
		closeBlock();
		
		
		writer.flush();
		writer.close();
	}
}
