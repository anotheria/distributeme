package org.distributeme.generator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

import org.distributeme.annotation.DistributeMe;
import org.distributeme.core.asynch.CallBackHandler;

import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;


/**
 * Generator for RMI based remote interface. 
 * @author lrosenberg
 */
public class AsynchInterfaceGenerator extends AbstractGenerator implements Generator{

	@Override
	public void generate(TypeDeclaration type, Filer filer, Map<String,String> options) throws IOException{
		DistributeMe typeAnnotation = type.getAnnotation(DistributeMe.class);
		if (!typeAnnotation.asynchSupport())
			return;

		PrintWriter writer = filer.createSourceFile(getPackageName(type)+"."+getAsynchInterfaceName(type));
		setWriter(writer);
		
		
		writePackage(type);
		writeAnalyzerComments(type);
		writeImport(CallBackHandler.class);
		emptyline();
		
		writeString("public interface "+getAsynchInterfaceName(type)+" extends "+type.getQualifiedName()+", org.distributeme.core.asynch.AsynchStub{");
		increaseIdent();
		
		Collection<? extends MethodDeclaration> methods = getAllDeclaredMethods(type);
		for (MethodDeclaration method : methods){
			String methodDecl = getAsynchInterfaceMethodDeclaration(method);
			writeStatement(methodDecl);
			emptyline(); 
		}
		
		closeBlock();
		
		writer.flush();
		writer.close();
	}
	
	
}
