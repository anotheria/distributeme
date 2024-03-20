package org.distributeme.generator;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.FactoryNotFoundException;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.util.IdCodeGenerator;
import net.anotheria.util.PidTools;
import org.distributeme.annotation.CombinedService;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.DummyFactory;
import org.distributeme.annotation.Route;
import org.distributeme.annotation.RouteMe;
import org.distributeme.annotation.ServerListener;
import org.distributeme.annotation.SupportService;
import org.distributeme.core.RMIRegistryUtil;
import org.distributeme.core.RegistryUtil;
import org.distributeme.core.ServerShutdownHook;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.SystemPropertyNames;
import org.distributeme.core.Verbosity;
import org.distributeme.core.conventions.SystemProperties;
import org.distributeme.core.lifecycle.LifecycleComponentImpl;
import org.distributeme.core.listener.ListenerRegistry;
import org.distributeme.core.listener.ServerLifecycleListener;
import org.distributeme.core.listener.ServerLifecycleListenerShutdownHook;
import org.distributeme.core.routing.RegistrationNameProvider;
import org.distributeme.core.routing.RoutingAware;
import org.distributeme.core.util.LocalServiceDescriptorStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Generator for RMI based server programm.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class ServerGenerator extends AbstractGenerator implements Generator{
	
	private static final String[] SUPPORT_SERVICES_ONLY = {
		"org.distributeme.support.lifecycle.generated.LifecycleSupportServer",
		"org.distributeme.support.eventservice.generated.EventServiceRMIBridgeServer",
	};

	private static final String[] SUPPORT_SERVICES_WITH_AGENTS = {
		"org.distributeme.support.lifecycle.generated.LifecycleSupportServer",
		"org.distributeme.support.eventservice.generated.EventServiceRMIBridgeServer",
		"org.distributeme.agents.transporter.generated.TransporterServer",
	};

	/**
	 * <p>Constructor for ServerGenerator.</p>
	 *
	 * @param environment a {@link javax.annotation.processing.ProcessingEnvironment} object.
	 */
	public ServerGenerator(ProcessingEnvironment environment) {
		super(environment);
	}

	/** {@inheritDoc} */
	@Override
	public void generate(TypeElement type, Filer filer, Map<String,String> options) throws IOException{
		JavaFileObject sourceFile = filer.createSourceFile(getPackageName(type)+"."+getServerName(type));
        PrintWriter writer = new PrintWriter(sourceFile.openWriter());
		setWriter(writer);
		
		//meta servers rely on other skeletons and just starts them.
		boolean combinedServer = type.getAnnotation(CombinedService.class)!=null;
		
		writePackage(type);
		writeAnalyzerComments(type);
		emptyline();
		writeImport(Logger.class);
		writeImport(LoggerFactory.class);
		writeImport(Marker.class);
		writeImport(MarkerFactory.class);
		writeImport(UnicastRemoteObject.class);
		writeImport(Permission.class);
		writeImport(Registry.class);
		writeImport(RegistryUtil.class);
		writeImport(ServiceDescriptor.class);
		writeImport(LocalServiceDescriptorStore.class);
		writeImport("org.distributeme.core.ServiceDescriptor.Protocol");
		writeImport(MetaFactory.class);
		writeImport(FactoryNotFoundException.class);
		writeImport(Extension.class);
		writeImport(PidTools.class);
		writeImport(IdCodeGenerator.class);
		writeImport(RMIRegistryUtil.class);
		writeImport(RemoteException.class);
		writeImport(ServiceFactory.class);
		writeImport(LifecycleComponentImpl.class);
		writeImport(Verbosity.class);
		writeImport(SystemPropertyNames.class);
		writeImport(ServerShutdownHook.class);
		writeImport(SystemProperties.class);
		writeImport(List.class);
		writeImport(RoutingAware.class);
		emptyline();
		
		DistributeMe annotation = type.getAnnotation(DistributeMe.class);
		boolean supportService = type.getAnnotation(SupportService.class)!=null;
		String [] SUPPORT_SERVICES = annotation.agentsSupport() ? SUPPORT_SERVICES_WITH_AGENTS : SUPPORT_SERVICES_ONLY;
		/*
		*** Removed imports for PMD.
		if (!supportService){
			for (String sService : SUPPORT_SERVICES){
				writeImport(sService);
			}
		}*/

		writeAnalyzeIgnoreAnnotation(type);
		writeString("public class "+getServerName(type)+"{");
		increaseIdent();
		emptyline();
		writeStatement("private static Logger log");
		writeStatement("private static Marker FATAL = MarkerFactory.getMarker(\"FATAL\")");
		writeStatement("private static long serverStartTime = System.currentTimeMillis()"); //nanoTime is more precise but for our purposes millis are enough.
		emptyline();

		
		//checking for support service.
		if (!supportService){
			//only support service gets a main method
			writeString("public static void main(String a[]) throws Exception{");
			increaseIdent();

			writeCommentLine("the security manager decision and setting is now done by server side utils");
			writeStatement("org.distributeme.core.util.ServerSideUtils.setSecurityManagerIfRequired()");
			emptyline();

			writeString("try {");
			increaseIdent();
			writeStatement("init()");
			writeCommentLine("Log current server PID (Process Id)");
			writeStatement("PidTools.logPid()");
			writeCommentLine("force verbosity to configure itself");
			writeStatement("Verbosity.logServerSideExceptions()");
			writeStatement("createSupportServicesAndRegisterLocally()");
			if(!combinedServer){
				writeStatement("createServiceAndRegisterLocally()");
			}
			if(combinedServer){
				writeStatement("createCombinedServicesAndRegisterLocally()");
			}
			writeStatement("startService()");
			writeStatement("notifyListenersAboutStart()");
			//writeStatement("this(new "+type.getAnnotation(DistributeMe.class).implName()+"())");
			decreaseIdent();
			writeString("} catch (Throwable e) {");
			
			increaseIdent();
			writeStatement("log.error(FATAL, \"Unhandled exception caught\", e)");
			writeStatement("System.err.println(e.getMessage())");
			writeStatement("System.exit(-4)");
			decreaseIdent();
			writeString("}");
			closeBlock("main");
			emptyline();
		}
		
		if (supportService){
			writeCommentLine("Support service have no main method");
		}
		
		List<AnnotationMirror> mirrors = findMirrors(type, ServerListener.class);
		writeStatement("private static final List<"+ServerLifecycleListener.class.getName()+"> serverListeners = new "+ArrayList.class.getName()+"<"+ServerLifecycleListener.class.getName()+">("+(mirrors==null ? 0:mirrors.size())+")");
		writeString("private static void notifyListenersAboutStart(){");
		increaseIdent();
		if (mirrors!=null && mirrors.size()>0){
			writeCommentLine("compiled listeners");
			for (AnnotationMirror mirror : mirrors){
				writeString("try{");
				increaseIdent();
				writeStatement(ServerLifecycleListener.class.getName()+" listener = new "+findMethodValue(mirror, "listenerClass").getValue()+"()");
				writeStatement("listener.afterStart()");
				writeCommentLine("Only add successful listeners");
				writeStatement("serverListeners.add(listener)");
				decreaseIdent();
				writeString("}catch(Exception e){");
				writeIncreasedStatement("log.error("+quote("Couldn't initialize listener "+findMethodValue(mirror, "listenerClass").getValue())+", e)");
				writeString("}");
			}
			//writeStatement("private static final "+ServerListener.class.getName()+" serverListeners = new "+ArrayList.class.getName()+"<"+ServerListener.class.getName()+">("+mirrors.size()+")");
			emptyline();
		}
		
		writeCommentLine("configured listeners");
		writeStatement("List<"+ServerLifecycleListener.class.getName()+"> configuredListeners = "+ListenerRegistry.class.getName()+".getInstance().getServerLifecycleListeners()");
		writeString("if (configuredListeners!=null && configuredListeners.size()>0){");
		increaseIdent();
		writeString("for ("+ServerLifecycleListener.class.getName()+" listener : configuredListeners){");
		increaseIdent();
		writeString("try{");
		increaseIdent();
		writeStatement("listener.afterStart()");
		decreaseIdent();
		writeString("}catch(Exception e){");
		writeIncreasedStatement("log.error("+quote("Couldn't call afterStart on  listener ")+" + listener, e)");
		writeString("}");
		closeBlock("for");
		closeBlock("if");
		
		
		
		
		closeBlock("notifyListenersAboutStart");
		emptyline();
		
		
		writeString("public static void init() throws Exception{");
		increaseIdent();
		writeStatement("log = LoggerFactory.getLogger("+getServerName(type)+".class)");
		writeCommentLine("// CUSTOM CODE STARTED");
		String[] initCode = annotation.initcode();
		for (String s : initCode){
			writeString(s);
		}
		writeCommentLine("// CUSTOM CODE ENDED");
		
		closeBlock("init");
		emptyline();
		
		if (!combinedServer){
			//START METHOD createServiceAndRegisterLocally
			writeCommentLine("Have to keep local reference to the rmiServant and skeleton to prevent gc removal");
			writeStatement("private static "+getRemoteInterfaceName(type)+" skeleton = null"); 
			writeStatement("private static "+getRemoteInterfaceName(type)+" rmiServant = null");
			writeStatement("private static String serviceId = null");
			emptyline();

			writeString("public static void createServiceAndRegisterLocally() throws Exception{");
			increaseIdent();
			writeCommentLine("Use default port, which is -1");
			writeStatement("createServiceAndRegisterLocally(-1)");
			closeBlock();
			emptyline();

			writeString("public static void createServiceAndRegisterLocally(int customRegistryPort) throws Exception{");
			increaseIdent();
			writeCommentLine("creating impl");
			//old , direct instantiation writeStatement(type.getQualifiedName()+" impl = new "+type.getAnnotation(DistributeMe.class).factoryClassName()+"().create()");
			AnnotationMirror annotationMirror = findMirror(type, DistributeMe.class);
			if (annotationMirror==null)
				throw new AssertionError("AnnotationMirror is null, which actually can't happen, since the annotation was previously found: "+annotation);
			AnnotationValue factoryClazzValue = findMethodValue(annotationMirror, "factoryClazz");
				
			String factoryClassName = getDefaultImplFactoryName(type);
			String implClassName = type.getQualifiedName()+"Impl";
			
			if (factoryClazzValue!=null && !(factoryClazzValue.getValue().equals(DummyFactory.class.getName()+".class"))){
				writeCommentLine("Registering factory");
				writeStatement("MetaFactory.addFactoryClass("+type+".class, Extension."+annotation.extension()+", "+factoryClazzValue.getValue()+".class)");
			}else{
				writeCommentLine("No factory specified");
				if (initCode.length>0){
					writeCommentLine("init code not empty, assuming it contains factory Element");
				}else{
					//try autoresolve
					writeString("try{");
					increaseIdent();
					writeStatement("Class<ServiceFactory<"+type.getQualifiedName()+">> factoryClazz = (Class<ServiceFactory<"+type.getQualifiedName()+">>)Class.forName("+quote(factoryClassName)+")");
					writeStatement("MetaFactory.addFactoryClass("+type+".class, Extension."+annotation.extension()+", factoryClazz)");
					decreaseIdent();
					writeString("}catch(ClassNotFoundException factoryNotFound){");
					increaseIdent();
					writeString("try{");
					increaseIdent();
					writeCommentLine("Even more convenient - try to instantiate the implementation directly");
					writeStatement("Class<? extends "+type.getQualifiedName()+"> implClazz = (Class<? extends "+type.getQualifiedName()+">)Class.forName("+quote(implClassName)+")");
					writeStatement("MetaFactory.createOnTheFlyFactory("+type+".class, Extension."+annotation.extension()+", implClazz.newInstance())");
					decreaseIdent();
					writeString("}catch(ClassNotFoundException implNotFound){");
					increaseIdent();
					writeStatement("log.info("+quote("Giving up trying to find an impl instance, tried "+factoryClassName+" and "+implClassName+", expect start to fail since init code were empty too and no factory has been supplied explicitely")+")");
					closeBlock("inner catch");
					closeBlock("outer catch");
				}
			}
			writeStatement(type.getQualifiedName()+" impl = null");
			writeString("try{");
			increaseIdent();
			writeStatement("impl = MetaFactory.get(" + type.getQualifiedName() + ".class, Extension." + annotation.extension() + ")");
			decreaseIdent();
			writeString("}catch (FactoryNotFoundException factoryNotFound){");
			writeIncreasedStatement("throw new AssertionError(" + quote("Un- or mis-configured, can't instantiate service instance for " + type.getQualifiedName() + " tried initcode, submitted factory, autoguessed factory (" + factoryClassName + ") and impl class (" + implClassName + ")") + ")");
			writeString("}");
			
			
			writeStatement("skeleton = new " + getSkeletonName(type) + "(impl)");
			writeStatement("rmiServant = (" + getRemoteInterfaceName(type) + ") UnicastRemoteObject.exportObject(skeleton, SystemProperties.SERVICE_BINDING_PORT.getAsInt())");
			writeStatement("serviceId = "+getConstantsName(type)+".getServiceId()");
			writeCommentLine("Save original serviceId for later RoutingAware call");
			writeStatement("String definedServiceId = serviceId");
			emptyline();
			
			//determine whether we have a custom registration name.
			if (type.getAnnotation(RouteMe.class)!=null){
				emptyline();
				writeCommentLine("Customizing registration name");
				AnnotationMirror routeMe = findMirror(type, RouteMe.class);
				AnnotationValue registrationNameProviderClazzValue = findMethodValue(routeMe, "providerClass");
				AnnotationValue registrationNameProviderParameterValue = findMethodValue(routeMe, "providerParameter");
				writeStatement(RegistrationNameProvider.class.getName()+" nameProvider = new "+registrationNameProviderClazzValue.getValue()+"()");
				writeStatement("nameProvider.customize("+quote(registrationNameProviderParameterValue.getValue())+")");
				writeStatement("serviceId = nameProvider.getRegistrationName(serviceId)");
				emptyline();
			}
			
			//determine whether we have a registration name provider
			writeStatement("String regNameProviderClass = System.getProperty(SystemPropertyNames.REGISTRATION_NAME_PROVIDER)");
			writeString("if (regNameProviderClass!=null){");
			increaseIdent();
			writeStatement(RegistrationNameProvider.class.getName()+" suppliedNameProvider = ("+RegistrationNameProvider.class.getName()+")Class.forName(regNameProviderClass).newInstance()");
			writeStatement("serviceId = suppliedNameProvider.getRegistrationName(serviceId)");
			closeBlock("if (regNameProviderClass!=null)");
			
			emptyline();
			writeStatement("log.info("+quote("Getting local registry")+")");
			writeStatement("Registry registry = null");
			openTry();
			writeStatement("registry = RMIRegistryUtil.findOrCreateRegistry(customRegistryPort)");
			decreaseIdent();
			writeString("}catch(RemoteException e){");
			increaseIdent();
			writeStatement("log.error(FATAL, "+quote("Couldn't obtain free port for a local rmi registry")+", e)");
			writeStatement("System.err.println("+quote("Couldn't obtain a free port for local rmi registry")+")");
			writeStatement("System.exit(-1)"); 
			closeBlock();
					
			emptyline();
			writeStatement("log.info(" + quote("Registering ") + "+serviceId+" + quote(" locally.") + ")");
		
			emptyline();
			openTry();
			writeStatement("registry.rebind(serviceId, rmiServant)");
			decreaseIdent();
			writeStatement("}catch(Exception e){");
			increaseIdent();
			writeStatement("log.error(FATAL, "+quote("Couldn't rebind myself at the local registry")+", e)");
			writeStatement("System.err.println("+quote("Couldn't rebind myself at the local registry")+")");
			writeStatement("e.printStackTrace()");
			writeStatement("System.exit(-2)"); 
			closeBlock("local registry bind.");
			emptyline();

			//we don't need this anymore apparently.
			//AnnotationMirror annotationMirrorRoute = findMirror(type, Route.class);

			//ROUTING AWARE-NESS.
			if (annotationMirror!=null){
				AnnotationValue routerParameter = findMethodValue(annotationMirror, "routerParameter");
				AnnotationValue configurationName = findMethodValue(annotationMirror, "configurationName");


				//after registration, if service is RoutingAware we should notify it about its name.
				//of course it only makes sense if the service had Route annotation.
				writeString("if (impl instanceof RoutingAware){");
				increaseIdent();
				writeStatement("((RoutingAware)impl).notifyServiceId(definedServiceId, serviceId, "
						+ (routerParameter == null ? "null" : quote(routerParameter.getValue()))
						+ ", "
						+ (configurationName == null ? "null" : quote(configurationName.getValue()))
						+ ") ");
				closeBlock("/if impl RoutingAware");
			}


			//finally locally register service
			if (!supportService){
				writeStatement("LifecycleComponentImpl.INSTANCE.registerPublicService(serviceId, skeleton)");
			}
			closeBlock();
			emptyline();
		}
		
		
		//combined server have no own descriptors.
		if (!combinedServer){
			writeString("public static ServiceDescriptor createDescriptor(String instanceId) throws Exception{");
			String descriptorCall = "RegistryUtil.createLocalServiceDescription("+
			"Protocol.RMI, "+
			" serviceId,"+
			" instanceId,"+
			" RMIRegistryUtil.getRmiRegistryPort())";
			increaseIdent();
			writeStatement("return "+descriptorCall);
			closeBlock();
		}

		//METHOD startService
		writeString("public static void startService() throws Exception{");
		increaseIdent();
//		writeStatement("String serviceId = "+getConstantsName(type)+".getServiceId()");
		writeStatement("String instanceId = IdCodeGenerator.generateCode(10)");

		
		if(!combinedServer){
		
			writeStatement("boolean registerCentrally = !SystemProperties.SKIP_CENTRAL_REGISTRY.getAsBoolean()");
			writeString("if (registerCentrally){");
			increaseIdent();
			writeStatement("ServiceDescriptor descriptor = createDescriptor(instanceId)");
			writeStatement("LocalServiceDescriptorStore.getInstance().addServiceDescriptor(descriptor)");
			
			emptyline();
			writeString("if (!RegistryUtil.bind(descriptor)){");
			increaseIdent();
			writeStatement("log.error(FATAL, "+quote("Couldn't bind myself to the central registry at ")+"+RegistryUtil.describeRegistry())");
			writeStatement("System.err.println("+quote("Couldn't bind myself at the central registry at ")+"+RegistryUtil.describeRegistry())");
			writeStatement("System.exit(-3)"); 
			closeBlock("central registry bind");
			writeStatement("Runtime.getRuntime().addShutdownHook(new ServerShutdownHook(descriptor))");
			emptyline();
			
			decreaseIdent();
			writeString("}else{");
			writeIncreasedStatement("System.out.println("+quote("skipping registration for ")+"+serviceId"+")");
			writeString("}");

			writeStatement("System.out.println("+quote("Server ")+"+serviceId+"+quote(" is up and ready (in \"+(System.currentTimeMillis()-serverStartTime)+\" ms).")+")");
		}

		if(combinedServer){
			List<String> serviceNames = getCombinedServicesNames(type);
			for (String service : serviceNames){
				String descriptorVariableName = getServerName(service).toLowerCase()+"Descriptor";
				String serviceServerClassName = getFullyQualifiedServerName(service);
				writeStatement("ServiceDescriptor "+descriptorVariableName+" = "+serviceServerClassName+".createDescriptor(instanceId)");
				writeStatement("LocalServiceDescriptorStore.getInstance().addServiceDescriptor("+descriptorVariableName+")");

				emptyline();
				writeString("if (!RegistryUtil.bind("+descriptorVariableName+")){");
				increaseIdent();
				writeStatement("log.error(FATAL, "+quote("Couldn't bind ")+"+"+descriptorVariableName+"+"+quote(" to the central registry at ")+"+RegistryUtil.describeRegistry())");
				writeStatement("System.err.println("+quote("Couldn't bind ")+"+"+descriptorVariableName+"+"+quote(" at the central registry at ")+"+RegistryUtil.describeRegistry())");
				writeStatement("System.exit(-3)"); 
				closeBlock("central registry bind");
				writeStatement("Runtime.getRuntime().addShutdownHook(new ServerShutdownHook("+descriptorVariableName+"))");
				emptyline();
				writeStatement("System.out.println("+quote("Server ")+"+"+descriptorVariableName+".getServiceId()+"+quote(" is up and ready (in \"+(System.currentTimeMillis()-serverStartTime)+\" ms).")+")");
			}
		}

		//note, this shutdown hook will be activated AFTER de-registration hook.
		writeStatement("Runtime.getRuntime().addShutdownHook(new "+ServerLifecycleListenerShutdownHook.class.getName()+"(serverListeners))");
		closeBlock("startService");
		
		
		if (!supportService){
			emptyline();
			//METHOD createSupportServicesAndRegisterLocally
			writeString("public static void createSupportServicesAndRegisterLocally() throws Exception{");
			increaseIdent();
			
			for (String s : SUPPORT_SERVICES){
				writeStatement(s+".init()");
				writeStatement(s+".createServiceAndRegisterLocally()");
			}
			closeBlock("createSupportServicesAndRegisterLocally");
			emptyline();
		}
		
		if (combinedServer){
			emptyline();
			writeString("public static void createCombinedServicesAndRegisterLocally() throws Exception{");
			increaseIdent();
			writeStatement("createCombinedServicesAndRegisterLocally(-1)");
			closeBlock("createCombinedServicesAndRegisterLocally");
			emptyline();

			writeString("public static void createCombinedServicesAndRegisterLocally(int customRegistryPort) throws Exception{");
			increaseIdent();
			List<String> targetServicesNames = getCombinedServicesNames(type);
			for (String service : targetServicesNames){
				writeStatement(getFullyQualifiedServerName(service)+".init()");
				writeStatement(getFullyQualifiedServerName(service)+".createServiceAndRegisterLocally(customRegistryPort)");
			}
//			for (String s : SUPPORT_SERVICES){
//				writeStatement(s+".init()");
//				writeStatement(s+".createServiceAndRegisterLocally()");
//			}
			closeBlock("createCombinedServicesAndRegisterLocally");
			emptyline();
		}
			
		closeBlock();
		writeAnalyzerCommentsEnd(type);
		
		writer.flush();
		writer.close();
	}
	
	private List<String> getCombinedServicesNames(TypeElement type){
		AnnotationMirror ann = findMirror(type, CombinedService.class);
		AnnotationValue val = findMethodValue(ann, "services");
		List<? extends AnnotationValue> values = (List<? extends AnnotationValue>)val.getValue();
		ArrayList<String> ret = new ArrayList<String>();
		for (AnnotationValue o : values){
			ret.add(o.getValue().toString());
		}
		return ret;
	}
}
