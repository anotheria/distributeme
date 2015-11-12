package org.distributeme.generator.jsonrpc;


import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ReferenceType;
import org.apache.commons.lang.StringUtils;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.DontRoute;
import org.distributeme.annotation.FailBy;
import org.distributeme.annotation.Route;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.Defaults;
import org.distributeme.core.exception.DistributemeRuntimeException;
import org.distributeme.core.exception.ServiceUnavailableException;
import org.distributeme.core.interceptor.InterceptionPhase;
import org.distributeme.core.routing.Router;
import org.distributeme.generator.logwriter.LogWriter;
import org.distributeme.generator.logwriter.SysErrorLogWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generator for json-rpc based client stubs.
 *
 * @author lrosenberg
 */
public class ClientServiceImplGenerator extends AbstractJsonRpcGenerator implements JsonRpcGenerator {

    private static Logger log = LoggerFactory.getLogger(ClientServiceImplGenerator.class);

    private final Filer filer;

    public ClientServiceImplGenerator(final Filer file) {
        this.filer = file;
    }

    @Override
    public void generate(TypeDeclaration type) throws IOException {
        PrintWriter writer = filer.createSourceFile(getPackageName(type) + "." + getClientStubName(type));
        setWriter(writer);

        //logging annotation resolving
        LogWriter logWriter = null;
        //System.out.println("====================");
        try {
            AnnotationMirror logWriterMirror = findMirror(type, DistributeMe.class);
            AnnotationValue logWriterClazzValue = findLogWriterValue(logWriterMirror);
            //System.out.println("Type: "+type+", Mirror "+logWriterMirror+", clazzValue: "+logWriterClazzValue+", allvalues: "+logWriterMirror.getElementValues());
            String logWriterClazzName = null;
            if (logWriterClazzValue == null) {
                logWriterClazzName = SysErrorLogWriter.class.getName();
            } else {
                logWriterClazzName = "" + logWriterClazzValue.getValue();
            }

            logWriter = (LogWriter) (Class.forName(logWriterClazzName).newInstance());
            //System.out.println("@@@ created log writer "+logWriter);
        } catch (Exception e) {
            log.warn("Still have this stupid exception...", e);
            logWriter = new SysErrorLogWriter();
        }
        //System.out.println("====================");

        String loggerInitialization = logWriter.createLoggerInitialization(getStubName(type));
        if (loggerInitialization != null && loggerInitialization.length() > 0) {
            writeStatement(loggerInitialization);
        }
        emptyline();

        DistributeMe ann = type.getAnnotation(DistributeMe.class);
        writePackage(type);
        emptyline();
        writeImport(Logger.class);
		writeImport(LoggerFactory.class);
        writeImport(List.class);
        writeImport(Map.class);
        writeImport(getInterfaceFullName(type));
        writeImport("cz.eman.jsonrpc.shared.JsonTransformer");
        writeImport("cz.eman.jsonrpc.shared.bo.JsonRpcNormalResponse");
        writeImport("cz.eman.jsonrpc.shared.bo.JsonRpcErrorResponse");
        writeImport("cz.eman.jsonrpc.shared.bo.JsonRpcRequest");
        writeImport("org.codehaus.jackson.JsonNode");
        writeImport("org.codehaus.jackson.map.ObjectMapper");
        writeImport("org.codehaus.jackson.node.ArrayNode");
        writeImport("org.codehaus.jackson.node.JsonNodeFactory");
        writeImport(ClientSideCallContext.class);
        writeImport(org.distributeme.core.DiscoveryMode.class);
        writeImport("org.distributeme.core.RegistryUtil");
        writeImport("org.distributeme.core.ServiceDescriptor");
        writeImport("org.distributeme.core.exception.NoConnectionToServerException");
        writeImport("org.distributeme.core.failing.FailDecision");
        writeImport("org.distributeme.core.failing.FailingStrategy");
        writeImport("org.mortbay.io.ByteArrayBuffer");
        writeImport("org.mortbay.jetty.client.ContentExchange");
        writeImport("org.mortbay.jetty.client.HttpClient");
        writeImport("java.io.IOException");
        writeImport("java.util.concurrent.ConcurrentHashMap");
        writeImport("java.util.concurrent.ConcurrentMap");
        writeImport("java.util.concurrent.atomic.AtomicInteger");
        writeImport("java.util.ArrayList");
        writeImport("java.util.HashMap");
        writeImport("java.util.List");
        writeImport("java.util.Map");
        writeImport("java.util.Collection");
        writeImport("java.util.concurrent.ConcurrentHashMap");
        writeImport("java.util.concurrent.ConcurrentMap");
        writeImport("java.util.concurrent.ConcurrentMap");
        writeImport("java.util.concurrent.atomic.AtomicInteger");
        writeImport("org.distributeme.core.concurrencycontrol.ConcurrencyControlStrategy");
        writeImport("org.distributeme.core.interceptor.ClientSideRequestInterceptor");
        writeImport("org.distributeme.core.interceptor.InterceptionContext");
        writeImport("org.distributeme.core.interceptor.InterceptionPhase");
        writeImport("org.distributeme.core.interceptor.InterceptorRegistry");
        writeImport("org.distributeme.core.interceptor.InterceptorResponse");
        writeImport("org.distributeme.core.exception.DistributemeRuntimeException");
        writeImport("org.distributeme.core.exception.NoConnectionToServerException");
        writeImport("org.distributeme.core.exception.ServiceUnavailableException");
        writeImport(Defaults.class);

        emptyline();

        writeString("public class " + getClientStubName(type) + " implements " + getServerInterfaceName(type) + "," + getInterfaceName(type) + " {");
        increaseIdent();
        emptyline();
        writeStatement("private static Logger log = LoggerFactory.getLogger(" + getClientStubName(type) + ".class)");
        emptyline();

        writeStatement("private " + type.getQualifiedName() + " implementation");
        emptyline();
        writeStatement("private long lastAccess");
        writeStatement("private long created");
        emptyline();
        writeStatement("private volatile ConcurrentMap<String, " + getServerInterfaceName(type) + "> delegates = new ConcurrentHashMap<String, " + getServerInterfaceName(type) + " >()");
        emptyline();
        writeStatement("private DiscoveryMode discoveryMode = DiscoveryMode.AUTO");
        writeStatement("private HttpClient client");

        AnnotationMirror clazzWideRoute = findMirror(type, Route.class);
        if (clazzWideRoute != null) {
            //System.out.println("Class wide router "+Router.class.getName());
            writeCommentLine("Class wide router ");
            writeStatement("private final " + Router.class.getName() + " clazzWideRouter");
        } else {
            writeCommentLine("No class-wide-router set, skipping.");
        }
        emptyline();

        /***** CONCURRENCY START *****/
        List<TranslatedCCAnnotation> concurrencyControlAnnotations = writeConcurrencyControlDeclarations(type);
        /***** CONCURRENCY END *****/

        Collection<? extends MethodDeclaration> methods = getAllDeclaredMethods(type);
        writeCommentLine("Method wide routers if applicable ");
        Map<MethodDeclaration, AnnotationValue> routedMethods = new HashMap<MethodDeclaration, AnnotationValue>();
        //Map<MethodDeclaration, AnnotationValue> routerParameters = new HashMap<MethodDeclaration, AnnotationValue>();
        for (MethodDeclaration method : methods) {
            AnnotationMirror methodRoute = findMirror(method, Route.class);
            if (methodRoute != null) {
                //System.out.println("Will write "+Router.class.getName()+" "+getMethodRouterName(method));
                writeStatement("private final " + Router.class.getName() + " " + getMethodRouterName(method));
                routedMethods.put(method, findRouterClassValue(methodRoute));
                //routerParameters.put(method, findRouterParameterValue(methodRoute));
            }
        }
        writeCommentLine("Method wide routers END ");
        emptyline();

        writeCommentLine("Failing");
        writeCommentLine("Class wide failing strategy ");
        AnnotationMirror clazzWideFailingStrategyAnnotation = findMirror(type, FailBy.class);
        String clazzWideFailingStrategyName = null;
        if (clazzWideFailingStrategyAnnotation != null) {
            clazzWideFailingStrategyName = "" + findMethodValue(clazzWideFailingStrategyAnnotation, "strategyClass").getValue();
        }
        if (clazzWideFailingStrategyName != null) {
            writeStatement("private FailingStrategy clazzWideFailingStrategy = new " + clazzWideFailingStrategyName + "()");
        } else {
            writeStatement("private FailingStrategy clazzWideFailingStrategy = " + Defaults.class.getSimpleName() + ".getDefaultFailingStrategy()");
        }
        emptyline();
        for (MethodDeclaration method : methods) {
            AnnotationMirror methodFailingStrategyAnnotation = findMirror(method, FailBy.class);
            String methodFailingStrategyName;
            if (methodFailingStrategyAnnotation != null) {
                methodFailingStrategyName = "" + findMethodValue(methodFailingStrategyAnnotation, "strategyClass").getValue();
                writeStatement("private FailingStrategy " + getFailingStrategyVariableName(method) + " = new " + methodFailingStrategyName + "()");
            } else {
                writeStatement("private FailingStrategy " + getFailingStrategyVariableName(method) + " = clazzWideFailingStrategy");
            }

        }
        writeCommentLine("Failing end");
        emptyline();

        writeStatement("private ServiceDescriptor lookupDescriptor");
        writeStatement("private AtomicInteger requestID = new AtomicInteger(0)");
        emptyline();
        writeString("public " + getClientStubName(type) + "(" + type.getQualifiedName() + " anImplementation){");
        increaseIdent();
        writeStatement("created = System.currentTimeMillis()");
        writeStatement("implementation = anImplementation");
        writeStatement("discoveryMode = DiscoveryMode.AUTO");
        closeBlock();
        emptyline();
        writeString("private " + getClientStubName(type) + "(final ServiceDescriptor lookupDescriptor){");
        increaseIdent();
        writeStatement("this.lookupDescriptor = lookupDescriptor");
        closeBlock();
        // parameters
        Map<String, String> primitiveTypes = new HashMap<String, String>();
        primitiveTypes.put("long", "Long");
        primitiveTypes.put("int", "Integer");
        for (MethodDeclaration method : methods) {
            String methodDecl = getSkeletonMethodDeclaration(method);
            writeString("public " + methodDecl + "{");
            increaseIdent();
            writeStatement("ArrayNode jsonNode = JsonNodeFactory.instance.arrayNode()");
            writeStatement("ObjectMapper m = new ObjectMapper()");
            for (ParameterDeclaration p : method.getParameters()) {
                String call = "";


                if (primitiveTypes.containsKey(p.getType().toString())) {
                    writeStatement("jsonNode.add(" + p.getSimpleName() + ")");
                } else if (p.getType().toString().contains("[]")) {
                    writeStatement("ArrayNode jsonArrayNode = JsonNodeFactory.instance.arrayNode()");
                    writeString("for(byte byte1 : " + p.getSimpleName() + ") {");
                    increaseIdent();
                    writeStatement("jsonArrayNode.add(byte1)");
                    decreaseIdent();
                    writeString("}");
                    writeStatement("jsonNode.add(jsonArrayNode)");
                } else {
                    writeStatement("jsonNode.add(m.valueToTree(" + p.getSimpleName() + "))");
                }
                if (!StringUtils.isEmpty(call)) {
                    writeStatement(call);
                }
            }
            writeStatement("jsonNode.add(m.<JsonNode>valueToTree(__transportableCallContext))");
            writeStatement("final List retList = new ArrayList()");
            String call = "";
            if (primitiveTypes.containsKey(method.getReturnType().toString())) {
                writeString("try {");
                writeStatement("String response = callHttpClient(\"" + method.getSimpleName() + "\", jsonNode)");
                writeStatement("if(response == null) return null");
                writeStatement("final List tempRetList = (List) JsonTransformer.toObject(response, List.class)");
                writeStatement("retList.add(" + primitiveTypes.get(method.getReturnType().toString()) + ".valueOf(String.valueOf(tempRetList.get(0))))");
                writeStatement("retList.add(tempRetList.get(1))");
                writeStatement("return retList");
                writeString("} catch (IOException e) {");
                writeIncreasedStatement("log.error(\"" + method.getSimpleName() + "()\", e)");
                writeIncreasedStatement("throw new RuntimeException(e)");
                writeString("}");
            } else if (method.getReturnType().toString().equals("void")) {
                call += "callHttpClient(\"" + method.getSimpleName() + "\", jsonNode);\n";
                call += "return retList";

            } else {
                writeString("try {");
                writeStatement("String response = callHttpClient(\"" + method.getSimpleName() + "\", jsonNode)");
                writeStatement("if(response == null) return null");
                writeStatement("final List tempRetList = (List) JsonTransformer.toObject(response, List.class)");
                writeStatement("JsonNode arrayNode = m.readTree(response)");

                writeStatement("retList.add(m.readValue(arrayNode.get(0), " + method.getReturnType() + ".class))");
                writeStatement("retList.add(tempRetList.get(1))");

                writeStatement("return retList");
                writeString("} catch (IOException e) {");
                writeIncreasedStatement("log.error(\"" + method.getSimpleName() + "()\", e)");
                writeIncreasedStatement("throw new RuntimeException(e)");
                writeString("}");
            }
            if (!StringUtils.isEmpty(call)) {
                writeStatement(call);
            }
            closeBlock();//method end
            emptyline();
        }
        emptyline();

        // returns

        for (MethodDeclaration method : methods) {
            writeString("public " + getStubMethodDeclaration(method) + "{");
            increaseIdent();
            StringBuilder callToPrivate = new StringBuilder(method.getSimpleName() + "(");
            for (ParameterDeclaration p : method.getParameters()) {
                callToPrivate.append(p.getSimpleName());
                callToPrivate.append(", ");
            }
            writeStatement((isVoidReturn(method) ? "" : "return ") + callToPrivate.toString() + "(ClientSideCallContext)null)");
            closeBlock("public " + getStubMethodDeclaration(method));
            emptyline();
        }

        emptyline();
        for (MethodDeclaration method : methods) {
            StringBuilder callToPrivate = new StringBuilder(method.getSimpleName() + "(");
            for (ParameterDeclaration p : method.getParameters()) {
                callToPrivate.append(p.getSimpleName());
                callToPrivate.append(", ");
            }
            //writeStatement((isVoidReturn(method) ? "" : "return ")+callToPrivate.toString()+"(ClientSideCallContext)null)");

            String methodDecl = getInternalStubMethodDeclaration(method);
            writeString("public " + methodDecl + "{");
            increaseIdent();
            writeStatement("List __fromServerSide = null;");
            writeStatement("Exception exceptionInMethod = null");
            writeString("if (diMeCallContext == null)");
            writeIncreasedStatement("diMeCallContext = new ClientSideCallContext(" + quote(method.getSimpleName()) + ")");
            writeString("if (discoveryMode==DiscoveryMode.AUTO && diMeCallContext.getServiceId()==null)");
            writeIncreasedStatement("diMeCallContext.setServiceId(" + getConstantsName(type) + ".getServiceId())");
            emptyline();
            writeStatement("HashMap __transportableCallContext = diMeCallContext.getTransportableCallContext()");

            //interceptors, phase 1
            boolean interceptionEnabled = true; //later can be configured via annotation.
            writeCommentLine("Initialize interceptors");
            writeStatement("List<ClientSideRequestInterceptor> diMeInterceptors = InterceptorRegistry.getInstance().getClientSideRequestInterceptors()");
            writeStatement("InterceptionContext diMeInterceptionContext = new InterceptionContext()");
//			writeInterceptionBlock(InterceptionPhase.BEFORE_CLIENT, method);


            //concurrency control
            writeCommentLine("Concurrency control, client side - start");
            writeStatement(getCCStrategyVariableName(method) + ".notifyClientSideCallStarted(diMeCallContext)");
            emptyline();

            if (interceptionEnabled) {
                //create parameters
                writeStatement("ArrayList<Object> diMeParameters = new ArrayList<Object>()");
                Collection<? extends ParameterDeclaration> parameters = method.getParameters();
                for (ParameterDeclaration p : parameters) {
                    writeStatement("diMeParameters.add(" + p.getSimpleName() + ")");
                }
                writeStatement("diMeCallContext.setParameters(diMeParameters)");
            }

//			boolean doRoute = false;
            AnnotationMirror methodMirror = findMirror(method, DontRoute.class);
            if (methodMirror != null) {
                writeCommentLine("explicitely skipping routing for method " + method);
            } else {
                String routerName = null;
                if (clazzWideRoute != null)
                    routerName = "clazzWideRouter";
                if (routedMethods.get(method) != null) {
                    routerName = getMethodRouterName(method);
                }
                if (routerName != null) {
//					doRoute = true;
                    if (!interceptionEnabled) {
                        //this means that we have to create parameters for context
                        writeStatement("ArrayList<Object> diMeParameters = new ArrayList<Object>()");
                        Collection<? extends ParameterDeclaration> parameters = method.getParameters();
                        for (ParameterDeclaration p : parameters) {
                            writeStatement("diMeParameters.add(" + p.getSimpleName() + ")");
                        }
                        writeStatement("diMeCallContext.setParameters(diMeParameters)");

                    }
					writeStatement("diMeCallContext.setServiceId(" + routerName + ".getServiceIdForCall(diMeCallContext))");
                }
            }

            writeString("try{");
            increaseIdent();
            writeInterceptionBlock(InterceptionPhase.BEFORE_SERVICE_CALL, method);

            //now reparse parameters
            writeCommentLine("Reparse parameters in case an interceptor modified them");
            Collection<? extends ParameterDeclaration> parameters = method.getParameters();
            int parameterCounter = 0;
            for (ParameterDeclaration p : parameters) {
                writeStatement(p.getSimpleName() + " = " + convertReturnValue(p.getType(), "diMeParameters.get(" + (parameterCounter++) + ")"));
                //writeStatement("diMeParameters.add("+p.getSimpleName()+")");
            }

            String call = "__fromServerSide = getDelegate(diMeCallContext.getServiceId())." + method.getSimpleName();
            String paramCall = "";
            for (ParameterDeclaration p : parameters) {
                if (paramCall.length() != 0)
                    paramCall += ", ";
                paramCall += p.getSimpleName();
            }
            if (paramCall.length() > 0)
                paramCall += ", ";
            paramCall += " __transportableCallContext";
            call += "(" + paramCall + ");";
            writeString(call);
            writeStatement("__transportableCallContext.putAll(((HashMap)__fromServerSide.get(1)))");
            if (isVoidReturn(method)) {
                writeStatement("return");
            } else {
                writeStatement("return " + convertReturnValue(method.getReturnType(), "__fromServerSide.get(0)"));
            }
            decreaseIdent();
            writeString("} catch(NoConnectionToServerException e) {");
            writeIncreasedStatement("exceptionInMethod = e");
            writeString("} finally {");
            //concurrency control
            writeCommentLine("Concurrency control, client side - end");
            writeIncreasedStatement(getCCStrategyVariableName(method) + ".notifyClientSideCallFinished(diMeCallContext)");
            writeInterceptionBlock(InterceptionPhase.AFTER_SERVICE_CALL, method);
            writeString("}");//catch


            //failing
            writeCommentLine("Failing");
            writeString("if (exceptionInMethod!=null){");
            increaseIdent();
            writeStatement("FailDecision failDecision = " + getFailingStrategyVariableName(method) + ".callFailed(diMeCallContext)");
            writeString("if (failDecision.getTargetService()!=null)");
            writeIncreasedStatement("diMeCallContext.setServiceId(failDecision.getTargetService())");
            writeString("switch(failDecision.getReaction()){");
            increaseIdent();
            writeString("case RETRY:");
            if (!isVoidReturn(method)) {
                writeIncreasedStatement("return " + callToPrivate + "diMeCallContext.increaseCallCount())");
            } else {
                writeIncreasedStatement(callToPrivate + "diMeCallContext.increaseCallCount())");
                writeIncreasedStatement("return");
            }

            writeString("case RETRYONCE:");
            increaseIdent();
            writeCommentLine("Only retry if its the first call");
            writeString("if (!diMeCallContext.isFirstCall())");
            writeIncreasedStatement("break");
            if (!isVoidReturn(method)) {
                writeStatement("return " + callToPrivate + "diMeCallContext.increaseCallCount())");
            } else {
                writeStatement(callToPrivate + "diMeCallContext.increaseCallCount())");
                writeStatement("return");
            }
            decreaseIdent();


            writeString("case FAIL:");
            writeString("default:");
            writeCommentLine("Fail or default is to do nothing at all and let the request fail");
            closeBlock("switch(failDecision)");

            closeBlock();
            writeCommentLine("fail through, if we are here, we must have had an exception before.");
            writeStatement("throw mapException(exceptionInMethod)");
            closeBlock();//method end
            emptyline();
        }

        emptyline();
        writeString("private void notifyDelegateFailed(){");
        increaseIdent();
        writeStatement("notifyDelegateFailed(" + getConstantsName(type) + ".getServiceId())");
        closeBlock();
        emptyline();

        writeString("private void notifyDelegateFailed(String serviceId){");
        increaseIdent();
        writeStatement("delegates.remove(serviceId)");
        closeBlock();
        emptyline();

        writeString("private " + getServerInterfaceName(type) + " getDelegate() throws NoConnectionToServerException{");
        increaseIdent();
        writeStatement("return getDelegate(" + getConstantsName(type) + ".getServiceId())");
        closeBlock();
        emptyline();

        writeString("private " + getServerInterfaceName(type) + " getDelegate(String serviceId) throws NoConnectionToServerException{");
        increaseIdent();
        writeCommentLine("if no serviceid is provided, fallback to default resolve with manual mode");
        writeString("if (serviceId==null)");
        writeIncreasedStatement("return getDelegate()");
        writeStatement(getServerInterfaceName(type) + " delegate = delegates.get(serviceId)");
        writeString("if (delegate==null){");
        increaseIdent();
        openTry();
        writeStatement("delegate = lookup(serviceId)");
        writeStatement("delegates.putIfAbsent(serviceId, delegate)");
        decreaseIdent();
        writeString("}catch(Exception e){");
        //TODO replace this with a typed exception!
        writeCommentLine("//TODO - generate and throw typed exception.");
        writeIncreasedStatement("log.error(\"getDelegate()\", e)");
        writeIncreasedStatement("throw new NoConnectionToServerException(\"Couldn't lookup delegate because: \"+e.getMessage()+\" at \"+RegistryUtil.describeRegistry(), e)");
        writeString("}//try");
        closeBlock("first if (del==null) ");
        writeStatement("return delegate");
        closeBlock("fun");
        emptyline();

        writeString("private " + getServerInterfaceName(type) + " lookup(String serviceId) throws NoConnectionToServerException{");
        increaseIdent();
        writeCommentLine("//first we need to lookup target host.");
        writeStatement("ServiceDescriptor toLookup = new ServiceDescriptor(ServiceDescriptor.Protocol.JSONRPC, serviceId)");
        writeStatement("ServiceDescriptor targetService = RegistryUtil.resolve(toLookup)");
        writeString("if (targetService==null)");
        writeIncreasedStatement("throw new RuntimeException(" + quote("Can't resolve host for an instance of ") + "+" + getConstantsName(type) + ".getServiceId())");
        writeStatement("return new " + getClientStubName(type) + "(targetService)");

        closeBlock();
        emptyline();

        //service adapter methods.
        writeCommentLine("Service adapter methods");
        writeString("public long getCreationTimestamp(){ return created; }");
        writeString("public long getLastAccessTimestamp(){ return lastAccess; }");


        writeString("public " + getInterfaceName(type) + " getImplementation() { return implementation; }");

        writeString("private String callHttpClient(final String methodName, final JsonNode paramNode) {");
        increaseIdent();

        writeString("if(client ==null) {");
        increaseIdent();
        writeStatement("client = new HttpClient()");
        writeStatement("client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL)");
        writeString("if(System.getProperty(\"distributeme.json.ssl\") != null) {");
        increaseIdent();
        writeStatement("client.setKeyStoreLocation(System.getProperty(\"distributeme.json.ssl.keystore\"))");
        writeStatement("client.setKeyStorePassword(System.getProperty(\"distributeme.json.ssl.password\"))");
        writeStatement("client.setKeyManagerPassword(System.getProperty(\"distributeme.json.ssl.keyPassword\"))");
        writeString("if(System.getProperty(\"distributeme.json.ssl.truststore\") != null) {");
        increaseIdent();
        writeStatement("client.setTrustStoreLocation(System.getProperty(\"distributeme.json.ssl.truststore\"))");
        decreaseIdent();
        writeString("}");
        writeString("if(System.getProperty(\"distributeme.json.ssl.trustPassword\") != null) {");
        increaseIdent();
        writeStatement("client.setTrustStorePassword(System.getProperty(\"distributeme.json.ssl.trustPassword\"))");
        decreaseIdent();
        writeString("}");
        decreaseIdent();
        writeString("}");


        writeString("try {");
        writeIncreasedStatement("client.start()");
        writeString("} catch (Exception e) {");
        writeIncreasedStatement("log.error(\"callHttpClient()\", e)");
        writeIncreasedStatement("throw new RuntimeException(e)");
        writeString("}");
        decreaseIdent();
        writeString("}");
        writeStatement("final ContentExchange exchange = new ContentExchange()");
        writeStatement("exchange.setMethod(\"POST\")");
        writeString("if(System.getProperty(\"distributeme.json.ssl\") == null) {");
        writeStatement("exchange.setURL(\"http://\" + lookupDescriptor.getHost() + \":\" + lookupDescriptor.getPort() + \"/servlets/\" + lookupDescriptor.getInstanceId())");
        writeString("} else {");
        writeStatement("exchange.setURL(\"https://\" + lookupDescriptor.getHost() + \":\" + lookupDescriptor.getPort() + \"/servlets/\" + lookupDescriptor.getInstanceId())");
        writeString("}");
        writeStatement("JsonRpcRequest jsonRpcRequest = new JsonRpcRequest()");
        writeStatement("jsonRpcRequest.setId(requestID.incrementAndGet() + \"\")");
        writeStatement("jsonRpcRequest.setJsonrpc(\"2.0\")");
        writeStatement("jsonRpcRequest.setMethod(methodName)");
        writeStatement("jsonRpcRequest.setParams(paramNode)");
        writeStatement("exchange.setRequestContent(new ByteArrayBuffer(JsonTransformer.toJson(jsonRpcRequest)))");

        writeString("try {");
        increaseIdent();
        writeString("if (log.isDebugEnabled()) {");
        writeIncreasedStatement("log.debug(\"client: send request to server: \" + exchange.toString())");
        writeString("}");
        decreaseIdent();
        writeIncreasedStatement("client.send(exchange)");
        writeIncreasedStatement("exchange.waitForDone()");
        writeIncreasedString("if(exchange.getResponseContent().contains(\"error\")) {");
        increaseIdent();
        writeIncreasedStatement("JsonRpcErrorResponse jsonRpcErrorResponse = (JsonRpcErrorResponse) JsonTransformer.toObject(exchange.getResponseContent(), JsonRpcErrorResponse.class)");
        writeIncreasedStatement("throw new RuntimeException(\"callHttpClient(\" + methodName + \" , \" + paramNode + \") \"  + jsonRpcErrorResponse.getError())");
        writeString("}");
        decreaseIdent();

        writeIncreasedStatement("JsonRpcNormalResponse jsonRpcResponse = (JsonRpcNormalResponse) JsonTransformer.toObject(exchange.getResponseContent(), JsonRpcNormalResponse.class)");
        increaseIdent();
        writeString("if (log.isDebugEnabled()) {");
        writeIncreasedStatement("log.debug(\"client: send request to server: \" + exchange.toString())");
        writeString("}");
        decreaseIdent();
        //writeIncreasedStatement("if(jsonRpcResponse.getResult() == null) return null");

        increaseIdent();
        writeString("if(jsonRpcResponse.getResult() == null) {");
        writeIncreasedStatement("log.debug(\"client: received response from server: jsonRpcResponse.getResult() == null.\") ");
        writeIncreasedStatement("return null");
        writeString("}");
        decreaseIdent();

        increaseIdent();
        writeString("if (log.isDebugEnabled()) {");
        writeIncreasedStatement("log.debug(\"client: client: received response from server: \" + jsonRpcResponse.getResult().toString())");
        writeString("}");
        decreaseIdent();
        writeIncreasedStatement("return jsonRpcResponse.getResult().toString()");
        writeString("} catch (IOException e) {");
        writeIncreasedStatement("log.error(\"callHttpClient()\", e)");
        writeIncreasedStatement("throw new RuntimeException(e)");
        writeString("} catch (InterruptedException e) {");
        writeIncreasedStatement("log.error(\"callHttpClient()\", e)");
        writeIncreasedStatement("throw new RuntimeException(e)");
        writeString("}");
        closeBlock();
        emptyline();


        //write exception mapping block
		emptyline();
		writeString("private "+DistributemeRuntimeException.class.getSimpleName()+" mapException(Exception in){");
		increaseIdent();
		writeString("if (in instanceof "+DistributemeRuntimeException.class.getSimpleName()+")");
		writeIncreasedStatement("return ("+DistributemeRuntimeException.class.getSimpleName()+") in");
		writeStatement("return new "+ServiceUnavailableException.class.getSimpleName()+"(\"Unexpected exception: \"+in.getMessage()+\" \" + in.getClass().getName(), in)");
		closeBlock();

        //write concurrency control strategy created methods
        for (TranslatedCCAnnotation cca : concurrencyControlAnnotations) {
            writeConcurrencyControlCreationMethod(cca);
        }

        closeBlock();

        writer.flush();
        writer.close();
    }

    /**
     * Returns true if the method has no return value.
     *
     * @param decl
     * @return
     */
    protected boolean isVoidReturn(MethodDeclaration decl) {
        return decl.getReturnType().toString().equals("void");
    }

    protected String getMethodRouterName(MethodDeclaration declaration) {
        return declaration.getSimpleName() + "Router";
    }

    private void writeInterceptionBlock(InterceptionPhase phase, MethodDeclaration method) {
        boolean afterCall = phase == InterceptionPhase.AFTER_SERVICE_CALL;
        writeStatement("diMeInterceptionContext.setCurrentPhase(InterceptionPhase." + phase.toString() + ")");
        if (afterCall) {
            writeString("if (__fromServerSide!=null){");
            writeIncreasedStatement("diMeInterceptionContext.setReturnValue(__fromServerSide.get(0))");
            writeString("}");
            writeStatement("diMeInterceptionContext.setException(exceptionInMethod)");
            writeStatement("boolean diMeReturnOverriden = false");
        }
        writeString("for (ClientSideRequestInterceptor interceptor : diMeInterceptors){");
        increaseIdent();
        writeStatement("InterceptorResponse interceptorResponse = interceptor." + interceptionPhaseToMethod(phase) + "(diMeCallContext, diMeInterceptionContext)");
        writeString("switch(interceptorResponse.getCommand()){");
        writeString("case ABORT:");
        increaseIdent();
        writeString("if (interceptorResponse.getException() instanceof RuntimeException)");
        writeIncreasedStatement("throw (RuntimeException) interceptorResponse.getException()");
        for (ReferenceType type : method.getThrownTypes()) {
            writeString("if (interceptorResponse.getException() instanceof " + type.toString() + ")");
            writeIncreasedStatement("throw (" + type.toString() + ") interceptorResponse.getException()");
        }
        writeStatement("throw new RuntimeException(" + quote("Interceptor exception") + ",interceptorResponse.getException())");
        decreaseIdent();
        writeString("case RETURN:");
        if (isVoidReturn(method)) {
            writeIncreasedStatement("return");
        } else {
            writeIncreasedStatement("return " + convertReturnValue(method.getReturnType(), "interceptorResponse.getReturnValue()"));
        }
        if (!isVoidReturn(method) && afterCall) {
            writeString("case OVERWRITE_RETURN_AND_CONTINUE:");
            writeIncreasedStatement("__fromServerSide.set(0, interceptorResponse.getReturnValue())");
            writeIncreasedStatement("diMeInterceptionContext.setReturnValue(interceptorResponse.getReturnValue())");
            writeIncreasedStatement("diMeReturnOverriden = true");
            writeIncreasedStatement("break");
        }
        writeString("case CONTINUE:");
        writeIncreasedStatement("break");
        writeString("default:");
        writeIncreasedStatement("throw new IllegalStateException(" + quote("Unsupported or unexpected command from interceptor ") + " + interceptorResponse.getCommand()+ \" in phase:\"+diMeInterceptionContext.getCurrentPhase())");
        increaseIdent();
        closeBlock("switch");

        closeBlock("for");
        if (!isVoidReturn(method) && afterCall) {
            writeString("if (diMeReturnOverriden)");
            writeIncreasedStatement("return " + convertReturnValue(method.getReturnType(), "__fromServerSide.get(0)"));
        }
    }
}
