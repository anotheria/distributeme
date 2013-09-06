package org.distributeme.generator;

import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ReferenceType;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.core.Defaults;
import org.distributeme.core.ServiceLocator;
import org.distributeme.core.asynch.CallBackHandler;
import org.distributeme.core.asynch.CallTimeoutedException;
import org.distributeme.core.asynch.SingleCallHandler;
import org.distributeme.core.exception.DistributemeRuntimeException;
import org.distributeme.core.exception.NoConnectionToServerException;
import org.distributeme.core.exception.ServiceUnavailableException;
import org.distributeme.generator.logwriter.LogWriter;
import org.distributeme.generator.logwriter.SysErrorLogWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Generator for RMI based stubs. 
 * @author lrosenberg
 */
public class AsynchStubGenerator extends AbstractStubGenerator implements Generator{
	
	private static Logger log = LoggerFactory.getLogger(AsynchStubGenerator.class);
	
	@Override
	public void generate(TypeDeclaration type, Filer filer, Map<String,String> options) throws IOException{
		
		//System.out.println("%%%\nStarting generating "+type+"\n\n");
		
		DistributeMe typeAnnotation = type.getAnnotation(DistributeMe.class);
		if (!typeAnnotation.asynchSupport())
			return;
		PrintWriter writer = filer.createSourceFile(getPackageName(type)+"."+getAsynchStubName(type));
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
		writeImport(type.getQualifiedName());
		writeImport(InterruptedException.class);
		
		
		writeImport(DistributemeRuntimeException.class);
		writeImport(NoConnectionToServerException.class);
		writeImport(ServiceUnavailableException.class);
		writeImport(Defaults.class);
		writeImport(CallBackHandler.class);
		writeImport(SingleCallHandler.class);
		writeImport(CallTimeoutedException.class);
		writeImport(IllegalStateException.class);
		writeImport(ServiceLocator.class);
		writeImport(ExecutorService.class);
		writeImport(Executors.class);
		writeImport(AtomicLong.class);
		emptyline();
		
		writeString("public class "+getAsynchStubName(type)+" implements "+getAsynchInterfaceName(type)+"{");
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
	
		
		Collection<? extends MethodDeclaration> methods = getAllDeclaredMethods(type);
		
		writeStatement("private final "+getInterfaceName(type)+" diMeTarget");
		writeStatement("private final ExecutorService diMeExecutor");
		writeStatement("private final AtomicLong diMeRequestCounter = new AtomicLong()");
		emptyline();

		//create AUTO constructor
		writeString("public "+getAsynchStubName(type)+"(){");
		increaseIdent();
		writeStatement("this(ServiceLocator.getRemote("+type.getSimpleName()+".class))");
		closeBlock();
		emptyline();
	
		emptyline();
		writeString("public "+getAsynchStubName(type)+"("+getInterfaceName(type)+" aTarget){");
		increaseIdent();
		writeStatement("diMeTarget = aTarget");
		if (typeAnnotation.asynchExecutorPoolSize()==-1){
			//use defaults
			writeStatement("diMeExecutor = Executors.newFixedThreadPool(Defaults.getAsynchExecutorPoolSize())");
		}else{
			writeStatement("diMeExecutor = Executors.newFixedThreadPool("+typeAnnotation.asynchExecutorPoolSize()+")");
		}
		
		closeBlock();
		emptyline();
	
		////////// METHODS ///////////
		for (MethodDeclaration method : methods){
			writeString("public "+getStubMethodDeclaration(method)+"{");
			increaseIdent();
			writeStatement("SingleCallHandler diMeCallHandler = new SingleCallHandler()");
			String callParams = getStubParametersCall(method);
			if (callParams.length()>0)
				callParams += ", ";
			callParams += "diMeCallHandler";
			writeStatement(getAsynchMethodName(method)+"("+callParams+")");
			writeString("try{");
			increaseIdent();
			
			if (typeAnnotation.asynchCallTimeout()==-1){
				//use defaults
				writeStatement("diMeCallHandler.waitForResults()");
			}else{
				writeStatement("diMeCallHandler.waitForResults("+typeAnnotation.asynchCallTimeout()+")");
			}
			
			decreaseIdent();
			writeString("}catch(InterruptedException e){");
			increaseIdent();
			closeBlock();
			
			writeString("if (!diMeCallHandler.isFinished())");
			writeIncreasedStatement("throw new CallTimeoutedException()");
			if (isVoidReturn(method)){
				writeString("if (diMeCallHandler.isSuccess())");
				writeIncreasedStatement("return");
			}else{
				writeString("if (diMeCallHandler.isSuccess())");
				writeIncreasedStatement("return "+convertReturnValue(method.getReturnType(), "diMeCallHandler.getReturnValue()"));
			}

			
			writeString("if (diMeCallHandler.isError()){");
			increaseIdent();
			writeStatement("Exception exceptionInMethod = diMeCallHandler.getReturnException()");
			writeString("if (exceptionInMethod instanceof RuntimeException)");
			writeIncreasedStatement("throw (RuntimeException)exceptionInMethod");
			if (method.getThrownTypes().size()>0){
				for (ReferenceType refType : method.getThrownTypes()){
					writeString("if (exceptionInMethod instanceof "+refType.toString()+")");
					writeIncreasedStatement("throw ("+refType.toString()+")exceptionInMethod");
				}
			}
			
			
			writeStatement("throw new RuntimeException("+quote("Shouldn't happen, unexpected exception of class ")+"+exceptionInMethod.getClass().getName()+"+quote(" thrown by method")+", exceptionInMethod)");
			closeBlock("if isError");

			writeStatement("throw new IllegalStateException("+quote("You can't be here ;-)")+")");
			closeBlock(getStubMethodDeclaration(method));
			emptyline();

			
			writeString("public "+getStubAsynchMethodDeclaration(method)+"{");
			increaseIdent();
			writeString("diMeExecutor.execute(new Runnable() {");
			increaseIdent();
			writeString("@Override");
			writeString("public void run() {");
			increaseIdent();
			writeStatement("long diMeRequestNumber = diMeRequestCounter.incrementAndGet()");
			//writeStatement("System.out.println(this+\" started \"+diMeRequestNumber)");
			openTry();
			writeCommentLine("make the real call here");
			String call = "diMeTarget."+method.getSimpleName()+"("+getStubParametersCall(method)+")";
			if (!isVoidReturn(method)){
				call = method.getReturnType()+" diMeReturnValue = "+call;
			}
			writeStatement(call);
			writeString("if (diMeHandlers!=null){");
			increaseIdent();
			writeString("for (CallBackHandler diMeHandler : diMeHandlers){");
			increaseIdent();
			openTry();
			//writeStatement("System.out.println(\"Calling \"+diMeHandler+\".success(\"+"+(isVoidReturn(method)?"null":"diMeReturnValue")+"+\")\");" );
			writeStatement("diMeHandler.success("+((!isVoidReturn(method)) ? "diMeReturnValue":"null")+")");
			decreaseIdent();
			writeString("}catch(Exception ignored){");
			writeCommentLine("add exception warn here");
			writeString("}");
			closeBlock("for");
			closeBlock("if");
			
			
			
			decreaseIdent();
			writeString("}catch(Exception e){");
			increaseIdent();
			//writeString("e.printStackTrace();");
			writeString("if (diMeHandlers!=null){");
			increaseIdent();
			writeString("for (CallBackHandler diMeHandler : diMeHandlers){");
			increaseIdent();
			openTry();
			writeStatement("diMeHandler.error(e)");
			decreaseIdent();
			writeString("}catch(Exception ignored){");
			writeCommentLine("add exception warn here");
			writeString("}");
			closeBlock("for");
			closeBlock("if");
			closeBlock("catch");
			closeBlock();
			decreaseIdent();
			writeString("});");

			closeBlock("public "+getStubAsynchMethodDeclaration(method));
			emptyline();
		}
		
		emptyline();
		writeString("public void shutdown(){");
		increaseIdent();
		writeStatement("diMeExecutor.shutdown()");
		closeBlock();
		
		
		closeBlock();
		
		
		writer.flush();
		writer.close();
		//System.out.println("%%%\finished generating "+type+"");

	}
}
