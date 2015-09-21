package org.distributeme.generator;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.TypeParameterDeclaration;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.ReferenceType;
import net.anotheria.util.StringUtils;
import org.distributeme.annotation.ConcurrencyControlClientSideLimit;
import org.distributeme.annotation.ConcurrencyControlLimit;
import org.distributeme.annotation.ConcurrencyControlServerSideLimit;
import org.distributeme.annotation.Route;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.Defaults;
import org.distributeme.core.interceptor.InterceptionPhase;
import org.distributeme.core.routing.Router;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Base generator class.
 * @author lrosenberg
 *
 */
public class AbstractGenerator {
	/**
	 * PrintWriter for output generations.
	 */
	private PrintWriter writer; 

	/**
	 * Counter for concurrency control creation methods.
	 */
	private static AtomicInteger ccOrders = new AtomicInteger();
	
	/**
	 * Counter for router creation methods.
	 */
	private static AtomicInteger routerOrders = new AtomicInteger();

	
	protected void setWriter(PrintWriter aWriter){
		writer = aWriter; 
		resetIdent();
	}
	
	protected PrintWriter getWriter(){
		return writer;
	}
	
	/**
	 * Returns the name of the generated Remote interface for a type.
	 * @param type
	 * @return
	 */
	protected static String getRemoteInterfaceName(TypeDeclaration type){
		return "Remote"+type.getSimpleName();
	}

	protected static String getAsynchInterfaceName(TypeDeclaration type){
		return "Asynch"+type.getSimpleName();
	}

	/**
	 * Returns the name of the generated Stub class for a type.
	 * @param type
	 * @return
	 */
	protected static String getStubName(TypeDeclaration type){
		return "Remote"+type.getSimpleName()+"Stub";
	}

	protected static String getJaxRsStubName(TypeDeclaration type){
		return type.getSimpleName()+"JaxRsStub";
	}

	protected static String getAsynchStubName(TypeDeclaration type){
		return "Asynch"+type.getSimpleName()+"Stub";
	}

	public static final String getDefaultImplFactoryName(TypeDeclaration type){
		return type.getQualifiedName()+"Factory";
	}


	/**
	 * Returns the name of the generated Skeleton class for a type.
	 * @param type
	 * @return
	 */
	protected static String getSkeletonName(TypeDeclaration type){
		return "Remote"+type.getSimpleName()+"Skeleton";
	}

	protected static String getResourceName(TypeDeclaration type){
		return type.getSimpleName()+"Resource";
	}

	protected static String getConstantsName(TypeDeclaration type){
		return type.getSimpleName()+"Constants";
	}

	protected static String getFactoryName(TypeDeclaration type){
		return "Remote"+type.getSimpleName()+"Factory";
	}

	protected static String getAsynchFactoryName(TypeDeclaration type){
		return "Asynch"+type.getSimpleName()+"Factory";
	}

	protected static String getServerName(TypeDeclaration type){
		String name = type.getSimpleName();
		return getServerName(name);
	}

	/**
	 * Return the fully qualified name for the server class.
	 * @param interfaceName
	 * @return
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

	protected static String getServerName(String interfaceName){
		int indexOfDot = interfaceName.lastIndexOf('.');
		if (indexOfDot!=-1)
			interfaceName = interfaceName.substring(indexOfDot+1);
		if (interfaceName.endsWith("Service"))
			interfaceName = interfaceName.substring(0, interfaceName.length()-"Service".length());
		return interfaceName+"Server";
	}
	
	protected static String getInterfaceName(TypeDeclaration type){
		return type.getSimpleName();
	}
	
	
	protected static String getPackageName(TypeDeclaration type){
		return type.getPackage().getQualifiedName()+".generated";
	}
	
	protected void writePackage(TypeDeclaration type){
		writeString("package "+getPackageName(type)+";");
	}

	/**
	 * Writes comments that disables analyzers like checkstyle.
	 * @param type
	 */
	protected void writeAnalyzerComments(TypeDeclaration type){
		writeString("//CHECKSTYLE:OFF");
	}

	protected String quote(String s){
		return "\""+s+"\"";
	}
	
	protected String quote(Object o){
		return "\""+o+"\"";
	}

	protected String quote(StringBuilder s){
		return "\""+s.toString()+"\"";
	}

	protected String quote(int a){
		return quote(""+a);
	}

	protected void writeIncreasedString(String s){
		increaseIdent();
		writeString(s);
		decreaseIdent();
		
	}

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
	 * @param s string to write.
	 * @return
	 */
	protected void writeString(String s){
		StringBuilder ret = getIdent();
		ret.append(s).append(CRLF);
		writer.write(ret.toString()); 
	}
	

	//later replace with openTry
	protected void openTry(){
		writeString("try{");
		increaseIdent();
	}

	protected void openFun(String s){
		if (!s.endsWith("{"))
			s+=" {";
		writeString(s);
		increaseIdent();
	}
	
	
	/**
	 * Writes a statement (';' at the end of the line)
	 * @param s statement to write.
	 * @return
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
	
	protected void resetIdent(){
	    ident = 0;
	}
	

	/**
	 * Appends an empty line.
	 */
	public void emptyline(){
		writer.write(CRLF);
	}
	

	protected void writeImport(String imp){
		writeString("import "+imp+";");
	}
	
	protected void writeImport(Class<?> clazz){
		writeImport(clazz.getName());
	}


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
	 * @param comment
	 */
	protected void closeBlock(String comment){
		decreaseIdent();
		writeString("} //..."+comment);
	}

	/**
	 * Closes a block without ident and writes a comment.
	 * @param comment
	 */
	protected void closeBlockWithoutIdent(String comment){
		writeString("} //..."+comment);
	}

	protected void appendMark(int markNumber){
		
//		String ret = "/* ***** MARK ";
//		ret += markNumber;
//		ret += ", Generator: "+this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
 //   	ret += " ***** */";
//		return emptyline()+writeString(ret)+emptyline();
	}

	/**
	 * @param commentline
	 * @return
	 */
	protected void writeCommentLine(String commentline){
		String tokens[] = StringUtils.tokenize(commentline, '\n');
		if (tokens.length!=1)
			writeComment(commentline);
		else
			writeString("// "+commentline);
	}
	
	protected void writeComment(String commentline){
	    String tokens[] = StringUtils.tokenize(commentline, '\n');
	    
	    writeString("/**");
	    for (int i=0; i<tokens.length; i++){
	       writeString(" * "+tokens[i]); 
	    }
	    writeString(" */");
	}


	protected void startClassBody(){
		ident = 1;
	}
	
	private String getFormalTypeDeclaration(MethodDeclaration method){
		StringBuilder formalTypeDeclaration = new StringBuilder("");
		Collection<TypeParameterDeclaration> formalTypeParameters = method.getFormalTypeParameters();
		for (TypeParameterDeclaration d : formalTypeParameters){
			if (formalTypeDeclaration.length()>0)
				formalTypeDeclaration.append(", ");
			formalTypeDeclaration.append(d.toString());
		}
		
		String ret = formalTypeDeclaration.length()>0 ? 
			"<"+formalTypeDeclaration.toString()+">" : formalTypeDeclaration.toString();
		return ret;
	}

    protected String getMethodDeclaration(MethodDeclaration method){


		StringBuilder methodDecl = new StringBuilder();
		//CHANGE 1.0.8 method names now return lists instead of concrete method
		//methodDecl.append(getFormalTypeDeclaration(method)).append(method.getReturnType().toString()).append(" ");
		methodDecl.append(getFormalTypeDeclaration(method)).append("List<?>").append(" ");
		methodDecl.append(method.getSimpleName()).append("(");
		Collection<? extends ParameterDeclaration> parameters = method.getParameters();
		boolean first = true;
		for (ParameterDeclaration p : parameters){
			if (!first){
				methodDecl.append(", ");
			}
			methodDecl.append(p.getType().toString()+" "+p.getSimpleName());
			first = false;
		}

		methodDecl.append(")");

		return methodDecl.toString();
	}
	
	protected String getInterfaceMethodDeclaration(MethodDeclaration method, boolean includeTransportableContext){
		
		
		StringBuilder methodDecl = new StringBuilder();
		//CHANGE 1.0.8 - return value is always a list.
		//methodDecl.append(getFormalTypeDeclaration(method)).append(method.getReturnType().toString()).append(" ");
		methodDecl.append(getFormalTypeDeclaration(method)).append("List").append(" ");
		methodDecl.append(method.getSimpleName()).append("(");
		Collection<? extends ParameterDeclaration> parameters = method.getParameters();
		boolean first = true;
		for (ParameterDeclaration p : parameters){
			if (!first){
				methodDecl.append(", ");
			}
			methodDecl.append(p.getType().toString()+" "+p.getSimpleName());
			first = false;
		}
		if (includeTransportableContext){
			//adding transportable call context for piggybacking and interceptor communication.
			methodDecl.append((first ? "":", ")+"Map<?,?> __transportableCallContext");
		}
		
		methodDecl.append(")");
		
		return methodDecl.toString();
	}

	protected String getAsynchInterfaceMethodDeclaration(MethodDeclaration method){
		StringBuilder methodDecl = new StringBuilder();
		methodDecl.append(getFormalTypeDeclaration(method)).append("void").append(" ");
		methodDecl.append(getAsynchMethodName(method)).append("(");
		Collection<? extends ParameterDeclaration> parameters = method.getParameters();
		boolean first = true;
		for (ParameterDeclaration p : parameters){
			if (!first){
				methodDecl.append(", ");
			}
			methodDecl.append(p.getType().toString()+" "+p.getSimpleName());
			first = false;
		}
		//adding call back handlers.
		methodDecl.append((first ? "":", ")+"CallBackHandler ... diMeCallBackHandlers");

		methodDecl.append(")");
		
		return methodDecl.toString();
	}

	protected String getResourceSkeletonMethodDeclaration(MethodDeclaration method){
		StringBuilder declaration = new StringBuilder();
		declaration.append(getInterfaceMethodDeclaration(method, false));
		if (method.getThrownTypes().size()>0){
			StringBuilder exceptions = new StringBuilder();
			for (ReferenceType type : method.getThrownTypes()){
				if (exceptions.length()>0)
					exceptions.append(", ");
				exceptions.append(type.toString());
			}
			declaration.append(" throws ").append(exceptions);
		}

		return declaration.toString();
	}

	protected String getSkeletonMethodDeclaration(MethodDeclaration method){
		StringBuilder declaration = new StringBuilder(); 
		declaration.append(getInterfaceMethodDeclaration(method, true));
		if (method.getThrownTypes().size()>0){
			StringBuilder exceptions = new StringBuilder();
			for (ReferenceType type : method.getThrownTypes()){
				if (exceptions.length()>0)
					exceptions.append(", ");
				exceptions.append(type.toString());
			}
			declaration.append(" throws ").append(exceptions);
		}
		
		return declaration.toString();
	}
	
	protected String getStubParametersDeclaration(MethodDeclaration method){
		return getStubParametersDeclaration(method, false);
	}

	protected String getStubParametersDeclaration(MethodDeclaration method, boolean declareFinal){
		StringBuilder ret = new StringBuilder();
		Collection<? extends ParameterDeclaration> parameters = method.getParameters();
		boolean first = true;
		for (ParameterDeclaration p : parameters){
			if (!first){
				ret.append(", ");
			}
			ret.append((declareFinal?"final ":"")+p.getType().toString()+" "+p.getSimpleName());
			first = false;
		}
		return ret.toString();
	}
	
	protected String getStubParametersCall(MethodDeclaration method){
		StringBuilder ret = new StringBuilder();
		Collection<? extends ParameterDeclaration> parameters = method.getParameters();
		boolean first = true;
		for (ParameterDeclaration p : parameters){
			if (!first){
				ret.append(", ");
			}
			ret.append(p.getSimpleName());
			first = false;
		}
		return ret.toString();
	}

	protected String getStubMethodDeclaration(MethodDeclaration method){
		StringBuilder methodDecl = new StringBuilder();
		methodDecl.append(getFormalTypeDeclaration(method)).append(method.getReturnType()).append(" ");
		methodDecl.append(method.getSimpleName()).append("(");
		methodDecl.append(getStubParametersDeclaration(method));
		methodDecl.append(")");
		
		if (method.getThrownTypes().size()>0){
			StringBuilder exceptions = new StringBuilder();
			for (ReferenceType type : method.getThrownTypes()){
				if (exceptions.length()>0)
					exceptions.append(", ");
				exceptions.append(type.toString());
			}
			methodDecl.append(" throws "+exceptions.toString());
		}
		
		
		return methodDecl.toString();
	}
	
	protected String getStubAsynchMethodDeclaration(MethodDeclaration method){
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
//			for (ReferenceType type : method.getThrownTypes()){
//				if (exceptions.length()>0)
//					exceptions.append(", ");
//				exceptions.append(type.toString());
//			}
//			methodDecl.append(" throws "+exceptions.toString());
//		}
		
		
		return methodDecl.toString();
	}

	protected String getAsynchMethodName(MethodDeclaration method){
		return "asynch"+StringUtils.capitalize(method.getSimpleName());
	}
	
	protected String getInternalStubMethodDeclaration(MethodDeclaration method){
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
			for (ReferenceType type : method.getThrownTypes()){
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
	 * @param type
	 * @return
	 */
	protected Collection<? extends MethodDeclaration> getAllDeclaredMethods(TypeDeclaration type){
		ArrayList<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
		methods.addAll(type.getMethods());
		
		Collection<InterfaceType> superinterfaces =  type.getSuperinterfaces();
		for (InterfaceType it : superinterfaces){
			methods.addAll(getAllDeclaredMethods(it.getDeclaration()));
		}
		
		return methods;
	}
	
	protected List<TypeDeclaration> getAllDeclaredTypes(TypeDeclaration type){
		ArrayList<TypeDeclaration> types = new ArrayList<TypeDeclaration>();
		
		types.add(type);
		
		Collection<InterfaceType> superinterfaces =  type.getSuperinterfaces();
		for (InterfaceType it : superinterfaces){
			types.addAll(getAllDeclaredTypes(it.getDeclaration()));
		}
		
		return types;
	}
	
	protected String getImplementedInterfacesAsString(TypeDeclaration type){
		List<TypeDeclaration> implementedInterfaces = getAllDeclaredTypes(type);
		String interfaceAsString = "";
		for (TypeDeclaration in : implementedInterfaces){
			if (interfaceAsString.length()>0)
				interfaceAsString += ", ";
			interfaceAsString += in.getQualifiedName()+".class";
		}
		return interfaceAsString;
	}
	
	/**
	 * Returns the mirror declaration for a declaration.
	 * @param type
	 * @param ann
	 * @return
	 */
	protected AnnotationMirror findMirror(Declaration type, Class<? extends Annotation> ann){
		//System.out.println("-%- findMirror "+type+" ann "+ann);
		Collection<AnnotationMirror> mirros = type.getAnnotationMirrors();
		for (AnnotationMirror m : mirros){
			AnnotationTypeDeclaration declaration = m.getAnnotationType().getDeclaration();
			//System.out.println("--- checking "+declaration.getSimpleName()+" compare with "+ann+" --> "+declaration.getSimpleName().equals(ann.getSimpleName()));
			if (declaration.getSimpleName().equals(ann.getSimpleName())){
				//System.out.println("returns "+m);
				return m;
			}
		}
		//System.out.println("returns "+null);
		return null;
	}
	
	protected List<AnnotationMirror> findMirrors(Declaration type, Class<? extends Annotation> ann){
		//System.out.println("-%- findMirror "+type+" ann "+ann);
		ArrayList<AnnotationMirror> ret = new ArrayList<AnnotationMirror>();
		Collection<AnnotationMirror> mirrors = type.getAnnotationMirrors();
		for (AnnotationMirror m : mirrors){
			AnnotationTypeDeclaration declaration = m.getAnnotationType().getDeclaration();
			//System.out.println("--- checking "+declaration.getSimpleName()+" compare with "+ann+" --> "+declaration.getSimpleName().equals(ann.getSimpleName()));
			if (declaration.getSimpleName().equals(ann.getSimpleName())){
				ret.add(m);
			}
		}
		return ret;
	}
	
	protected AnnotationTypeElementDeclaration findMirrorMethod(AnnotationMirror mirror, String methodName){
		AnnotationTypeDeclaration declaration = mirror.getAnnotationType().getDeclaration();
		Collection<AnnotationTypeElementDeclaration> methods = declaration.getMethods();
		for (AnnotationTypeElementDeclaration element : methods){
			if (element.getSimpleName().equals(methodName))
				return element;
		}
		return null;
	}
	
	protected AnnotationValue findLogWriterValue(AnnotationMirror mirror){
		return findMethodValue(mirror, "logWriterClazz");
	}

	protected AnnotationValue findRouterClassValue(AnnotationMirror mirror){
		return findMethodValue(mirror, "routerClass");
	}

	protected AnnotationValue findRouterParameterValue(AnnotationMirror mirror){
		return findMethodValue(mirror, "routerParameter");
	}

	protected AnnotationValue findRouterConfigurationName(AnnotationMirror mirror){
		return findMethodValue(mirror, "configurationName");
	}

	protected AnnotationValue findMethodValue(AnnotationMirror mirror, String methodName){
		//System.out.println("-- Called findMethodValue on "+methodName+" and "+mirror);
		AnnotationTypeElementDeclaration method = findMirrorMethod(mirror, methodName);
		Map<AnnotationTypeElementDeclaration, AnnotationValue> values = mirror.getElementValues();
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

	protected TranslatedCCAnnotation findConcurrencyControlAnnotation(Declaration type){
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

	protected List<TranslatedRouterAnnotation> writeRouterDeclarations(TypeDeclaration type){
		List<TranslatedRouterAnnotation> ret = new ArrayList<AbstractGenerator.TranslatedRouterAnnotation>();
		Collection<? extends MethodDeclaration> methods = getAllDeclaredMethods(type);
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
		for (MethodDeclaration method : methods){
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
//		for (MethodDeclaration method : methods){
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
	
	protected List<TranslatedCCAnnotation> writeConcurrencyControlDeclarations(TypeDeclaration type){
		List<TranslatedCCAnnotation> ret = new ArrayList<AbstractGenerator.TranslatedCCAnnotation>();
		Collection<? extends MethodDeclaration> methods = getAllDeclaredMethods(type);
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

		for (MethodDeclaration method : methods){
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

	private StringBuilder getParameterizedVariableName(MethodDeclaration declaration){
		StringBuilder ret = new StringBuilder();
		for (ParameterDeclaration pd : declaration.getParameters()){
			ret.append('_');
			ret.append(stripStrategyVariableName(pd.getType().toString()));
			//ret.append(pd.getType().getClass().getSimpleName());
			ret.append(pd.getSimpleName());
		}
		return ret;
	}

	/**
	 * Returns the name of the failing strategy variable for a method.
	 * @param declaration
	 * @return
	 */
	protected String getFailingStrategyVariableName(MethodDeclaration declaration){
		StringBuilder ret = new StringBuilder(declaration.getSimpleName()).append("FailingStrategy");
		ret.append(getParameterizedVariableName(declaration));
		return ret.toString();
	}
	/**
	 * Returns the name for concurrency control strategy variable
	 * @param declaration
	 * @return
	 */
	protected String getCCStrategyVariableName(MethodDeclaration declaration){
		StringBuilder ret = new StringBuilder(declaration.getSimpleName()).append("CCStrategy");
		ret.append(getParameterizedVariableName(declaration));
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
	
	
	protected void writeConcurrencyControlCreationMethod(TranslatedCCAnnotation cca){
		writeString("private ConcurrencyControlStrategy createConcurrencyControlStrategy"+cca.getOrder()+"(){");
		increaseIdent();
		writeStatement("ConcurrencyControlStrategy strat = new "+cca.getStrategyClass()+"()");
		writeStatement("strat.customize("+quote(cca.getParameter())+")");
		writeStatement("return strat");
		closeBlock();
	}
	
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
	 * @param decl
	 * @return
	 */
	protected boolean isVoidReturn(MethodDeclaration decl){
		return decl.getReturnType().toString().equals("void");
	}

	protected String getMethodRouterName(MethodDeclaration declaration){
		return declaration.getSimpleName()+"Router";
	}

	
}
