package org.distributeme.generator;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.DontRoute;
import org.distributeme.annotation.FailBy;
import org.distributeme.annotation.Route;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.Defaults;
import org.distributeme.core.DiscoveryMode;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.concurrencycontrol.ConcurrencyControlStrategy;
import org.distributeme.core.exception.DistributemeRuntimeException;
import org.distributeme.core.exception.NoConnectionToServerException;
import org.distributeme.core.exception.ServiceUnavailableException;
import org.distributeme.core.failing.FailDecision;
import org.distributeme.core.failing.FailingStrategy;
import org.distributeme.core.interceptor.ClientSideRequestInterceptor;
import org.distributeme.core.interceptor.FailedByInterceptorException;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptionPhase;
import org.distributeme.core.interceptor.InterceptorRegistry;
import org.distributeme.core.interceptor.InterceptorResponse;
import org.distributeme.generator.logwriter.LogWriter;
import org.distributeme.generator.logwriter.SysErrorLogWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generator for RMI based stubs.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class StubGenerator extends AbstractStubGenerator implements Generator{
	
	private static Logger log = LoggerFactory.getLogger(StubGenerator.class);

	/**
	 * <p>Constructor for StubGenerator.</p>
	 *
	 * @param environment a {@link javax.annotation.processing.ProcessingEnvironment} object.
	 */
	public StubGenerator(ProcessingEnvironment environment) {
		super(environment);
	}

	/** {@inheritDoc} */
	@Override
	public void generate(TypeElement type, Filer filer, Map<String,String> options) throws IOException{
		
		//System.out.println("%%%\nStarting generating "+type+"\n\n");
		
		JavaFileObject sourceFile = filer.createSourceFile(getPackageName(type)+"."+getStubName(type));
        PrintWriter writer = new PrintWriter(sourceFile.openWriter());
		setWriter(writer);
		
		writePackage(type);
		writeAnalyzerComments(type);
		emptyline();
		writeImport(List.class);
		writeImport(ArrayList.class);
		writeImport(HashMap.class);
		writeImport(ConcurrentMap.class);
		writeImport(ConcurrentHashMap.class);
		writeImport(Logger.class);
		writeImport(RemoteException.class);
		writeImport(NotBoundException.class);
		writeImport(org.distributeme.core.RegistryUtil.class);
		writeImport("java.rmi.registry.LocateRegistry");
		writeImport("java.rmi.registry.Registry");
		writeImport(ServiceDescriptor.class);
		writeImport("org.distributeme.core.ServiceDescriptor.Protocol");
		writeImport(DiscoveryMode.class);
		writeImport(FailingStrategy.class);
		writeImport(ConcurrencyControlStrategy.class);
		writeImport(FailDecision.class);
		writeImport(ClientSideCallContext.class);
		writeImport(DistributemeRuntimeException.class);
		writeImport(NoConnectionToServerException.class);
		writeImport(ServiceUnavailableException.class);
		writeImport(Defaults.class);
		writeImport(ClientSideRequestInterceptor.class);
		writeImport(InterceptorRegistry.class);
		writeImport(InterceptorResponse.class);
		writeImport(InterceptionContext.class);
		writeImport(InterceptionPhase.class);
		writeImport(FailedByInterceptorException.class);
		emptyline();
		
		writeString("public class "+getStubName(type)+" implements "+type.getQualifiedName()+"{");
		increaseIdent();
		emptyline();
		
		
		//logging annotation resolving
		LogWriter logWriter = null;
		//System.out.println("====================");
		try{
			AnnotationMirror logWriterMirror = findMirror(type, DistributeMe.class);
			AnnotationValue  logWriterClazzValue = findLogWriterValue(logWriterMirror);
			//System.out.println("Type: "+type+", Mirror "+logWriterMirror+", clazzValue: "+logWriterClazzValue+", allvalues: "+logWriterMirror.getElementValues());
			String logWriterClazzName = null;
			if (logWriterClazzValue==null){
				logWriterClazzName = SysErrorLogWriter.class.getName();
			}else{
				logWriterClazzName = ""+logWriterClazzValue.getValue();
			}
			
			logWriter = (LogWriter)(Class.forName(logWriterClazzName).newInstance());
			//System.out.println("@@@ created log writer "+logWriter);
		}catch(Exception e){
			log.warn("Still have this stupid exception...", e);
			logWriter = new SysErrorLogWriter();
		}
		//System.out.println("====================");

		String loggerInitialization = logWriter.createLoggerInitialization(getStubName(type));
		if (loggerInitialization!=null && loggerInitialization.length()>0)
			writeStatement(loggerInitialization);
		emptyline();
	
		
		//this variable indicates that clazzWideRouting is on.
		boolean clazzWideRoutingEnabled = findMirror(type, Route.class)!=null;

		
		writeStatement("private volatile ConcurrentMap<String,"+getRemoteInterfaceName(type)+"> delegates = new ConcurrentHashMap<String,"+getRemoteInterfaceName(type)+">()");
		emptyline();
		writeStatement("private DiscoveryMode discoveryMode = DiscoveryMode.AUTO");
		emptyline();
		
		//NEW ROUTER HANDLING
		List<TranslatedRouterAnnotation> routerAnnotations = writeRouterDeclarations(type);

		//OLD ROUTER HANDLING
		Collection<? extends ExecutableElement> methods = getAllDeclaredMethods(type);
		Set<ExecutableElement> routedMethods = new HashSet<ExecutableElement>();
		for (ExecutableElement method : methods){
			AnnotationMirror methodRoute = findMirror(method, Route.class);
			if (methodRoute!=null){
				//System.out.println("Will write "+Router.class.getName()+" "+getMethodRouterName(method));
				routedMethods.add(method);
			}
		}
		emptyline();
		
		/***** FAILING START *****/
		writeCommentLine("Failing");
		writeCommentLine("Class wide failing strategy ");
		AnnotationMirror clazzWideFailingStrategyAnnotation = findMirror(type, FailBy.class);
		String clazzWideFailingStrategyName = null;
		if (clazzWideFailingStrategyAnnotation != null){
			clazzWideFailingStrategyName = ""+findMethodValue(clazzWideFailingStrategyAnnotation, "strategyClass").getValue();
		}

		//start clazz wide failing
		if (clazzWideFailingStrategyAnnotation!=null){
			//check if we should mirror the router
			FailBy ann = type.getAnnotation(FailBy.class);
			if (ann.reuseRouter()){
				if (!clazzWideRoutingEnabled){
					throw new AssertionError("Can't reuse router if no @Route router is configured.");
				}
				writeStatement("private FailingStrategy clazzWideFailingStrategy = (FailingStrategy) clazzWideRouter");
			}else{
				if (clazzWideFailingStrategyName!=null)
					writeStatement("private FailingStrategy clazzWideFailingStrategy = new "+clazzWideFailingStrategyName+"()");
			}
		}else{
			writeStatement("private FailingStrategy clazzWideFailingStrategy = "+Defaults.class.getSimpleName()+".getDefaultFailingStrategy()");
		}
		emptyline();
		//end clazz wide failing

		for (ExecutableElement method : methods){
			AnnotationMirror methodFailingStrategyAnnotation = findMirror(method, FailBy.class);
			if (methodFailingStrategyAnnotation != null){
				FailBy ann = method.getAnnotation(FailBy.class);
				if (ann.reuseRouter()){
					if (!routedMethods.contains(method) && !clazzWideRoutingEnabled)
						throw new AssertionError("Can't reuse router in method "+method+", because no router is configured.");
					String targetRouterName = routedMethods.contains(method) ? getMethodRouterName(method) : "clazzWideRouter";
					writeStatement("private FailingStrategy "+getFailingStrategyVariableName(method)+" = (FailingStrategy) "+targetRouterName);
				}else{
					String methodFailingStrategyName = ""+findMethodValue(methodFailingStrategyAnnotation, "strategyClass").getValue();
					writeStatement("private FailingStrategy "+getFailingStrategyVariableName(method)+" = new "+methodFailingStrategyName+"()");
				}
			}else{
				writeStatement("private FailingStrategy "+getFailingStrategyVariableName(method)+" = clazzWideFailingStrategy");				
			}
			
		}
		writeCommentLine("Failing end");
		emptyline();
		/***** FAILING END *****/
		
		/***** CONCURRENCY START *****/
		List<TranslatedCCAnnotation> concurrencyControlAnnotations = writeConcurrencyControlDeclarations(type);
		/***** CONCURRENCY END *****/
		
		
		
		//create AUTO constructor
		writeString("public "+getStubName(type)+"(){");
		increaseIdent();
		writeStatement("discoveryMode = DiscoveryMode.AUTO");
		closeBlock();
		emptyline();
	
		//create MANUAL constructor
		writeStatement("private ServiceDescriptor manuallySetDescriptor");
		writeStatement("private "+getRemoteInterfaceName(type)+" manuallySetTarget");
		emptyline();
		writeString("public "+getStubName(type)+"(ServiceDescriptor target){");
		increaseIdent();
		writeStatement("discoveryMode = DiscoveryMode.MANUAL");
		writeStatement("manuallySetDescriptor = target");
		writeString("try{");
		writeIncreasedStatement("manuallySetTarget = lookup(manuallySetDescriptor)");
		writeString("}catch(NoConnectionToServerException e){");
		writeIncreasedStatement("throw new IllegalStateException("+quote("Can not resolve manually set reference")+", e)");
		closeBlockWithoutIdent();
		closeBlock();
		emptyline();
		
		////////// METHODS ///////////
		for (ExecutableElement method : methods){
			writeString("public "+getStubMethodDeclaration(method)+"{");
			increaseIdent();
			StringBuilder callToPrivate = new StringBuilder(method.getSimpleName()+"(");
			for (VariableElement p : method.getParameters()){
				callToPrivate.append(p.getSimpleName());
				callToPrivate.append(", ");
			}
			writeStatement((isVoidReturn(method) ? "" : "return ")+callToPrivate.toString()+"(ClientSideCallContext)null)");
			closeBlock("public "+getStubMethodDeclaration(method));
			emptyline();

			
			String methodDecl = getInternalStubMethodDeclaration(method);
			writeString("private "+methodDecl+"{");
			increaseIdent();
			writeStatement("List __fromServerSide = null");
			writeStatement("Exception exceptionInMethod = null");
			writeCommentLine("This flag is used by the interceptor logic to mark a request es failed, even it is not.");
			writeStatement("boolean diMeForceFailing = false");
			//if true the call will not be executed, instead we jump into failing.
			writeStatement("boolean abortAndFail = false");
			writeString("if (diMeCallContext == null)");
			writeIncreasedStatement("diMeCallContext = new ClientSideCallContext("+quote(method.getSimpleName())+")");
			writeString("if (discoveryMode == DiscoveryMode.MANUAL) {");
			writeIncreasedStatement("diMeCallContext.setServiceId(manuallySetDescriptor.getServiceId())");
			writeString("}");
			writeString("if (discoveryMode==DiscoveryMode.AUTO && diMeCallContext.getServiceId()==null)");
			writeIncreasedStatement("diMeCallContext.setServiceId("+getConstantsName(type)+".getServiceId())");
			emptyline();
			writeStatement("HashMap __transportableCallContext = diMeCallContext.getTransportableCallContext()");
			
			//interceptors, phase 1
			boolean interceptionEnabled = true; //later can be configured via annotation.
			writeCommentLine("Initialize interceptors");
			writeStatement("List<ClientSideRequestInterceptor> diMeInterceptors = InterceptorRegistry.getInstance().getClientSideRequestInterceptors()");
			writeStatement("InterceptionContext diMeInterceptionContext = new InterceptionContext()");
//			writeInterceptionBlock(InterceptionPhase.BEFORE_CLIENT, method);

			emptyline();
			//concurrency control
			writeCommentLine("Concurrency control, client side - start");
			writeStatement(getCCStrategyVariableName(method)+".notifyClientSideCallStarted(diMeCallContext)");

			emptyline();
			writeString("try{");
			increaseIdent();

			if (interceptionEnabled){
				//create parameters
				writeStatement("ArrayList<Object> diMeParameters = new ArrayList<Object>()");
				Collection<? extends VariableElement> parameters = method.getParameters();
				for (VariableElement p : parameters){
					writeStatement("diMeParameters.add("+p.getSimpleName()+")");
				}
				writeStatement("diMeCallContext.setParameters(diMeParameters)");
			}
			
//			boolean doRoute = false;
			AnnotationMirror methodMirror = findMirror(method, DontRoute.class);
			if (methodMirror!=null){
				writeCommentLine("explicitly skipping routing for method "+method);
			}else{
				String routerName = null;
				if (clazzWideRoutingEnabled)
					routerName = "clazzWideRouter";
				if (routedMethods.contains(method)){
					routerName = getMethodRouterName(method);
				}
				if (routerName!=null ){
//					doRoute = true;
					if (!interceptionEnabled){
						//this means that we have to create parameters for context
						writeStatement("ArrayList<Object> diMeParameters = new ArrayList<Object>()");
						Collection<? extends VariableElement> parameters = method.getParameters();
						for (VariableElement p : parameters){
							writeStatement("diMeParameters.add("+p.getSimpleName()+")");
						}
						writeStatement("diMeCallContext.setParameters(diMeParameters)");
						
					}
					writeStatement("diMeCallContext.setServiceId("+routerName+".getServiceIdForCall(diMeCallContext))");
				}
			}
			
			writeInterceptionBlock(InterceptionPhase.BEFORE_SERVICE_CALL, method);
			
			//now re-parse parameters
			writeCommentLine("parse parameters again in case an interceptor modified them");
			Collection<? extends VariableElement> parameters = method.getParameters();
			int parameterCounter = 0;
			for (VariableElement p : parameters){
				writeStatement(p.getSimpleName()+" = " +convertReturnValue(p.asType(), "diMeParameters.get("+(parameterCounter++)+")"));
				//writeStatement("diMeParameters.add("+p.getSimpleName()+")");
			}
			
			String call = "__fromServerSide = getDelegate(diMeCallContext.getServiceId())."+method.getSimpleName();
			StringBuilder paramCall = new StringBuilder();
			for (VariableElement p : parameters){
				if (paramCall.length()!=0)
					paramCall.append(", ");
				paramCall.append(p.getSimpleName());
			}
			if (paramCall.length()>0)
				paramCall.append(", ");
			paramCall.append(" __transportableCallContext");
			call += "("+paramCall.toString()+");";
			writeString("if (!abortAndFail){");
			increaseIdent();
			writeString(call);
			writeStatement("__transportableCallContext.putAll(((HashMap)__fromServerSide.get(1)))");
			if (isVoidReturn(method)){
				writeStatement("return");
			}else{
				writeStatement("return "+convertReturnValue(method.getReturnType(), "__fromServerSide.get(0)"));
			}
			closeBlock();
			decreaseIdent();
			writeString("}catch(RemoteException e){");
			increaseIdent();
			writeCommentLine("handle exceptions properly");
			writeStatement(logWriter.createExceptionOutput(quote(method.getSimpleName()+"(...)"), "e"));
			writeStatement("notifyDelegateFailed(diMeCallContext.getServiceId())");
			writeStatement("exceptionInMethod = e");
			decreaseIdent();
			writeString("}catch(NoConnectionToServerException e){");
			writeIncreasedStatement("exceptionInMethod = e");
			writeString("}finally{");
			increaseIdent();
			//concurrency control
			writeCommentLine("Concurrency control, client side - end");
			writeStatement(getCCStrategyVariableName(method) + ".notifyClientSideCallFinished(diMeCallContext)");
			writeInterceptionBlock(InterceptionPhase.AFTER_SERVICE_CALL, method);
			emptyline();
			closeBlock("finally");
			
			
			
			//failing
			writeCommentLine("Failing");
			writeString("if (exceptionInMethod!=null || diMeForceFailing || abortAndFail){");
			increaseIdent();
			writeStatement("FailDecision failDecision = "+getFailingStrategyVariableName(method)+".callFailed(diMeCallContext)");
			writeString("if (failDecision.getTargetService()!=null)");
			writeIncreasedStatement("diMeCallContext.setServiceId(failDecision.getTargetService())");
			writeString("switch(failDecision.getReaction()){");
			increaseIdent();
			writeString("case RETRY:");
			if (!isVoidReturn(method)){
				writeIncreasedStatement("return "+callToPrivate+"diMeCallContext.increaseCallCount())");
			}else{
				writeIncreasedStatement(callToPrivate+"diMeCallContext.increaseCallCount())");
				writeIncreasedStatement("return");
			}
			
			writeString("case RETRYONCE:");
			increaseIdent();
			writeCommentLine("Only retry if its the first call");
			writeString("if (!diMeCallContext.isFirstCall())");
			writeIncreasedStatement("break");
			if (!isVoidReturn(method)){
				writeStatement("return "+callToPrivate+"diMeCallContext.increaseCallCount())");
			}else{
				writeStatement(callToPrivate+"diMeCallContext.increaseCallCount())");
				writeStatement("return");
			}
			decreaseIdent();
			

			writeString("case FAIL:");
			writeString("default:");
			writeCommentLine("Fail or default is to do nothing at all and let the request fail");
			closeBlock("switch(failDecision)");
			
			closeBlock();
			writeCommentLine("fail through, if we are here, we must have had an exception before.");
			writeString("if (exceptionInMethod == null)");
			writeIncreasedStatement("throw new AssertionError("+quote("Exception must have been thrown before, but it wasn't, framework error!")+")");
			writeStatement("throw mapException(exceptionInMethod)");
			closeBlock();
			emptyline();
		}

		writeCommentLine("according to findbugs this method is never used, so we remove it for now reduce warnings.");
		emptyline();
		writeString("//private void notifyDelegateFailed(){");
		increaseIdent();
		writeStatement("//notifyDelegateFailed("+getConstantsName(type)+".getServiceId())");
		//closeBlock();
		decreaseIdent();
		writeString("//}");
		emptyline();
		
		writeString("private void notifyDelegateFailed(String serviceId){");
		increaseIdent();
		writeString("if (discoveryMode==DiscoveryMode.MANUAL){");
		writeIncreasedStatement("manuallySetTarget = null");
		writeIncreasedStatement("return");
		writeString("}");
		writeString("if (serviceId!=null)");
		writeIncreasedStatement("delegates.remove(serviceId)");
		closeBlock("notifyDelegateFailed");
		emptyline();
	
		writeString("private "+getRemoteInterfaceName(type)+" getDelegate() throws NoConnectionToServerException{");
		increaseIdent();
		writeString("if (discoveryMode==DiscoveryMode.MANUAL){");
		increaseIdent();
		writeString("if (manuallySetTarget!=null)");
		writeIncreasedStatement("return manuallySetTarget");
		//now we have to lookup in manual mode, meaning creating a new stub.
		writeStatement("manuallySetTarget = lookup(manuallySetDescriptor)");
		writeStatement("return manuallySetTarget");
		closeBlock("if (mode==MANUAL)");
		writeStatement("return getDelegate("+getConstantsName(type)+".getServiceId())");
		closeBlock();
		emptyline();
	
		writeString("private "+getRemoteInterfaceName(type)+" getDelegate(String serviceId) throws NoConnectionToServerException{");
		increaseIdent();
		writeCommentLine("if no serviceId is provided, fallback to default resolve with manual mode");
		writeString("if (serviceId==null || discoveryMode==DiscoveryMode.MANUAL)");
		writeIncreasedStatement("return getDelegate()");
		writeStatement(getRemoteInterfaceName(type)+" delegate = delegates.get(serviceId)");
		writeString("if (delegate==null){");
		increaseIdent();
		openTry();
		writeStatement("delegate = lookup(serviceId)");
		writeStatement(getRemoteInterfaceName(type) + " existingDelegate = delegates.putIfAbsent(serviceId, delegate)");
		writeString("if (existingDelegate!=null)");
		writeIncreasedStatement("delegate = existingDelegate");
		decreaseIdent();
		writeString("}catch(Exception e){");
		//TODO replace this with a typed exception!
		writeCommentLine("//TODO - generate and throw typed exception.");
		writeIncreasedStatement("throw new NoConnectionToServerException(\"Couldn't lookup delegate because: \"+e.getMessage()+\" at \"+RegistryUtil.describeRegistry(), e)");
		writeString("}//try");
		closeBlock("first if (del==null) ");
		writeStatement("return delegate");
		closeBlock("fun");
		emptyline();
	
		writeString("private "+getRemoteInterfaceName(type)+" lookup(String serviceId) throws NoConnectionToServerException{");
		increaseIdent();
		writeCommentLine("//first we need to lookup target host.");
		writeStatement("ServiceDescriptor toLookup = new ServiceDescriptor(Protocol.RMI, serviceId)");
		writeStatement("ServiceDescriptor targetService = RegistryUtil.resolve(toLookup)");
		writeString("if (targetService==null)");
		writeIncreasedStatement("throw new RuntimeException("+quote("Can't resolve host for an instance of ")+"+"+getConstantsName(type)+".getServiceId())");
		writeStatement("Registry registry = null");
		openTry();
		writeStatement("registry = LocateRegistry.getRegistry(targetService.getHost(), targetService.getPort())");
		decreaseIdent();
		writeString("}catch(Exception e){");
		writeIncreasedStatement(logWriter.createErrorOutputWithException(quote("lookup - couldn't obtain rmi registry on ")+"+targetService+"+quote(", aborting lookup"), "e"));
		writeIncreasedStatement("throw new NoConnectionToServerException("+quote("Can't resolve rmi registry for an instance of ")+"+"+getConstantsName(type)+".getServiceId())");
		writeString("}");
		
		openTry();
		writeStatement("return ("+getRemoteInterfaceName(type)+") registry.lookup(serviceId)");
		decreaseIdent();
		writeString("}catch(RemoteException e){");
		writeIncreasedStatement("throw new NoConnectionToServerException("+quote("Can't lookup service in the target rmi registry for an instance of ")+"+serviceId, e)");
		writeString("}catch(NotBoundException e){");
		writeIncreasedStatement("throw new NoConnectionToServerException("+quote("Can't lookup service in the target rmi registry for an instance of ")+"+serviceId, e)");
		writeString("}");
	
		closeBlock();
		emptyline();
	
		
		writeString("private "+getRemoteInterfaceName(type)+" lookup(ServiceDescriptor serviceDescriptor) throws NoConnectionToServerException{");
		increaseIdent();
		writeStatement("Registry registry = null");
		openTry();
		writeStatement("registry = LocateRegistry.getRegistry(serviceDescriptor.getHost(), serviceDescriptor.getPort())");
		decreaseIdent();
		writeString("}catch(Exception e){");
		writeIncreasedStatement(logWriter.createErrorOutputWithException(quote("lookup - couldn't obtain rmi registry on ")+"+serviceDescriptor+"+quote(", aborting lookup"), "e"));
		writeIncreasedStatement("throw new NoConnectionToServerException("+quote("Can't resolve rmi registry for ")+"+serviceDescriptor)");
		writeString("}");
		
		openTry();
		writeStatement(getRemoteInterfaceName(type)+" ret = ("+getRemoteInterfaceName(type)+") registry.lookup(serviceDescriptor.getServiceId())");

		writeStatement("return ret");
		decreaseIdent();
		writeString("}catch(RemoteException e){");
		writeIncreasedStatement("throw new NoConnectionToServerException("+quote("Can't lookup service in the target rmi registry for an instance of ")+"+serviceDescriptor, e)");
		writeString("}catch(NotBoundException e){");
		writeIncreasedStatement("throw new NoConnectionToServerException("+quote("Can't lookup service in the target rmi registry for an instance of ")+"+serviceDescriptor, e)");
		writeString("}");
	
		closeBlock();

		//write exception mapping block
		emptyline();
		writeString("private "+DistributemeRuntimeException.class.getSimpleName()+" mapException(Exception in){");
		increaseIdent();
		writeString("if (in instanceof "+DistributemeRuntimeException.class.getSimpleName()+")");
		writeIncreasedStatement("return ("+DistributemeRuntimeException.class.getSimpleName()+") in");
		writeString("if (in instanceof "+RemoteException.class.getSimpleName()+")");
		writeIncreasedStatement("return new "+ServiceUnavailableException.class.getSimpleName()+" (\"Service unavailable due to rmi failure: \"+in.getMessage(), in)");
		writeStatement("return new "+ServiceUnavailableException.class.getSimpleName()+"(\"Unexpected exception: \"+in.getMessage()+\" \" + in.getClass().getName(), in)");
		closeBlock();
		
		//write concurrency control strategy created methods
		for (TranslatedCCAnnotation cca: concurrencyControlAnnotations){
			writeConcurrencyControlCreationMethod(cca);
		}
	
		//write concurrency control strategy created methods
		for (TranslatedRouterAnnotation tra: routerAnnotations){
			writeRouterCreationMethod(getConstantsName(type)+".getServiceId()", tra);
		}

		closeBlock();
		
		
		writer.flush();
		writer.close();
		//System.out.println("%%%\finished generating "+type+"");

	}
	
	
	private void writeInterceptionBlock(InterceptionPhase phase, ExecutableElement method){
		boolean afterCall = phase == InterceptionPhase.AFTER_SERVICE_CALL;
		writeStatement("diMeInterceptionContext.setCurrentPhase(InterceptionPhase."+phase.toString()+")");
		if (afterCall){
			writeString("if (__fromServerSide!=null){");
			writeIncreasedStatement("diMeInterceptionContext.setReturnValue(__fromServerSide.get(0))");
			writeString("}");
			writeStatement("diMeInterceptionContext.setException(exceptionInMethod)");
			writeStatement("boolean diMeReturnOverriden = false");
		}
		writeString("for (ClientSideRequestInterceptor interceptor : diMeInterceptors){");
		increaseIdent();
		writeStatement("InterceptorResponse interceptorResponse = interceptor."+interceptionPhaseToMethod(phase)+"(diMeCallContext, diMeInterceptionContext)");
		writeString("switch(interceptorResponse.getCommand()){");
		writeString("case ABORT:");
		increaseIdent();
		writeString("if (interceptorResponse.getException() instanceof RuntimeException)");
		writeIncreasedStatement("throw (RuntimeException) interceptorResponse.getException()");
		for (TypeMirror type : method.getThrownTypes()){
			writeString("if (interceptorResponse.getException() instanceof "+type.toString()+")");
			writeIncreasedStatement("throw ("+type.toString()+") interceptorResponse.getException()");
		}
		writeStatement("throw new RuntimeException("+quote("Interceptor exception")+",interceptorResponse.getException())");
		decreaseIdent();
		writeString("case RETURN:");
		if (isVoidReturn(method)){
			writeIncreasedStatement("return");
		}else{
			writeIncreasedStatement("return "+convertReturnValue(method.getReturnType(), "interceptorResponse.getReturnValue()"));
		}
		if (!isVoidReturn(method) && afterCall){
			writeString("case OVERWRITE_RETURN_AND_CONTINUE:");
			writeIncreasedString("if (__fromServerSide == null)");
			increaseIdent();
			writeIncreasedStatement("throw new AssertionError("+quote("Incorrect use of interceptor, there is no return value in this method or it is null")+")");
			decreaseIdent();
			writeIncreasedStatement("__fromServerSide.set(0, interceptorResponse.getReturnValue())");
			writeIncreasedStatement("diMeInterceptionContext.setReturnValue(interceptorResponse.getReturnValue())");
			writeIncreasedStatement("diMeReturnOverriden = true");
			writeIncreasedStatement("break");
		}
		writeString("case CONTINUE:");
		writeIncreasedStatement("break");
		writeString("case ABORT_AND_FAIL:");
		writeIncreasedStatement("abortAndFail = true");
		if (!afterCall) {
			writeIncreasedStatement("exceptionInMethod = new FailedByInterceptorException()");
		}
		writeIncreasedStatement("break");
		writeString("case RETURN_AND_FAIL:");
		writeCommentLine("Force failing logic to work.");
		if (!afterCall) {
			writeIncreasedStatement("diMeForceFailing = true");
			writeIncreasedStatement("exceptionInMethod = new FailedByInterceptorException()");
		}
		//after call we can't jump into the failover processing because we are in the finally block.
		if (afterCall){
			writeStatement(getFailingStrategyVariableName(method)+".callFailed(diMeCallContext)");
		}
		//writeIncreasedStatement("System.out.println(\"I will need to fail from now on\")");
		writeIncreasedStatement("break");
		writeString("default:");
		writeIncreasedStatement("throw new IllegalStateException("+quote("Unsupported or unexpected command from interceptor ")+" + interceptorResponse.getCommand()+ \" in phase:\"+diMeInterceptionContext.getCurrentPhase())");
		increaseIdent();
		closeBlock("switch");
			
		closeBlock("for");
		if (!isVoidReturn(method) && afterCall){
			writeCommentLine("The next check for null of __fromServerSide is unneeded but for security reasons.");
			writeString("if (diMeReturnOverriden && __fromServerSide!=null)");
			writeIncreasedStatement("return "+convertReturnValue(method.getReturnType(), "__fromServerSide.get(0)"));
		}

	}
	
	
	
}
