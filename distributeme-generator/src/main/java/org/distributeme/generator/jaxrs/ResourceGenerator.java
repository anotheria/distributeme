package org.distributeme.generator.jaxrs;

import net.anotheria.moskito.core.dynamic.MoskitoInvokationProxy;
import net.anotheria.moskito.core.logging.DefaultStatsLogger;
import net.anotheria.moskito.core.logging.IntervalStatsLogger;
import net.anotheria.moskito.core.logging.SLF4JLogOutput;
import net.anotheria.moskito.core.predefined.ServiceStatsCallHandler;
import net.anotheria.moskito.core.predefined.ServiceStatsFactory;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.IProducerRegistryAPI;
import net.anotheria.moskito.core.registry.ProducerRegistryAPIFactory;
import net.anotheria.moskito.core.stats.DefaultIntervals;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.core.Defaults;
import org.distributeme.core.ServerSideCallContext;
import org.distributeme.core.ServiceLocator;
import org.distributeme.core.Verbosity;
import org.distributeme.core.concurrencycontrol.ConcurrencyControlStrategy;
import org.distributeme.core.interceptor.*;
import org.distributeme.core.lifecycle.HealthStatus;
import org.distributeme.core.lifecycle.LifecycleAware;
import org.distributeme.core.util.VoidMarker;
import org.distributeme.generator.AbstractGenerator;
import org.distributeme.generator.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Generator for RMI based skeletons. 
 * @author lrosenberg
 */
public class ResourceGenerator extends AbstractGenerator implements Generator{

	public ResourceGenerator(ProcessingEnvironment environment) {
		super(environment);
	}

	@Override
	public void generate(TypeElement type, Filer filer, Map<String,String> options) throws IOException{
		JavaFileObject sourceFile = filer.createSourceFile(getPackageName(type) + '.' + getResourceName(type));
		Writer writer = sourceFile.openWriter();
		setWriter(writer);
		
		DistributeMe ann = type.getAnnotation(DistributeMe.class);
		writePackage(type);
		writeAnalyzerComments(type);
		emptyline();
		writeImport(Logger.class);
		writeImport(LoggerFactory.class);
		writeImport(List.class);
		writeImport(Map.class);
		writeImport(ArrayList.class);
		writeImport(Collections.class);
		writeImport(Verbosity.class);
		writeImport(Defaults.class);
		writeImport(ServerSideCallContext.class);
		writeImport(ServerSideRequestInterceptor.class);
		writeImport(InterceptorResponse.class);
		writeImport(InterceptionContext.class);
		writeImport(InterceptorRegistry.class);
		writeImport(InterceptionPhase.class);
		writeImport(ConcurrencyControlStrategy.class);
		writeImport(ServiceLocator.class);
		if (ann.moskitoSupport()){
			writeImport(MoskitoInvokationProxy.class);
			writeImport(DefaultStatsLogger.class);
			writeImport(IntervalStatsLogger.class);
			writeImport(SLF4JLogOutput.class);
			writeImport(DefaultIntervals.class);
			writeImport(ServiceStatsCallHandler.class);
			writeImport(ServiceStatsFactory.class);
			writeImport(IProducerRegistryAPI.class);
			writeImport(ProducerRegistryAPIFactory.class);
			writeImport(IStatsProducer.class);
		}
		writeImport(MediaType.class);
		writeImport(Path.class);
		writeImport(POST.class);
		writeImport(Produces.class);
		writeImport(Consumes.class);

		emptyline();
		
		//check if the service is LifecycleAware
		boolean lifecycleAware = false;

		List<? extends TypeMirror> superInterfaces = type.getInterfaces();
		for (Object si : superInterfaces){
			if (si.toString().equals("org.distributeme.core.lifecycle.LifecycleAware")){
				lifecycleAware = true;
				break;
			}
		}
		
		if (lifecycleAware){
			writeImport(LifecycleAware.class);
			writeImport(HealthStatus.class);
		}
		

		writeString("@Path(\"/"+ type.getSimpleName() +"\")");
		writeString("@Produces(MediaType.APPLICATION_JSON)");
		writeString("@Consumes(MediaType.APPLICATION_JSON)");

		writeString("public class " + getResourceName(type) + (lifecycleAware ? " implements  LifecycleAware" : "") + " {");
		increaseIdent();
		emptyline();
		writeStatement("private static Logger log = LoggerFactory.getLogger("+getResourceName(type)+".class)");
		emptyline();

		writeStatement("private "+type.getQualifiedName()+" implementation");
		emptyline();
		writeStatement("private long lastAccess");
		writeStatement("private long created");
		emptyline();
		
		/***** CONCURRENCY START *****/
		List<TranslatedCCAnnotation> concurrencyControlAnnotations = writeConcurrencyControlDeclarations(type);
		/***** CONCURRENCY END *****/
		
		writeString("public "+getResourceName(type)+"(){");
		increaseIdent();
		writeStatement("this(ServiceLocator.getLocal("+type.getQualifiedName()+".class))");

		closeBlockNEW();
		emptyline();
		writeString("public "+getResourceName(type)+ '(' +type.getQualifiedName()+" anImplementation){");
		increaseIdent();
		writeStatement("created = System.currentTimeMillis()");
		if (ann.moskitoSupport()){
			
			writeString("MoskitoInvokationProxy proxy = new MoskitoInvokationProxy(");
			writeIncreasedString("anImplementation,");
			writeIncreasedString("new ServiceStatsCallHandler(),");
			writeIncreasedString("new ServiceStatsFactory(),");
			writeIncreasedString(quote(type.getSimpleName().toString())+", ");
			writeIncreasedString(quote("service")+ ',');
			writeIncreasedString(quote("default")+ ',');
			writeIncreasedString(getImplementedInterfacesAsString(type));
			writeString(");");
		
			writeStatement("implementation = ("+type.getQualifiedName()+") proxy.createProxy()");
			writeString("// add moskito logger");
			writeStatement("new DefaultStatsLogger(proxy.getProducer(), new SLF4JLogOutput(LoggerFactory.getLogger(\"moskito.custom.default\")))");
			writeStatement("new IntervalStatsLogger(proxy.getProducer(), DefaultIntervals.FIVE_MINUTES, new SLF4JLogOutput(LoggerFactory.getLogger(\"moskito.custom.5m\")))");
			writeStatement("new IntervalStatsLogger(proxy.getProducer(), DefaultIntervals.FIFTEEN_MINUTES, new SLF4JLogOutput(LoggerFactory.getLogger(\"moskito.custom.15m\")))");
			writeStatement("new IntervalStatsLogger(proxy.getProducer(), DefaultIntervals.ONE_HOUR, new SLF4JLogOutput(LoggerFactory.getLogger(\"moskito.custom.1h\")))");
			writeStatement("new IntervalStatsLogger(proxy.getProducer(), DefaultIntervals.ONE_DAY, new SLF4JLogOutput(LoggerFactory.getLogger(\"moskito.custom.1d\")))");
			writeString("//end moskito logger");
			
			//add moskito BI loggers.
			writeCommentLine("//ADD LOGGING FOR ALL BUILTIN PRODUCERS");
			writeStatement("IProducerRegistryAPI api = new ProducerRegistryAPIFactory().createProducerRegistryAPI()");
			writeStatement("List<IStatsProducer> stats = api.getAllProducersBySubsystem(\"builtin\")");
			
			writeString("for (IStatsProducer producer : stats){");
				increaseIdent(); 
				writeStatement("new DefaultStatsLogger(producer, new SLF4JLogOutput(LoggerFactory.getLogger(\"moskito.bi.default\")))");
				writeStatement("new IntervalStatsLogger(producer, DefaultIntervals.FIVE_MINUTES, new SLF4JLogOutput(LoggerFactory.getLogger(\"moskito.bi.5m\")))");
				writeStatement("new IntervalStatsLogger(producer, DefaultIntervals.FIFTEEN_MINUTES, new SLF4JLogOutput(LoggerFactory.getLogger(\"moskito.bi.15m\")))");
				writeStatement("new IntervalStatsLogger(producer, DefaultIntervals.ONE_HOUR, new SLF4JLogOutput(LoggerFactory.getLogger(\"moskito.bi.1h\")))");
				writeStatement("new IntervalStatsLogger(producer, DefaultIntervals.ONE_DAY, new SLF4JLogOutput(LoggerFactory.getLogger(\"moskito.bi.1d\")))");
			closeBlockNEW();
			//END BUILTIN PRODUCERS LOGGING
			
			
		}else{
			writeStatement("implementation = anImplementation");
		}
		closeBlockNEW();
		emptyline();
		
		
		//WRITING METHODS
		Collection<? extends ExecutableElement> methods = getAllDeclaredMethods(type);
		for (ExecutableElement method : methods){
			String methodDecl = getResourceSkeletonMethodDeclaration(method);
			List<? extends TypeMirror> exceptions = method.getThrownTypes();
			writeString("@POST @Path(\""+method.getSimpleName()+"\")");
			writeString("public "+methodDecl+ '{');
			increaseIdent();
			writeString("Map <?,?> __transportableCallContext = Collections.emptyMap(); //not used NOW!");
			writeStatement("lastAccess = System.currentTimeMillis()");
			writeStatement("ServerSideCallContext diMeCallContext = new ServerSideCallContext("+quote(method.getSimpleName())+", __transportableCallContext)");
			writeStatement("diMeCallContext.setServiceId("+getConstantsName(type)+".getServiceId())");
			writeStatement("ArrayList<Object> diMeParameters = new ArrayList<Object>()");
			Collection<? extends VariableElement> parameters = method.getParameters();
			for (VariableElement p : parameters){
				writeStatement("diMeParameters.add("+p.getSimpleName()+ ')');
			}
			writeStatement("diMeCallContext.setParameters(diMeParameters)");
			writeStatement("InterceptionContext diMeInterceptionContext = new InterceptionContext()");
			writeCommentLine("Initialize interceptors");
			writeStatement("List<ServerSideRequestInterceptor> diMeInterceptors = InterceptorRegistry.getInstance().getServerSideRequestInterceptors()");

			//add return handling.
			emptyline();
			writeStatement("ArrayList __return = new ArrayList()");
			emptyline();
			
			
			writeInterceptionBlock(InterceptionPhase.BEFORE_SERVANT_CALL, method);
			
			//concurrency control
			writeCommentLine("Concurrency control, server side - ");
			writeStatement(getCCStrategyVariableName(method)+".notifyServerSideCallStarted(diMeCallContext)");
			emptyline();
			
			//if(exceptions.size() > 0){
				writeString("try{");
				increaseIdent();
			//}
			String call = "";
			if (!method.getReturnType().toString().equals("void"))
				call +="Object __result = ";
			call += "implementation."+method.getSimpleName();
			String paramCall = "";
			for (VariableElement p : parameters){
				if (!paramCall.isEmpty())
					paramCall += ", ";
				paramCall += p.getSimpleName();
			}
			call += '(' +paramCall+");";
			writeString(call);

			if (method.getReturnType().toString().equals("void")){
				writeStatement("__return.add("+VoidMarker.class.getName()+".VOID)");
			}else{
				writeStatement("__return.add(__result)");
				writeStatement("diMeInterceptionContext.setReturnValue(__result)");
			}
			writeStatement("__return.add(diMeCallContext.getTransportableCallContext())");
			writeInterceptionBlock(InterceptionPhase.AFTER_SERVANT_CALL, method);
			writeStatement("return __return");

			decreaseIdent();

			for (TypeMirror exc : exceptions) {
				writeString("}catch(" + exc + " e){");
				increaseIdent();
				writeString("if (Verbosity.logServerSideExceptions())");
				writeIncreasedStatement("log.error(" + quote(method.getSimpleName() + "()") + ", e)");
				decreaseIdent();
				writeIncreasedStatement("throw(e)");
			}
			writeString("}finally{");
			writeIncreasedStatement(getCCStrategyVariableName(method)+".notifyServerSideCallFinished(diMeCallContext)");
			writeString("}");
			
			closeBlockNEW();//method end
			emptyline();
		}
		
		//service adapter methods.
		writeCommentLine("Service adapter methods");
		writeString("public long getCreationTimestamp(){ return created; }");
		writeString("public long getLastAccessTimestamp(){ return lastAccess; }");
		emptyline();
		
		//lifecycleaware methods
		if( lifecycleAware){
			writeCommentLine("Support for LifecycleAware");
			writeString("public HealthStatus getHealthStatus(){ return implementation.getHealthStatus(); }");
		}
		
		//write concurrency control strategy created methods
		for (TranslatedCCAnnotation cca: concurrencyControlAnnotations){
			writeConcurrencyControlCreationMethod(cca);
		}

		
		closeBlockNEW();
		
		
		writer.flush();
		writer.close();
	}
	
	private void writeInterceptionBlock(InterceptionPhase phase, ExecutableElement method){
		//boolean afterCall = phase == InterceptionPhase.AFTER_SERVANT_CALL; 
		writeStatement("diMeInterceptionContext.setCurrentPhase(InterceptionPhase."+ phase + ')');
		writeString("for (ServerSideRequestInterceptor interceptor : diMeInterceptors){");
		increaseIdent();
		writeStatement("InterceptorResponse interceptorResponse = interceptor."+interceptionPhaseToMethod(phase)+"(diMeCallContext, diMeInterceptionContext)");
		writeString("switch(interceptorResponse.getCommand()){");
		writeString("case ABORT:");
		increaseIdent();
		writeString("if (interceptorResponse.getException() instanceof RuntimeException)");
		writeIncreasedStatement("throw (RuntimeException) interceptorResponse.getException()");
		for (TypeMirror type : method.getThrownTypes()){
			writeString("if (interceptorResponse.getException() instanceof "+ type + ')');
			writeIncreasedStatement("throw ("+ type +") interceptorResponse.getException()");
		}
		writeStatement("throw new RuntimeException("+quote("Interceptor exception")+",interceptorResponse.getException())");
		decreaseIdent();
		writeString("case RETURN:");
		writeIncreasedStatement("__return.set(0, interceptorResponse.getReturnValue())");
		writeIncreasedStatement("diMeInterceptionContext.setReturnValue(interceptorResponse.getReturnValue())");
		writeIncreasedStatement("break");
		writeString("case OVERWRITE_RETURN_AND_CONTINUE:");
		writeIncreasedStatement("__return.set(0, interceptorResponse.getReturnValue())");
		writeIncreasedStatement("diMeInterceptionContext.setReturnValue(interceptorResponse.getReturnValue())");
		writeIncreasedStatement("break");
		writeString("case CONTINUE:");
		writeIncreasedStatement("break");
		increaseIdent();
		closeBlock("switch");
			
		closeBlock("for");
	}
}
