package org.distributeme.generator;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.distributeme.core.lifecycle.ServiceAdapter;

import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ReferenceType;

/**
 * Generator for RMI based remote interface. 
 * @author lrosenberg
 */
public class RemoteInterfaceGenerator extends AbstractGenerator implements Generator{

	@Override
	public void generate(TypeDeclaration type, Filer filer, Map<String,String> options) throws IOException{
		PrintWriter writer = filer.createSourceFile(getPackageName(type)+"."+getRemoteInterfaceName(type));
		setWriter(writer);
		
		
		writePackage(type);
		writeAnalyzerComments(type);
		emptyline();
		writeImport(Remote.class);
		writeImport(RemoteException.class);
		writeImport(ServiceAdapter.class);
		writeImport(List.class);
		writeImport(Map.class);
		emptyline();
		
		writeString("public interface "+getRemoteInterfaceName(type)+" extends Remote, ServiceAdapter{");
		increaseIdent();
		
		Collection<? extends MethodDeclaration> methods = getAllDeclaredMethods(type);
		for (MethodDeclaration method : methods){
			String methodDecl = getInterfaceMethodDeclaration(method);
			
			if (method.getThrownTypes().size()>0){
				StringBuilder exceptions = new StringBuilder();
				for (ReferenceType rt : method.getThrownTypes()){
					if (exceptions.length()>0)
						exceptions.append(", ");
					exceptions.append(rt.toString());
				}
				methodDecl += " throws "+exceptions +", RemoteException";
			}else{
				methodDecl +=" throws RemoteException";
			}
			
			writeStatement(methodDecl);
			emptyline(); 
		}
		
		closeBlock();
		
		writer.flush();
		writer.close();
	}
	
	
}
