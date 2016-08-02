package org.distributeme.generator;

import org.distributeme.annotation.DistributeMe;
import org.distributeme.core.asynch.CallBackHandler;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;



/**
 * Generator for RMI based remote interface. 
 * @author lrosenberg
 */
public class AsynchInterfaceGenerator extends AbstractGenerator implements Generator{

	public AsynchInterfaceGenerator(ProcessingEnvironment environment) {
		super(environment);
	}

	@Override
	public void generate(TypeElement type, Filer filer, Map<String,String> options) throws IOException{
		DistributeMe typeAnnotation = type.getAnnotation(DistributeMe.class);
		if (!typeAnnotation.asynchSupport())
			return;

		JavaFileObject sourceFile = filer.createSourceFile(getPackageName(type) + '.' + getAsynchInterfaceName(type));
		PrintWriter writer = new PrintWriter(sourceFile.openWriter());
		setWriter(writer);
		
		
		writePackage(type);
		writeAnalyzerComments(type);
		writeImport(CallBackHandler.class);
		emptyline();
		
		writeString("public interface "+getAsynchInterfaceName(type)+" extends "+type.getQualifiedName()+", org.distributeme.core.asynch.AsynchStub{");
		increaseIdent();
		
		Collection<? extends ExecutableElement> methods = getAllDeclaredMethods(type);
		for (ExecutableElement method : methods){
			String methodDecl = getAsynchInterfaceMethodDeclaration(method);
			writeStatement(methodDecl);
			emptyline(); 
		}
		
		closeBlockNEW();
		
		writer.flush();
		writer.close();
	}
	
	
}
