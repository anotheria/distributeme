package org.distributeme.generator.jsonrpc;

import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.tools.apt.mirror.declaration.AnnotationValueImpl;
import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.FactoryNotFoundException;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.util.IdCodeGenerator;
import net.anotheria.util.PidTools;
import org.distributeme.annotation.CombinedService;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.DummyFactory;
import org.distributeme.annotation.RouteMe;
import org.distributeme.annotation.SupportService;
import org.distributeme.core.RMIRegistryUtil;
import org.distributeme.core.RegistryLocation;
import org.distributeme.core.RegistryUtil;
import org.distributeme.core.ServerShutdownHook;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.SystemPropertyNames;
import org.distributeme.core.Verbosity;
import org.distributeme.core.lifecycle.LifecycleComponentImpl;
import org.distributeme.core.routing.RegistrationNameProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 * Generator for RMI based server programm.
 *
 * @author lrosenberg
 */
public class HttpEndpointServerGenerator extends AbstractJsonRpcGenerator implements JsonRpcGenerator {
    private final Filer filer;

    public HttpEndpointServerGenerator(final Filer filer) {
        this.filer = filer;
    }

    private static final String[] SUPPORT_SERVICES = {
            "org.distributeme.support.lifecycle.generated.LifecycleSupportServer",
            "org.distributeme.support.eventservice.generated.EventServiceRMIBridgeServer",
    };

    @Override
    public void generate(TypeDeclaration type) throws IOException {
        PrintWriter writer = filer.createSourceFile(getPackageName(type) + "." + getServerName(type));
        setWriter(writer);

        //meta servers rely on other client stubs and just starts them.
        boolean combinedServer = type.getAnnotation(CombinedService.class) != null;

        writePackage(type);
        emptyline();
        writeImport(Logger.class);
		writeImport(LoggerFactory.class);
		writeImport(Marker.class);
		writeImport(MarkerFactory.class);
		writeImport(RegistryUtil.class);
        writeImport(RegistryLocation.class);
        writeImport(ServiceDescriptor.class);
        writeImport("org.distributeme.core.ServiceDescriptor.Protocol");
        writeImport(MetaFactory.class);
        writeImport(FactoryNotFoundException.class);
        writeImport(Extension.class);
        writeImport(PidTools.class);
        writeImport(IdCodeGenerator.class);
        writeImport(RMIRegistryUtil.class);
        writeImport(ServiceFactory.class);
        writeImport(LifecycleComponentImpl.class);
        writeImport(Verbosity.class);
        writeImport(SystemPropertyNames.class);
        writeImport(ServerShutdownHook.class);
        writeImport(Permission.class);
        writeImport("cz.eman.jsonrpc.server.http.JsonRpcServlet");
        writeImport("org.mortbay.jetty.security.SslSocketConnector");
        writeImport("java.lang.System");
        writeImport("org.mortbay.jetty.Server");
        writeImport("org.mortbay.jetty.servlet.Context");
        writeImport("org.mortbay.jetty.servlet.ServletHolder");
        writeImport("org.mortbay.jetty.nio.SelectChannelConnector");
        emptyline();
        for (String supportService : SUPPORT_SERVICES) {
            writeImport(supportService);
        }

        writeString("public class " + getServerName(type) + "{");
        increaseIdent();
        emptyline();
		writeStatement("private static final Marker FATAL = MarkerFactory.getMarker(\"FATAL\")");
        writeStatement("private static Logger log");
        emptyline();

        boolean supportService = type.getAnnotation(SupportService.class) != null;

        //checking for support service.
        if (!supportService) {
            //only support service gets a main method
            writeString("public static void main(String a[]) throws Exception{");
            increaseIdent();

			writeString("if (System.getSecurityManager()==null)");
            increaseIdent();
            writeString("System.setSecurityManager(new SecurityManager(){");
            writeIncreasedString("public void checkPermission(Permission perm) { }");
            writeStatement("})");
            decreaseIdent();


            writeStatement("init()");
            writeCommentLine("force verbosity to configure itself");
            writeStatement("Verbosity.logServerSideExceptions()");
            writeStatement("createSupportServicesAndRegisterLocally()");
            if (!combinedServer) {
                writeStatement("createServiceAndRegisterLocally()");
            }
            if (combinedServer) {
                writeStatement("createCombinedServicesAndRegisterLocally()");
            }
            writeStatement("startService()");
            //writeStatement("this(new "+type.getAnnotation(DistributeMe.class).implName()+"())");
            closeBlock("main");
            emptyline();
        }

        if (supportService) {
            writeCommentLine("Support service have no main method");
        }
        DistributeMe annotation = type.getAnnotation(DistributeMe.class);

        writeString("public static void init() throws Exception{");
        increaseIdent();
        writeStatement("log = LoggerFactory.getLogger(" + getServerName(type) + ".class)");
        writeCommentLine("// CUSTOM CODE STARTED");
        String[] initCode = annotation.initcode();
        for (String s : initCode) {
            writeString(s);
        }
        writeCommentLine("// CUSTOM CODE ENDED");

        closeBlock("init");
        emptyline();

        if (!combinedServer) {
            //START METHOD createServiceAndRegisterLocally
            writeCommentLine("Have to keep local reference to the clientStub to prevent gc removal");
            writeStatement("private static " + getServerInterfaceName(type) + " clientStub = null");
            writeStatement("private static String serviceId = null");
            emptyline();
            writeString("public static void createServiceAndRegisterLocally() throws Exception{");
            increaseIdent();
            writeCommentLine("creating impl");
            //old , direct instantiation writeStatement(type.getQualifiedName()+" impl = new "+type.getAnnotation(DistributeMe.class).factoryClassName()+"().create()");
            AnnotationMirror annotationMirror = findMirror(type, DistributeMe.class);
            if (annotationMirror == null) {
                throw new AssertionError("AnnotationMirror is null, which actually can't happen, since the annotation was previously found: " + annotation);
            }
            AnnotationValue factoryClazzValue = findMethodValue(annotationMirror, "factoryClazz");

            String factoryClassName = getDefaultImplFactoryName(type);
            String implClassName = type.getQualifiedName() + "Impl";

            if (factoryClazzValue != null && !(factoryClazzValue.getValue().equals(DummyFactory.class.getName() + ".class"))) {
                writeCommentLine("Registering factory");
                writeStatement("MetaFactory.addFactoryClass(" + type + ".class, Extension." + annotation.extension() + ", " + factoryClazzValue.getValue() + ".class)");
            } else {
                writeCommentLine("No factory specified");
                if (initCode.length > 0) {
                    writeCommentLine("init code not empty, assuming it contains factory declaration");
                } else {
                    //try autoresolve
                    writeString("try{");
                    increaseIdent();
                    writeStatement("Class<ServiceFactory<" + type.getQualifiedName() + ">> factoryClazz = (Class<ServiceFactory<" + type.getQualifiedName() + ">>)Class.forName(" + quote(factoryClassName) + ")");
                    writeStatement("MetaFactory.addFactoryClass(" + type + ".class, Extension." + annotation.extension() + ", factoryClazz)");
                    decreaseIdent();
                    writeString("}catch(ClassNotFoundException factoryNotFound){");
                    increaseIdent();
                    writeString("try{");
                    increaseIdent();
                    writeCommentLine("Even more convinient - try to instantiate the implementation directly");
                    writeStatement("Class<? extends " + type.getQualifiedName() + "> implClazz = (Class<? extends " + type.getQualifiedName() + ">)Class.forName(" + quote(implClassName) + ")");
                    writeStatement("MetaFactory.createOnTheFlyFactory(" + type + ".class, Extension." + annotation.extension() + ", implClazz.newInstance())");
                    decreaseIdent();
                    writeString("}catch(ClassNotFoundException implNotFound){");
                    increaseIdent();
                    writeStatement("log.info(" + quote("Giving up trying to find an impl instance, tried " + factoryClassName + " and " + implClassName + ", expect start to fail since init code were empty too and no factory has been supplied explicitely") + ")");
                    closeBlock("inner catch");
                    closeBlock("outer catch");
                }
            }
            writeStatement(type.getQualifiedName() + " impl = null");
            writeString("try{");
            increaseIdent();
            writeStatement("impl = MetaFactory.get(" + type.getQualifiedName() + ".class, Extension." + annotation.extension() + ")");
            decreaseIdent();
            writeString("}catch (FactoryNotFoundException factoryNotFound){");
            writeIncreasedStatement("throw new AssertionError(" + quote("Un- or mis-configured, can't instantiate service instance for " + type.getQualifiedName() + " tried initcode, submitted factory, autoguessed factory (" + factoryClassName + ") and impl class (" + implClassName + ")") + ")");
            writeString("}");


            writeStatement("clientStub = new " + getServerImplName(type) + "(impl)");
            writeStatement("serviceId = " + getConstantsName(type) + ".getServiceId()");
            emptyline();

            //determine whether we have a custom registration name.
            if (type.getAnnotation(RouteMe.class) != null) {
                emptyline();
                writeCommentLine("Customizing registration name");
                AnnotationMirror routeMe = findMirror(type, RouteMe.class);
                AnnotationValue registrationNameProviderClazzValue = findMethodValue(routeMe, "providerClass");
                AnnotationValue registrationNameProviderParameterValue = findMethodValue(routeMe, "providerParameter");
                writeStatement(RegistrationNameProvider.class.getName() + " nameProvider = new " + registrationNameProviderClazzValue.getValue() + "()");
                writeStatement("nameProvider.customize(" + quote(registrationNameProviderParameterValue.getValue()) + ")");
                writeStatement("serviceId = nameProvider.getRegistrationName(serviceId)");
                emptyline();
            }

            //determine whether we have a registration name provider
            writeStatement("String regNameProviderClass = System.getProperty(SystemPropertyNames.REGISTRATION_NAME_PROVIDER)");
            writeString("if (regNameProviderClass!=null){");
            increaseIdent();
            writeStatement(RegistrationNameProvider.class.getName() + " suppliedNameProvider = (" + RegistrationNameProvider.class.getName() + ")Class.forName(regNameProviderClass).newInstance()");
            writeStatement("serviceId = suppliedNameProvider.getRegistrationName(serviceId)");
            closeBlock("if (regNameProviderClass!=null)");

            emptyline();
            //finally locally register service
            if (!supportService) {
                writeStatement("LifecycleComponentImpl.INSTANCE.registerPublicService(serviceId, clientStub)");
            }
            closeBlock();
            emptyline();
        }


        //combined server have no own descriptors.
        if (!combinedServer) {
            writeString("public static ServiceDescriptor createDescriptor(String instanceId) throws Exception{");
            String descriptorCall = "RegistryUtil.createLocalServiceDescription(" +
                    "Protocol.JSONRPC, " +
                    " serviceId," +
                    " instanceId," +
                    " RMIRegistryUtil.getRmiRegistryPort() +100)";
            increaseIdent();
            writeStatement("return " + descriptorCall);
            closeBlock();
        }


        //METHOD startService
        writeString("public static void startService() throws Exception{");
        increaseIdent();
        writeCommentLine("Log current server PID (Process Id)");
        writeStatement("PidTools.logPid()");
//		writeStatement("String serviceId = "+getConstantsName(type)+".getServiceId()");
        writeStatement("String instanceId = IdCodeGenerator.generateCode(10)");


        if (!combinedServer) {

            writeStatement("ServiceDescriptor descriptor = createDescriptor(instanceId)");
            writeStatement("startHttpEndpoint(descriptor)");

            emptyline();
            writeString("if (!RegistryUtil.bind(descriptor)){");
            increaseIdent();
            writeStatement("log.error(FATAL, " + quote("Coulnd't bind myself to the central registry at ") + "+RegistryUtil.describeRegistry())");
            writeStatement("System.err.println(" + quote("Coulnd't bind myself at the central registry at ") + "+RegistryUtil.describeRegistry())");
            writeStatement("System.exit(-3)");
            closeBlock("central registry bind");
            writeStatement("Runtime.getRuntime().addShutdownHook(new ServerShutdownHook(descriptor))");
            emptyline();
            writeStatement("System.out.println(" + quote("Server ") + "+serviceId+" + quote(" is up and ready.") + ")");
        }

        if (combinedServer) {
            List<String> serviceNames = getCombinedServicesNames(type);
            for (String service : serviceNames) {
                String descriptorVariableName = getServerName(service).toLowerCase() + "Descriptor";
                String serviceServerClassName = getServerName(service);
                writeStatement("ServiceDescriptor " + descriptorVariableName + " = " + serviceServerClassName + ".createDescriptor(instanceId)");

                emptyline();
                writeString("if (!RegistryUtil.bind(" + descriptorVariableName + ")){");
                increaseIdent();
                writeStatement("log.error(FATAL " + quote("Coulnd't bind ") + "+" + descriptorVariableName + "+" + quote(" to the central registry at ") + "+RegistryUtil.describeRegistry())");
                writeStatement("System.err.println(" + quote("Coulnd't bind ") + "+" + descriptorVariableName + "+" + quote(" at the central registry at ") + "+RegistryUtil.describeRegistry())");
                writeStatement("System.exit(-3)");
                closeBlock("central registry bind");
                writeStatement("Runtime.getRuntime().addShutdownHook(new ServerShutdownHook(" + descriptorVariableName + "))");
                emptyline();
                writeStatement("System.out.println(" + quote("Server ") + "+" + descriptorVariableName + ".getServiceId()+" + quote(" is up and ready.") + ")");
            }
        }

        closeBlock("startService");

        writeString("private static void startHttpEndpoint(final ServiceDescriptor descriptor) throws Exception {");
        increaseIdent();
        writeStatement("final Server server = new Server()");
        writeString("if(System.getProperty(\"distributeme.json.ssl\") != null) {");
        increaseIdent();
        writeStatement("final SslSocketConnector connector = new SslSocketConnector()");
        writeString("if(System.getProperty(\"distributeme.json.ssl.password\") != null) {");
        increaseIdent();
        writeStatement("connector.setPassword(System.getProperty(\"distributeme.json.ssl.password\"))");
        decreaseIdent();
        writeString("}");
        writeString("if(System.getProperty(\"distributeme.json.ssl.keystore\") != null) {");
        increaseIdent();
        writeStatement("connector.setKeystore(System.getProperty(\"distributeme.json.ssl.keystore\"))");
        decreaseIdent();
        writeString("}");
        writeString("if(System.getProperty(\"distributeme.json.ssl.keyPassword\") != null) {");
        increaseIdent();
        writeStatement("connector.setKeyPassword(System.getProperty(\"distributeme.json.ssl.keyPassword\"))");
        decreaseIdent();
        writeString("}");
        writeString("if(System.getProperty(\"distributeme.json.ssl.truststore\") != null) {");
        increaseIdent();
        writeStatement("connector.setTruststore(System.getProperty(\"distributeme.json.ssl.truststore\"))");
        decreaseIdent();
        writeString("}");
        writeString("if(System.getProperty(\"distributeme.json.ssl.trustPassword\") != null) {");
        increaseIdent();
        writeStatement("connector.setTrustPassword(System.getProperty(\"distributeme.json.ssl.trustPassword\"))");
        decreaseIdent();
        writeString("}");
        writeStatement("connector.setPort(descriptor.getPort())");
        writeStatement("server.addConnector(connector)");
        decreaseIdent();
        writeString("} else {");
        increaseIdent();
        writeStatement("final SelectChannelConnector connector = new SelectChannelConnector()");
        writeStatement("connector.setPort(descriptor.getPort())");
        writeStatement("server.addConnector(connector)");
        decreaseIdent();
        writeString("}");


        writeStatement("final Context context = new Context(server, \"/servlets\", Context.SESSIONS)");
        writeStatement("ServletHolder servletHolder = new ServletHolder(new JsonRpcServlet())");
        writeStatement("servletHolder.setInitParameter(descriptor.getInstanceId(), clientStub.getClass().getName())");
        writeStatement("context.addServlet(servletHolder, \"/\" + descriptor.getInstanceId())");
        writeStatement("server.start()");
        closeBlock("startHttpEndpoint");


        if (!supportService) {
            emptyline();
            //METHOD createSupportServicesAndRegisterLocally
            writeString("public static void createSupportServicesAndRegisterLocally() throws Exception{");
            increaseIdent();

            for (String s : SUPPORT_SERVICES) {
                writeStatement(s + ".init()");
                writeStatement(s + ".createServiceAndRegisterLocally()");
            }
            closeBlock("createSupportServicesAndRegisterLocally");
            emptyline();
        }

        if (combinedServer) {
            emptyline();
            writeString("public static void createCombinedServicesAndRegisterLocally() throws Exception{");
            increaseIdent();
            List<String> targetServicesNames = getCombinedServicesNames(type);
            for (String service : targetServicesNames) {
                writeStatement(getServerName(service) + ".init()");
                writeStatement(getServerName(service) + ".createServiceAndRegisterLocally()");
            }
//			for (String s : SUPPORT_SERVICES){
//				writeStatement(s+".init()");
//				writeStatement(s+".createServiceAndRegisterLocally()");
//			}
            closeBlock("createCombinedServicesAndRegisterLocally");
            emptyline();
        }

        closeBlock();


        writer.flush();
        writer.close();
    }


    private List<String> getCombinedServicesNames(TypeDeclaration type) {
        AnnotationMirror ann = findMirror(type, CombinedService.class);
        AnnotationValue val = findMethodValue(ann, "services");
        @SuppressWarnings("unchecked") ArrayList<AnnotationValueImpl> values = (ArrayList<AnnotationValueImpl>) val.getValue();
        ArrayList<String> ret = new ArrayList<String>();
        for (AnnotationValueImpl o : values) {
            ret.add(o.getValue().toString());
        }
        return ret;
    }
}
