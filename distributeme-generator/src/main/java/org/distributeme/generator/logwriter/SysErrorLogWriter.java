package org.distributeme.generator.logwriter;

/**
 * Implementation of LogWriter which logs everything out to standard error.
 * @author lrosenberg
 *
 */
public class SysErrorLogWriter implements LogWriter{
	@Override
	public String createExceptionOutput(String message, String exceptionName) {
		return exceptionName+".printStackTrace()";
	}

	@Override
	public String createLoggerInitialization(String className) {
		return "";
	}

	@Override
	public String createErrorOutput(String message) {
		return "System.err.println("+message+")"; 
	}

	@Override
	public String createErrorOutputWithException(String message, String exceptionName) {
		return "System.err.println("+message+"); "+exceptionName+".printStackTrace()";
	}

}
