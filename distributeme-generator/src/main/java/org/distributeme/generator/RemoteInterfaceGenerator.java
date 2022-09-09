package org.distributeme.generator;

import org.distributeme.core.lifecycle.ServiceAdapter;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Generator for RMI based remote interface.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class RemoteInterfaceGenerator extends AbstractGenerator implements Generator{

	/**
	 * <p>Constructor for RemoteInterfaceGenerator.</p>
	 *
	 * @param environment a {@link javax.annotation.processing.ProcessingEnvironment} object.
	 */
	public RemoteInterfaceGenerator(ProcessingEnvironment environment) {
		super(environment);
	}

	/** {@inheritDoc} */
	@Override
	public void generate(TypeElement type, Filer filer, Map<String,String> options) throws IOException{
		JavaFileObject sourceFile = filer.createSourceFile(getPackageName(type)+"."+getRemoteInterfaceName(type));
		PrintWriter writer = new PrintWriter(sourceFile.openWriter());
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
		
		Collection<? extends ExecutableElement> methods = getAllDeclaredMethods(type);
		for (ExecutableElement method : methods){
			String methodDecl = getInterfaceMethodDeclaration(method, true);
			
			if (method.getThrownTypes().size()>0){
				StringBuilder exceptions = new StringBuilder();
				for (TypeMirror rt : method.getThrownTypes()){
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
		writeAnalyzerCommentsEnd(type);

		writer.flush();
		writer.close();
	}
	
	
}
