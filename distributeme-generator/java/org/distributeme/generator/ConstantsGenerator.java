package org.distributeme.generator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import net.anotheria.util.StringUtils;

import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.TypeDeclaration;

/**
 * Generates constants class for rmi service distribution.
 * @author lrosenberg.
 *
 */
public class ConstantsGenerator extends AbstractGenerator implements Generator{

	@Override
	public void generate(TypeDeclaration type, Filer filer, Map<String,String> options) throws IOException{
		PrintWriter writer = filer.createSourceFile(getPackageName(type)+"."+getConstantsName(type));
		setWriter(writer);
		
		
		writePackage(type);
		writeAnalyzerComments(type);
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
