package org.distributeme.generator;

import net.anotheria.util.StringUtils;
import org.distributeme.annotation.ConcurrencyControlClientSideLimit;
import org.distributeme.annotation.ConcurrencyControlLimit;
import org.distributeme.annotation.ConcurrencyControlServerSideLimit;
import org.distributeme.annotation.Route;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.Defaults;
import org.distributeme.core.interceptor.InterceptionPhase;
import org.distributeme.core.routing.Router;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Base generator class.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class AbstractGenerator {
	/**
	 * PrintWriter for output generations.
	 */
	private PrintWriter writer;

	/**
	 * Processing environment for the annotations. Added as support for new annotation processing.
	 */
	private final ProcessingEnvironment environment;

	/**
	 * Counter for concurrency control creation methods.
	 */
	private static AtomicInteger ccOrders = new AtomicInteger();
	
	/**
	 * Counter for router creation methods.
	 */
	private static AtomicInteger routerOrders = new AtomicInteger();

	/**
	 * Constructor for AbstractGenerator.
	 *
	 * @param environment a {@link javax.annotation.processing.ProcessingEnvironment} object.
	 */
	public AbstractGenerator(ProcessingEnvironment environment) {
		this.environment = environment;
	}


	/**
	 * Setter for the field writer.
	 *
	 * @param aWriter a {@link java.io.Writer} object.
	 */
	protected void setWriter(Writer aWriter){
		writer = new PrintWriter(aWriter);
		resetIdent();
	}
	
	/**
	 * Getter for the field writer.
	 *
	 * @return a {@link java.io.Writer} object.
	 */
	protected Writer getWriter(){
		return writer;
	}
	
	/**
	 * Returns the name of the generated Remote interface for a type.
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getRemoteInterfaceName(TypeElement type){
		return "Remote"+type.getSimpleName().toString();
	}

	/**
	 * getAsynchInterfaceName.
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getAsynchInterfaceName(TypeElement type){
		return "Asynch"+type.getSimpleName().toString();
	}

	/**
	 * Returns the name of the generated Stub class for a type.
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getStubName(TypeElement type){
		return "Remote"+type.getSimpleName().toString()+"Stub";
	}

	/**
	 * <p>getJaxRsStubName.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getJaxRsStubName(TypeElement type){
		return type.getSimpleName().toString()+"JaxRsStub";
	}

	/**
	 * <p>getAsynchStubName.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getAsynchStubName(TypeElement type){
		return "Asynch"+type.getSimpleName().toString()+"Stub";
	}

	/**
	 * <p>getDefaultImplFactoryName.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static final String getDefaultImplFactoryName(TypeElement type){
		return type.getQualifiedName()+"Factory";
	}


	/**
	 * Returns the name of the generated Skeleton class for a type.
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getSkeletonName(TypeElement type){
		return "Remote"+type.getSimpleName().toString()+"Skeleton";
	}

	/**
	 * <p>getResourceName.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getResourceName(TypeElement type){
		return type.getSimpleName().toString()+"Resource";
	}

	/**
	 * <p>getConstantsName.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getConstantsName(TypeElement type){
		return type.getSimpleName().toString()+"Constants";
	}

	/**
	 * <p>getFactoryName.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getFactoryName(TypeElement type){
		return "Remote"+type.getSimpleName().toString()+"Factory";
	}

	/**
	 * <p>getAsynchFactoryName.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getAsynchFactoryName(TypeElement type){
		return "Asynch"+type.getSimpleName().toString()+"Factory";
	}

	/**
	 * <p>getServerName.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getServerName(TypeElement type){
		String name = type.getSimpleName().toString().toString();
		return getServerName(name);
	}

	/**
	 * Return the fully qualified name for the server class.
	 *
	 * @param interfaceName a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getFullyQualifiedServerName(String interfaceName){
		int indexOfDot = interfaceName.lastIndexOf('.');
		String packageName = "";
		if (indexOfDot!=-1){
			packageName = interfaceName.substring(0, indexOfDot);
			interfaceName = interfaceName.substring(indexOfDot+1);
		}
		packageName += ".generated.";
		
		if (interfaceName.endsWith("Service"))
			interfaceName = interfaceName.substring(0, interfaceName.length()-"Service".length());
		return packageName+interfaceName+"Server";
	}

	/**
	 * <p>getServerName.</p>
	 *
	 * @param interfaceName a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getServerName(String interfaceName){
		int indexOfDot = interfaceName.lastIndexOf('.');
		if (indexOfDot!=-1)
			interfaceName = interfaceName.substring(indexOfDot+1);
		if (interfaceName.endsWith("Service"))
			interfaceName = interfaceName.substring(0, interfaceName.length()-"Service".length());
		return interfaceName+"Server";
	}
	
	/**
	 * <p>getInterfaceName.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getInterfaceName(TypeElement type){
		return type.getSimpleName().toString().toString();
	}

	/**
	 * <p>getPackageOf.</p>
	 *
	 * @param type a {@link javax.lang.model.element.Element} object.
	 * @return a {@link javax.lang.model.element.PackageElement} object.
	 */
	protected PackageElement getPackageOf(Element type) {
		Elements elements = environment.getElementUtils();
		return elements.getPackageOf(type);
	}
	
	/**
	 * <p>getPackageName.</p>
	 *
	 * @param element a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getPackageName(TypeElement element){
        return getPackageOf(element).getQualifiedName()+".generated";
	}
	
	/**
	 * <p>writePackage.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 */
	protected void writePackage(TypeElement type){
		writeString("package "+getPackageName(type)+";");
	}

	/**
	 * Writes comments that disables analyzers like checkstyle.
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 */
	protected void writeAnalyzerComments(TypeElement type){
		writeString("//BEGIN GENERATED CODE");
	}

	protected void writeAnalyzeIgnoreAnnotation(TypeElement type){
		writeString("@SuppressWarnings(\"PMD\")");
	}

	protected void writeAnalyzerCommentsEnd(TypeElement type){
		writeString("//END GENERATED CODE");
	}

	/**
	 * <p>quote.</p>
	 *
	 * @param s a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String quote(String s){
		return "\""+s+"\"";
	}
	
	/**
	 * <p>quote.</p>
	 *
	 * @param o a {@link java.lang.Object} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String quote(Object o){
		return "\""+o+"\"";
	}

	/**
	 * <p>quote.</p>
	 *
	 * @param s a {@link java.lang.StringBuilder} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String quote(StringBuilder s){
		return "\""+s.toString()+"\"";
	}

	/**
	 * <p>quote.</p>
	 *
	 * @param a a int.
	 * @return a {@link java.lang.String} object.
	 */
	protected String quote(int a){
		return quote(""+a);
	}

	/**
	 * <p>writeIncreasedString.</p>
	 *
	 * @param s a {@link java.lang.String} object.
	 */
	protected void writeIncreasedString(String s){
		increaseIdent();
		writeString(s);
		decreaseIdent();
		
	}

	/**
	 * <p>writeIncreasedStatement.</p>
	 *
	 * @param s a {@link java.lang.String} object.
	 */
	protected void writeIncreasedStatement(String s){
		writeIncreasedString(s+";");
	}
	
	/**
	 * Linefeed.
	 */
	public static final String CRLF = "\n";
	
	/**
	 * Current ident.
	 */
	private int ident = 0;

	/**
	 * Writes a string in a new line with ident and linefeed.
	 *
	 * @param s string to write.
	 */
	protected void writeString(String s){
		StringBuilder ret = getIdent();
		ret.append(s).append(CRLF);
		writer.write(ret.toString()); 
	}
	

	//later replace with openTry
	/**
	 * <p>openTry.</p>
	 */
	protected void openTry(){
		writeString("try{");
		increaseIdent();
	}

	/**
	 * <p>openFun.</p>
	 *
	 * @param s a {@link java.lang.String} object.
	 */
	protected void openFun(String s){
		if (!s.endsWith("{"))
			s+=" {";
		writeString(s);
		increaseIdent();
	}
	
	
	/**
	 * Writes a statement (';' at the end of the line)
	 *
	 * @param s statement to write.
	 */
	protected void writeStatement(String s){
		StringBuilder ret = getIdent();
		ret.append(s).append(";").append(CRLF);
		writer.write(ret.toString()); 
	}

	/**
	 * Returns current ident as string.
	 * @return a string with "\t"s.
	 */
	private StringBuilder getIdent(){
		StringBuilder ret = new StringBuilder();
		for (int i=0; i<ident; i++)
			ret.append("\t");
		return ret;
	}
	
	/**
	 * increases current ident.
	 */
	protected void increaseIdent(){
		ident++;
	}
	
	/**
	 * decreases current ident.
	 */
	protected void  decreaseIdent(){
		ident--;
		if (ident<0)
			ident = 0;
	}
	
	/**
	 * <p>resetIdent.</p>
	 */
	protected void resetIdent(){
	    ident = 0;
	}
	

	/**
	 * Appends an empty line.
	 */
	public void emptyline(){
		writer.write(CRLF);
	}
	

	/**
	 * <p>writeImport.</p>
	 *
	 * @param imp a {@link java.lang.String} object.
	 */
	protected void writeImport(String imp){
		writeString("import "+imp+";");
	}
	
	/**
	 * <p>writeImport.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 */
	protected void writeImport(Class<?> clazz){
		writeImport(clazz.getName());
	}


	/**
	 * <p>writeImport.</p>
	 *
	 * @param packagename a {@link java.lang.String} object.
	 * @param classname a {@link java.lang.String} object.
	 */
	protected void writeImport(String packagename, String classname){
		writeString("import "+packagename+"."+classname+";");
	}


	/**
	 * Closes a previously opened code block by decreasing the ident and writing a closing '}'.
	 */
	protected void closeBlock(){
		decreaseIdent();
		writeString("}");
	}

	/**
	 * Closes a previously opened code block without decreasing the ident.
	 */
	protected void closeBlockWithoutIdent(){
		writeString("}");
	}

	/**
	 * Closes a block and writes a comment.
	 *
	 * @param comment a {@link java.lang.String} object.
	 */
	protected void closeBlock(String comment){
		decreaseIdent();
		writeString("} //..."+comment);
	}

	/**
	 * Closes a block without ident and writes a comment.
	 *
	 * @param comment a {@link java.lang.String} object.
	 */
	protected void closeBlockWithoutIdent(String comment){
		writeString("} //..."+comment);
	}

	/**
	 * <p>appendMark.</p>
	 *
	 * @param markNumber a int.
	 */
	protected void appendMark(int markNumber){
		
//		String ret = "/* ***** MARK ";
//		ret += markNumber;
//		ret += ", Generator: "+this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
 //   	ret += " ***** */";
//		return emptyline()+writeString(ret)+emptyline();
	}

	/**
	 * <p>writeCommentLine.</p>
	 *
	 * @param commentline a {@link java.lang.String} object.
	 */
	protected void writeCommentLine(String commentline){
		String[] tokens = StringUtils.tokenize(commentline, '\n');
		if (tokens.length!=1)
			writeComment(commentline);
		else
			writeString("// "+commentline);
	}
	
	/**
	 * <p>writeComment.</p>
	 *
	 * @param commentline a {@link java.lang.String} object.
	 */
	protected void writeComment(String commentline){
	    String[] tokens = StringUtils.tokenize(commentline, '\n');
	    
	    writeString("/**");
	    for (int i=0; i<tokens.length; i++){
	       writeString(" * "+tokens[i]); 
	    }
	    writeString(" */");
	}


	/**
	 * <p>startClassBody.</p>
	 */
	protected void startClassBody(){
		ident = 1;
	}
	
	private String getFormalTypeDeclaration(ExecutableElement method){
		StringBuilder formalTypeDeclaration = new StringBuilder("");
		List<? extends TypeParameterElement> formalTypeParameters = method.getTypeParameters();
		for (TypeParameterElement d : formalTypeParameters){
			if (formalTypeDeclaration.length()>0)
				formalTypeDeclaration.append(", ");
			formalTypeDeclaration.append(d.toString());

			List<? extends TypeMirror> bounds = d.getBounds();

			if (!bounds.isEmpty())
				formalTypeDeclaration.append(" extends ");

			for (Iterator<? extends TypeMirror> it = bounds.iterator(); it.hasNext(); ) {
				formalTypeDeclaration.append(it.next().toString());
				if (it.hasNext())
					formalTypeDeclaration.append(" & ");
			}
		}
		
		String ret = formalTypeDeclaration.length()>0 ? 
			"<"+formalTypeDeclaration.toString()+">" : formalTypeDeclaration.toString();
		return ret;
	}

    /**
     * <p>getMethodDeclaration.</p>
     *
     * @param method a {@link javax.lang.model.element.ExecutableElement} object.
     * @return a {@link java.lang.String} object.
     */
    protected String getMethodDeclaration(ExecutableElement method){


		StringBuilder methodDecl = new StringBuilder();
		//CHANGE 1.0.8 method names now return lists instead of concrete method
		//methodDecl.append(getFormalTypeDeclaration(method)).append(method.getReturnType().toString()).append(" ");
		methodDecl.append(getFormalTypeDeclaration(method)).append("List<?>").append(" ");
		methodDecl.append(method.getSimpleName()).append("(");
		Collection<? extends VariableElement> parameters = method.getParameters();
		boolean first = true;
		for (VariableElement p : parameters){
			if (!first){
				methodDecl.append(", ");
			}
			methodDecl.append(p.asType().toString()+" "+p.getSimpleName());
			first = false;
		}

		methodDecl.append(")");

		return methodDecl.toString();
	}
	
	/**
	 * <p>getInterfaceMethodDeclaration.</p>
	 *
	 * @param method a {@link javax.lang.model.element.ExecutableElement} object.
	 * @param includeTransportableContext a boolean.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getInterfaceMethodDeclaration(ExecutableElement method, boolean includeTransportableContext){
		
		
		StringBuilder methodDecl = new StringBuilder();
		//CHANGE 1.0.8 - return value is always a list.
		//methodDecl.append(getFormalTypeDeclaration(method)).append(method.getReturnType().toString()).append(" ");
		methodDecl.append(getFormalTypeDeclaration(method)).append("List").append(" ");
		methodDecl.append(method.getSimpleName()).append("(");
		Collection<? extends VariableElement> parameters = method.getParameters();
		boolean first = true;
		for (VariableElement p : parameters){
			if (!first){
				methodDecl.append(", ");
			}
			methodDecl.append(p.asType().toString()+" "+p.getSimpleName());
			first = false;
		}
		if (includeTransportableContext){
			//adding transportable call context for piggybacking and interceptor communication.
			methodDecl.append((first ? "":", ")+"Map<?,?> __transportableCallContext");
		}
		
		methodDecl.append(")");
		
		return methodDecl.toString();
	}

	/**
	 * <p>getAsynchInterfaceMethodDeclaration.</p>
	 *
	 * @param method a {@link javax.lang.model.element.ExecutableElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getAsynchInterfaceMethodDeclaration(ExecutableElement method){
		StringBuilder methodDecl = new StringBuilder();
		methodDecl.append(getFormalTypeDeclaration(method)).append("void").append(" ");
		methodDecl.append(getAsynchMethodName(method)).append("(");
		Collection<? extends VariableElement> parameters = method.getParameters();
		boolean first = true;
		for (VariableElement p : parameters){
			if (!first){
				methodDecl.append(", ");
			}
			methodDecl.append(p.asType().toString()+" "+p.getSimpleName());
			first = false;
		}
		//adding call back handlers.
		methodDecl.append((first ? "":", ")+"CallBackHandler ... diMeCallBackHandlers");

		methodDecl.append(")");
		
		return methodDecl.toString();
	}

	/**
	 * <p>getResourceSkeletonMethodDeclaration.</p>
	 *
	 * @param method a {@link javax.lang.model.element.ExecutableElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getResourceSkeletonMethodDeclaration(ExecutableElement method){
		StringBuilder element = new StringBuilder();
		element.append(getInterfaceMethodDeclaration(method, false));
		if (method.getThrownTypes().size()>0){
			StringBuilder exceptions = new StringBuilder();
			for (TypeMirror type : method.getThrownTypes()){
				if (exceptions.length()>0)
					exceptions.append(", ");
				exceptions.append(type.toString());
			}
			element.append(" throws ").append(exceptions);
		}

		return element.toString();
	}

	/**
	 * <p>getSkeletonMethodDeclaration.</p>
	 *
	 * @param method a {@link javax.lang.model.element.ExecutableElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getSkeletonMethodDeclaration(ExecutableElement method){
		StringBuilder element = new StringBuilder();
		element.append(getInterfaceMethodDeclaration(method, true));
		if (method.getThrownTypes().size()>0){
			StringBuilder exceptions = new StringBuilder();
			for (TypeMirror type : method.getThrownTypes()){
				if (exceptions.length()>0)
					exceptions.append(", ");
				exceptions.append(type.toString());
			}
			element.append(" throws ").append(exceptions);
		}
		
		return element.toString();
	}
	
	/**
	 * <p>getStubParametersDeclaration.</p>
	 *
	 * @param method a {@link javax.lang.model.element.ExecutableElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getStubParametersDeclaration(ExecutableElement method){
		return getStubParametersDeclaration(method, false);
	}

	/**
	 * <p>getStubParametersDeclaration.</p>
	 *
	 * @param method a {@link javax.lang.model.element.ExecutableElement} object.
	 * @param declareFinal a boolean.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getStubParametersDeclaration(ExecutableElement method, boolean declareFinal){
		StringBuilder ret = new StringBuilder();
		Collection<? extends VariableElement> parameters = method.getParameters();
		boolean first = true;
		for (VariableElement p : parameters){
			if (!first){
				ret.append(", ");
			}
			ret.append((declareFinal?"final ":"")+p.asType().toString()+" "+p.getSimpleName());
			first = false;
		}
		return ret.toString();
	}
	
	/**
	 * <p>getStubParametersCall.</p>
	 *
	 * @param method a {@link javax.lang.model.element.ExecutableElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getStubParametersCall(ExecutableElement method){
		StringBuilder ret = new StringBuilder();
		Collection<? extends VariableElement> parameters = method.getParameters();
		boolean first = true;
		for (VariableElement p : parameters){
			if (!first){
				ret.append(", ");
			}
			ret.append(p.getSimpleName());
			first = false;
		}
		return ret.toString();
	}

	/**
	 * <p>getStubMethodDeclaration.</p>
	 *
	 * @param method a {@link javax.lang.model.element.ExecutableElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getStubMethodDeclaration(ExecutableElement method){
		StringBuilder methodDecl = new StringBuilder();
		methodDecl.append(getFormalTypeDeclaration(method)).append(method.getReturnType()).append(" ");
		methodDecl.append(method.getSimpleName()).append("(");
		methodDecl.append(getStubParametersDeclaration(method));
		methodDecl.append(")");
		
		if (method.getThrownTypes().size()>0){
			StringBuilder exceptions = new StringBuilder();
			for (TypeMirror type : method.getThrownTypes()){
				if (exceptions.length()>0)
					exceptions.append(", ");
				exceptions.append(type.toString());
			}
			methodDecl.append(" throws "+exceptions.toString());
		}
		
		
		return methodDecl.toString();
	}
	
	/**
	 * <p>getStubAsynchMethodDeclaration.</p>
	 *
	 * @param method a {@link javax.lang.model.element.ExecutableElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getStubAsynchMethodDeclaration(ExecutableElement method){
		StringBuilder methodDecl = new StringBuilder();
		methodDecl.append(getFormalTypeDeclaration(method)).append(" void ");
		methodDecl.append(getAsynchMethodName(method)).append("(");
		String parameters = getStubParametersDeclaration(method, true);
		methodDecl.append(parameters);
		if (parameters.length()>0)
			methodDecl.append(", ");
		methodDecl.append("final CallBackHandler... diMeHandlers");
		methodDecl.append(")");
		
//		if (method.getThrownTypes().size()>0){
//			StringBuilder exceptions = new StringBuilder();
//			for (TypeMirror type : method.getThrownTypes()){
//				if (exceptions.length()>0)
//					exceptions.append(", ");
//				exceptions.append(type.toString());
//			}
//			methodDecl.append(" throws "+exceptions.toString());
//		}
		
		
		return methodDecl.toString();
	}

	/**
	 * <p>getAsynchMethodName.</p>
	 *
	 * @param method a {@link javax.lang.model.element.ExecutableElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getAsynchMethodName(ExecutableElement method){
		return "asynch"+StringUtils.capitalize(method.getSimpleName().toString());
	}
	
	/**
	 * <p>getInternalStubMethodDeclaration.</p>
	 *
	 * @param method a {@link javax.lang.model.element.ExecutableElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getInternalStubMethodDeclaration(ExecutableElement method){
		StringBuilder methodDecl = new StringBuilder();
		methodDecl.append(getFormalTypeDeclaration(method)).append(method.getReturnType()).append(" ");
		methodDecl.append(method.getSimpleName()).append("(");
		String parameters = getStubParametersDeclaration(method);
		methodDecl.append(parameters);
		if (parameters.length()>0)
			methodDecl.append(", ");
		methodDecl.append(ClientSideCallContext.class.getName()+" diMeCallContext");
		methodDecl.append(")");
		
		if (method.getThrownTypes().size()>0){
			StringBuilder exceptions = new StringBuilder();
			for (TypeMirror type : method.getThrownTypes()){
				if (exceptions.length()>0)
					exceptions.append(", ");
				exceptions.append(type.toString());
			}
			methodDecl.append(" throws "+exceptions.toString());
		}
		
		
		return methodDecl.toString();
	}

	/**
	 * Retrieves all methods including methods from superinterfaces.
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.util.Collection} object.
	 */
	protected Collection<? extends ExecutableElement> getAllDeclaredMethods(TypeElement type){
		List<ExecutableElement> methods = ElementFilter.methodsIn(type.getEnclosedElements());
        Types types = environment.getTypeUtils();

		List<? extends TypeMirror> superinterfaces = type.getInterfaces();
		for (TypeMirror it : superinterfaces){
            Element element = types.asElement(it);
            if (element instanceof TypeElement)
                methods.addAll(getAllDeclaredMethods((TypeElement) element));
		}
		
		return methods;
	}
	
	/**
	 * <p>getAllDeclaredTypes.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.util.List} object.
	 */
	protected List<TypeElement> getAllDeclaredTypes(TypeElement type){
		ArrayList<TypeElement> typesList = new ArrayList<TypeElement>();
        Types types = environment.getTypeUtils();

		typesList.add(type);
		
		List<? extends TypeMirror> superinterfaces =  type.getInterfaces();
		for (TypeMirror it : superinterfaces){
            Element element = types.asElement(it);
            typesList.addAll(getAllDeclaredTypes((TypeElement) element));
		}
		
		return typesList;
	}
	
	/**
	 * <p>getImplementedInterfacesAsString.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getImplementedInterfacesAsString(TypeElement type){
		List<TypeElement> implementedInterfaces = getAllDeclaredTypes(type);
		StringBuilder interfaceAsString = new StringBuilder();
		for (TypeElement in : implementedInterfaces){
			if (interfaceAsString.length()>0)
				interfaceAsString.append(", ");
			interfaceAsString.append(in.getQualifiedName()).append(".class");
		}
		return interfaceAsString.toString();
	}
	
	/**
	 * Returns the mirror element for an element.
	 *
	 * @param type a {@link javax.lang.model.element.Element} object.
	 * @param ann a {@link java.lang.Class} object.
	 * @return a {@link javax.lang.model.element.AnnotationMirror} object.
	 */
	protected AnnotationMirror findMirror(Element type, Class<? extends Annotation> ann){
		List<? extends AnnotationMirror> mirrors = type.getAnnotationMirrors();
		for (AnnotationMirror m : mirrors){
			Element element = m.getAnnotationType().asElement();
			if (element.getSimpleName().toString().equals(ann.getSimpleName())){
				return m;
			}
		}
		return null;
	}
	
	/**
	 * <p>findMirrors.</p>
	 *
	 * @param type a {@link javax.lang.model.element.Element} object.
	 * @param ann a {@link java.lang.Class} object.
	 * @return a {@link java.util.List} object.
	 */
	protected List<AnnotationMirror> findMirrors(Element type, Class<? extends Annotation> ann){
		//System.out.println("-%- findMirror "+type+" ann "+ann);
		ArrayList<AnnotationMirror> ret = new ArrayList<AnnotationMirror>();
		List<? extends AnnotationMirror> mirrors = type.getAnnotationMirrors();
		for (AnnotationMirror m : mirrors){
			Element element = m.getAnnotationType().asElement();
			//System.out.println("--- checking "+Element.getSimpleName()+" compare with "+ann+" --> "+Element.getSimpleName().equals(ann.getSimpleName()));
			if (element.getSimpleName().equals(ann.getSimpleName())){
				ret.add(m);
			}
		}
		return ret;
	}
	
	/**
	 * <p>findMirrorMethod.</p>
	 *
	 * @param mirror a {@link javax.lang.model.element.AnnotationMirror} object.
	 * @param methodName a {@link java.lang.String} object.
	 * @return a {@link javax.lang.model.element.ExecutableElement} object.
	 */
	protected ExecutableElement findMirrorMethod(AnnotationMirror mirror, String methodName){
		Element executableElement = mirror.getAnnotationType().asElement();
		Collection<ExecutableElement> methods = ElementFilter.methodsIn(executableElement.getEnclosedElements());
		for (ExecutableElement element : methods){
			if (element.getSimpleName().toString().equals(methodName))
				return element;
		}
		return null;
	}
	
	/**
	 * <p>findLogWriterValue.</p>
	 *
	 * @param mirror a {@link javax.lang.model.element.AnnotationMirror} object.
	 * @return a {@link javax.lang.model.element.AnnotationValue} object.
	 */
	protected AnnotationValue findLogWriterValue(AnnotationMirror mirror){
		return findMethodValue(mirror, "logWriterClazz");
	}

	/**
	 * <p>findRouterClassValue.</p>
	 *
	 * @param mirror a {@link javax.lang.model.element.AnnotationMirror} object.
	 * @return a {@link javax.lang.model.element.AnnotationValue} object.
	 */
	protected AnnotationValue findRouterClassValue(AnnotationMirror mirror){
		return findMethodValue(mirror, "routerClass");
	}

	/**
	 * <p>findRouterParameterValue.</p>
	 *
	 * @param mirror a {@link javax.lang.model.element.AnnotationMirror} object.
	 * @return a {@link javax.lang.model.element.AnnotationValue} object.
	 */
	protected AnnotationValue findRouterParameterValue(AnnotationMirror mirror){
		return findMethodValue(mirror, "routerParameter");
	}

	/**
	 * <p>findRouterConfigurationName.</p>
	 *
	 * @param mirror a {@link javax.lang.model.element.AnnotationMirror} object.
	 * @return a {@link javax.lang.model.element.AnnotationValue} object.
	 */
	protected AnnotationValue findRouterConfigurationName(AnnotationMirror mirror){
		return findMethodValue(mirror, "configurationName");
	}

	/**
	 * <p>findMethodValue.</p>
	 *
	 * @param mirror a {@link javax.lang.model.element.AnnotationMirror} object.
	 * @param methodName a {@link java.lang.String} object.
	 * @return a {@link javax.lang.model.element.AnnotationValue} object.
	 */
	protected AnnotationValue findMethodValue(AnnotationMirror mirror, String methodName){
		//System.out.println("-- Called findMethodValue on "+methodName+" and "+mirror);
		ExecutableElement method = findMirrorMethod(mirror, methodName);
		Map<? extends ExecutableElement, ? extends AnnotationValue> values = mirror.getElementValues();
		//System.out.println("-- values --: "+values);
		AnnotationValue mirrorMethodValue = values.get(method);
		return mirrorMethodValue;

	}
	
	/**
	 * Concurrency control annotation - representation helper class at gen-time.
	 * @author lrosenberg
	 *
	 */
	protected static class TranslatedCCAnnotation extends TranslatedAnnotation{
		public TranslatedCCAnnotation(String aStrategyClass, String aParameter){
			super(aStrategyClass, aParameter, ccOrders.incrementAndGet());
		}
		
	}
	
	/**
	 * Router annotation - representation helper class at gen-time.
	 * @author lrosenberg
	 *
	 */
	protected static class TranslatedRouterAnnotation extends TranslatedAnnotation{

		public TranslatedRouterAnnotation(String aStrategyClass, String aParameter, String configurationName){
			super(aStrategyClass, aParameter, routerOrders.incrementAndGet());
			setConfigurationName(configurationName);
		}
	}

	/**
	 * <p>findConcurrencyControlAnnotation.</p>
	 *
	 * @param type a {@link javax.lang.model.element.Element} object.
	 * @return a {@link org.distributeme.generator.AbstractGenerator.TranslatedCCAnnotation} object.
	 */
	protected TranslatedCCAnnotation findConcurrencyControlAnnotation(Element type){
		//try all shortcuts first.
		Annotation ann; 
		ann = type.getAnnotation(ConcurrencyControlServerSideLimit.class);
		if (ann!=null){
			String configName = ((ConcurrencyControlServerSideLimit)ann).configurationName();
			if (configName!=null && configName.length()>0)
				return new TranslatedCCAnnotation("org.distributeme.core.concurrencycontrol.ConfigurationBasedConcurrencyControlStrategy", configName);
			return new TranslatedCCAnnotation("org.distributeme.core.concurrencycontrol.ConstantBasedConcurrencyControlStrategy", "0,"+((ConcurrencyControlServerSideLimit)ann).value());

		}
		ann = type.getAnnotation(ConcurrencyControlClientSideLimit.class);
		if (ann!=null){
			String configName = ((ConcurrencyControlClientSideLimit)ann).configurationName();
			if (configName!=null && configName.length()>0)
				return new TranslatedCCAnnotation("org.distributeme.core.concurrencycontrol.ConfigurationBasedConcurrencyControlStrategy", configName);
			return new TranslatedCCAnnotation("org.distributeme.core.concurrencycontrol.ConstantBasedConcurrencyControlStrategy", ""+((ConcurrencyControlClientSideLimit)ann).value()+",0");
		}
		ann = type.getAnnotation(ConcurrencyControlLimit.class);
		if (ann!=null){
			String configName = ((ConcurrencyControlLimit)ann).configurationName();
			if (configName!=null && configName.length()>0)
				return new TranslatedCCAnnotation("org.distributeme.core.concurrencycontrol.ConfigurationBasedConcurrencyControlStrategy", configName);
			return new TranslatedCCAnnotation("org.distributeme.core.concurrencycontrol.ConstantBasedConcurrencyControlStrategy", ""+((ConcurrencyControlLimit)ann).client()+","+((ConcurrencyControlLimit)ann).server());
		}
		
		return null;
	}

	/**
	 * <p>writeRouterDeclarations.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.util.List} object.
	 */
	protected List<TranslatedRouterAnnotation> writeRouterDeclarations(TypeElement type){
		List<TranslatedRouterAnnotation> ret = new ArrayList<AbstractGenerator.TranslatedRouterAnnotation>();
		Collection<? extends ExecutableElement> methods = getAllDeclaredMethods(type);
		writeCommentLine("ROUTER DECL V2");
		
		AnnotationMirror clazzWideRoute = findMirror(type, Route.class);
		if (clazzWideRoute!=null){
			writeCommentLine("Class wide router ");
			AnnotationValue configurationNameValue = findRouterConfigurationName(clazzWideRoute);
			TranslatedRouterAnnotation tra = new TranslatedRouterAnnotation(""+findRouterClassValue(clazzWideRoute).getValue(),
					""+findRouterParameterValue(clazzWideRoute).getValue(),
					configurationNameValue == null ?  "":""+configurationNameValue.getValue()
			);
			writeStatement("private final "+Router.class.getName() + " clazzWideRouter = createRouterInstance"+tra.getOrder()+"()");
			ret.add(tra);
		}else{
			writeCommentLine("No class-wide-router set, skipping.");
		}
		emptyline();
		
		
		writeCommentLine("Method wide routers if applicable ");
		for (ExecutableElement method : methods){
			AnnotationMirror methodRoute = findMirror(method, Route.class);
			if (methodRoute!=null){
				//System.out.println("Will write "+Router.class.getName()+" "+getMethodRouterName(method));
				
				AnnotationValue routerParameterValue = findRouterParameterValue(methodRoute);
				AnnotationValue configurationNameValue = findRouterConfigurationName(methodRoute);
				TranslatedRouterAnnotation tra = new TranslatedRouterAnnotation(""+findRouterClassValue(methodRoute).getValue(),
						routerParameterValue == null ? "":""+routerParameterValue.getValue(),
						configurationNameValue == null ? "":""+configurationNameValue.getValue()
				);
				writeStatement("private final "+Router.class.getName()+" "+getMethodRouterName(method) +" = createRouterInstance"+tra.getOrder()+"()");
				ret.add(tra);
			}
		}
		writeCommentLine("Method wide routers END ");
		emptyline();
		
		//AnnotationMirror clazzWideCCStrategyAnnotation = findMirror(type, ConcurrencyControl.class);

//		TranslatedCCAnnotation clazzWideCCStrategyAnnotation = findConcurrencyControlAnnotation(type);
//		if (clazzWideCCStrategyAnnotation!=null){
//			writeStatement("private ConcurrencyControlStrategy clazzWideCCStrategy = createConcurrencyControlStrategy"+clazzWideCCStrategyAnnotation.getOrder()+"()");
//			ret.add(clazzWideCCStrategyAnnotation);
//		}else{
//			writeStatement("private ConcurrencyControlStrategy clazzWideCCStrategy = "+Defaults.class.getSimpleName()+".getDefaultConcurrencyControlStrategy()");
//		}
//		emptyline();
//
//		for (ExecutableElement method : methods){
//			TranslatedCCAnnotation methodCCStrategyAnnotation = findConcurrencyControlAnnotation(method);
//			if (methodCCStrategyAnnotation != null){
//				writeStatement("private ConcurrencyControlStrategy "+getCCStrategyVariableName(method)+" = createConcurrencyControlStrategy"+methodCCStrategyAnnotation.getOrder()+"()");
//				ret.add(methodCCStrategyAnnotation);
//			}else{
//				writeStatement("private ConcurrencyControlStrategy "+getCCStrategyVariableName(method)+" = clazzWideCCStrategy");				
//			}
//		}
//		writeCommentLine("CONCURRENCY CONTROL end");
//		emptyline();
		writeCommentLine("ROUTER DECL V2 end");
		return ret;
	}
	
	/**
	 * <p>writeConcurrencyControlDeclarations.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 * @return a {@link java.util.List} object.
	 */
	protected List<TranslatedCCAnnotation> writeConcurrencyControlDeclarations(TypeElement type){
		List<TranslatedCCAnnotation> ret = new ArrayList<AbstractGenerator.TranslatedCCAnnotation>();
		Collection<? extends ExecutableElement> methods = getAllDeclaredMethods(type);
		writeCommentLine("CONCURRENCY CONTROL");
		writeCommentLine("Class wide concurrency control strategy ");
		//AnnotationMirror clazzWideCCStrategyAnnotation = findMirror(type, ConcurrencyControl.class);
		TranslatedCCAnnotation clazzWideCCStrategyAnnotation = findConcurrencyControlAnnotation(type);
		if (clazzWideCCStrategyAnnotation!=null){
			writeStatement("private ConcurrencyControlStrategy clazzWideCCStrategy = createConcurrencyControlStrategy"+clazzWideCCStrategyAnnotation.getOrder()+"()");
			ret.add(clazzWideCCStrategyAnnotation);
		}else{
			writeStatement("private ConcurrencyControlStrategy clazzWideCCStrategy = "+Defaults.class.getSimpleName()+".getDefaultConcurrencyControlStrategy()");
		}
		emptyline();

		for (ExecutableElement method : methods){
			TranslatedCCAnnotation methodCCStrategyAnnotation = findConcurrencyControlAnnotation(method);
			if (methodCCStrategyAnnotation != null){
				writeStatement("private ConcurrencyControlStrategy "+getCCStrategyVariableName(method)+" = createConcurrencyControlStrategy"+methodCCStrategyAnnotation.getOrder()+"()");
				ret.add(methodCCStrategyAnnotation);
			}else{
				writeStatement("private ConcurrencyControlStrategy "+getCCStrategyVariableName(method)+" = clazzWideCCStrategy");				
			}
		}
		writeCommentLine("CONCURRENCY CONTROL end");
		emptyline();
		return ret;
	}

	private StringBuilder getParameterizedVariableName(ExecutableElement element){
		StringBuilder ret = new StringBuilder();
		for (VariableElement pd : element.getParameters()){
			ret.append('_');
			ret.append(stripStrategyVariableName(pd.asType().toString()));
			//ret.append(pd.getType().getClass().getSimpleName());
			ret.append(pd.getSimpleName());
		}
		return ret;
	}

	/**
	 * Returns the name of the failing strategy variable for a method.
	 *
	 * @param element a {@link javax.lang.model.element.ExecutableElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getFailingStrategyVariableName(ExecutableElement element){
		StringBuilder ret = new StringBuilder(element.getSimpleName()).append("FailingStrategy");
		ret.append(getParameterizedVariableName(element));
		return ret.toString();
	}
	/**
	 * Returns the name for concurrency control strategy variable
	 *
	 * @param element a {@link javax.lang.model.element.ExecutableElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getCCStrategyVariableName(ExecutableElement element){
		StringBuilder ret = new StringBuilder(element.getSimpleName()).append("CCStrategy");
		ret.append(getParameterizedVariableName(element));
		return ret.toString();
	}
	
	//included <> as backup in case we have generic types in types (List<Set<String>>).
	/**
	 * Chars which should be removed from names of variables.
	 */
	private static char[] toRemove = {'.', '[', ']', '<', '>'};
	private String stripStrategyVariableName(String toStrip){
		String ret = StringUtils.removeTag(toStrip, "");
		ret = StringUtils.removeChars(ret, toRemove);
		return ret;
	}
	
	
	/**
	 * <p>writeConcurrencyControlCreationMethod.</p>
	 *
	 * @param cca a {@link org.distributeme.generator.AbstractGenerator.TranslatedCCAnnotation} object.
	 */
	protected void writeConcurrencyControlCreationMethod(TranslatedCCAnnotation cca){
		writeString("private ConcurrencyControlStrategy createConcurrencyControlStrategy"+cca.getOrder()+"(){");
		increaseIdent();
		writeStatement("ConcurrencyControlStrategy strat = new "+cca.getStrategyClass()+"()");
		writeStatement("strat.customize("+quote(cca.getParameter())+")");
		writeStatement("return strat");
		closeBlock();
	}
	
	/**
	 * writeRouterCreationMethod.
	 *
	 * @param serviceIdCall a {@link java.lang.String} object.
	 * @param tra a {@link org.distributeme.generator.AbstractGenerator.TranslatedRouterAnnotation} object.
	 */
	protected void writeRouterCreationMethod(String serviceIdCall, TranslatedRouterAnnotation tra){
		writeString("private "+Router.class.getName()+" createRouterInstance"+tra.getOrder()+"(){");
		increaseIdent();
		writeStatement(Router.class.getName()+" router = new "+tra.getStrategyClass()+"()");
		if (tra.getConfigurationName()!=null && tra.getConfigurationName().length()>0){
			writeStatement("((org.distributeme.core.routing.ConfigurableRouter)router).setConfigurationName("+serviceIdCall+ ", " +quote(tra.getConfigurationName())+")");
		}else {
			writeStatement("router.customize("+serviceIdCall+ ", " + quote(tra.getParameter()) + ")");
		}
		writeStatement("return router");
		closeBlock();
	}

	/**
	 * InterceptionPhaseToMethod.
	 *
	 * @param phase a {@link org.distributeme.core.interceptor.InterceptionPhase} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String interceptionPhaseToMethod(InterceptionPhase phase){
		switch(phase){
		case BEFORE_SERVANT_CALL: 
			return "beforeServantCall";
		case BEFORE_SERVICE_CALL:
			return "beforeServiceCall";
		case AFTER_SERVICE_CALL: 
			return "afterServiceCall";
		case AFTER_SERVANT_CALL:
			return "afterServantCall";
		default: 
			throw new IllegalArgumentException("Unsupported interception phase");
		}
	}

	/**
	 * Returns true if the method has no return value.
	 *
	 * @param decl a {@link javax.lang.model.element.ExecutableElement} object.
	 * @return a boolean.
	 */
	protected boolean isVoidReturn(ExecutableElement decl){
		return decl.getReturnType().toString().equals("void");
	}

	/**
	 * getMethodRouterName.
	 *
	 * @param element a {@link javax.lang.model.element.ExecutableElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getMethodRouterName(ExecutableElement element){
		return element.getSimpleName()+"Router";
	}

	
}
