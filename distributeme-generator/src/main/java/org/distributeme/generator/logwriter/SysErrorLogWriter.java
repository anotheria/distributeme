package org.distributeme.generator.logwriter;

/**
 * Implementation of LogWriter which logs everything out to standard error.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class SysErrorLogWriter implements LogWriter{
	/** {@inheritDoc} */
	@Override
	public String createExceptionOutput(String message, String exceptionName) {
		return exceptionName+".printStackTrace()";
	}

	/** {@inheritDoc} */
	@Override
	public String createLoggerInitialization(String className) {
		return "";
	}

	/** {@inheritDoc} */
	@Override
	public String createErrorOutput(String message) {
		return "System.err.println("+message+")"; 
	}

	/** {@inheritDoc} */
	@Override
	public String createErrorOutputWithException(String message, String exceptionName) {
		return "System.err.println("+message+"); "+exceptionName+".printStackTrace()";
	}

}
