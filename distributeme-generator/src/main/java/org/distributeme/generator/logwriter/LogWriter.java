package org.distributeme.generator.logwriter;

/**
 * A log writer which can be used to log output via custom framework (like util.logging, log4j, logback etc).
 * @author lrosenberg
 *
 */
public interface LogWriter {
	String createExceptionOutput(String message, String exceptionName);
	
	String createErrorOutput(String message);
	
	String createErrorOutputWithException(String message, String exceptionName);
	/**
	 * Provides code that is needed to initialize the logger.
	 * @param className
	 * @return
	 */
	String createLoggerInitialization(String className);
}
 