package org.distributeme.generator.ws;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.moskito.core.dynamic.MoskitoInvokationProxy;
import net.anotheria.moskito.core.logging.DefaultStatsLogger;
import net.anotheria.moskito.core.logging.IntervalStatsLogger;
import net.anotheria.moskito.core.logging.Log4JOutput;
import net.anotheria.moskito.core.predefined.ServiceStatsCallHandler;
import net.anotheria.moskito.core.predefined.ServiceStatsFactory;
import org.distributeme.annotation.WebServiceMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

/**
 * <p>ServiceProxyGenerator class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class ServiceProxyGenerator extends WSStructureGenerator implements WebServiceMeGenerator {

	/**
	 * <p>Constructor for ServiceProxyGenerator.</p>
	 *
	 * @param environment a {@link javax.annotation.processing.ProcessingEnvironment} object.
	 */
	public ServiceProxyGenerator(ProcessingEnvironment environment) {
		super(environment);
	}

	/** {@inheritDoc} */
	@Override
	public void generate(TypeElement type) {
		PrintWriter writer = createSourceFile(type.getSimpleName().toString().toString(), getWSProxyPackage(type), getWSProxySimpleName(type));
		setWriter(writer);

		WebServiceMe ann = type.getAnnotation(WebServiceMe.class);

		// package
		writeString("package " + getWSProxyPackage(type) + ";");
		emptyline();
		// imports
		writeImport(getPackageOf(type).getQualifiedName().toString(), type.getSimpleName().toString());
		writeImport(Logger.class);
		writeImport(LoggerFactory.class);
		if (ann.moskitoSupport()) {
			writeImport(MoskitoInvokationProxy.class);
			writeImport(DefaultStatsLogger.class);
			writeImport(IntervalStatsLogger.class);
			writeImport(Log4JOutput.class);
			writeImport(ServiceStatsCallHandler.class);
			writeImport(ServiceStatsFactory.class);
			writeImport(MetaFactory.class);
			writeImport(Extension.class);
			writeImport("javax.jws.WebService");
		}
		emptyline();
		// class
		writeString("@WebService");
		writeString("public class " + getWSProxySimpleName(type) + " implements " + type.getSimpleName().toString() + " {");
		increaseIdent();
		emptyline();
		// variables
		writeStatement("private static final Logger LOGGER = Logger.getLogger(" + getWSProxySimpleName(type) + ".class)");
		emptyline();
		writeStatement("private " + type.getQualifiedName() + " implementation");
		emptyline();
		// constructor
		writeString("public " + getWSProxySimpleName(type) + "() {");
		increaseIdent();
		writeStatement("init()");
		if (ann.moskitoSupport()) {
			writeString("MoskitoInvokationProxy proxy = new MoskitoInvokationProxy(");
			writeIncreasedString("implementation,");
			writeIncreasedString("new ServiceStatsCallHandler(),");
			writeIncreasedString("new ServiceStatsFactory(),");
			writeIncreasedString(quote(type.getSimpleName().toString()) + ", ");
			writeIncreasedString(quote("service") + ",");
			writeIncreasedString(quote("default") + ",");
			writeIncreasedString(getImplementedInterfacesAsString(type));
			writeString(");");
			emptyline();
			writeStatement("implementation = (" + type.getQualifiedName() + ") proxy.createProxy()");
			emptyline();
			writeStatement("LoggerUtil.createSLF4JDefaultAndIntervalStatsLogger(proxy.getProducer())");
		}
		closeBlock();
		emptyline();
		// custom methods
		writeString("private void init() {");
		increaseIdent();
		writeString("try {");
		increaseIdent();
		String[] initCode = ann.initcode();
		for (String s : initCode) {
			writeString(s);
		}
		decreaseIdent();
		writeIncreasedStatement("implementation = MetaFactory.get(" + type.getQualifiedName() + ".class);");
		writeString("} catch (Exception e) {");
		writeIncreasedStatement("LOGGER.error(\"init()\", e)");
		writeIncreasedStatement("throw new RuntimeException(e)");
		writeString("}");
		closeBlock();
		// methods
		Collection<? extends ExecutableElement> methods = getAllDeclaredMethods(type);
		for (ExecutableElement method : methods) {
			String methodDecl = getStubMethodDeclaration(method);
			List<? extends TypeMirror> exceptions = method.getThrownTypes();
			writeString("@Override");
			writeString("public " + methodDecl + " {");
			increaseIdent();
			if (exceptions.size() > 0) {
				writeString("try{");
				increaseIdent();
			}
			String call = "";
			if (!method.getReturnType().toString().equals("void"))
				call += "return ";
			call += "implementation." + method.getSimpleName();
			Collection<? extends VariableElement> parameters = method.getParameters();
			String paramCall = "";
			for (VariableElement p : parameters) {
				if (paramCall.length() != 0)
					paramCall += ", ";
				paramCall += p.getSimpleName();
			}
			call += "(" + paramCall + ");";
			writeString(call);
			if (exceptions.size() > 0) {
				decreaseIdent();

				for (TypeMirror exc : exceptions) {
					writeString("} catch (" + exc.toString() + " e) {");
					writeIncreasedStatement("LOGGER.error(" + quote(method.getSimpleName() + "()") + ", e)");
					writeIncreasedStatement("throw(e)");
				}
				writeString("}");
			}

			closeBlock();
			emptyline();
		}
		// end
		closeBlock();
		closeWriter(writer);
	}

}
