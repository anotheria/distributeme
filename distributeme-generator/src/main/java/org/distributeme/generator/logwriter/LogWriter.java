package org.distributeme.generator.logwriter;

/**
 * A log writer which can be used to log output via custom framework (like util.logging, log4j, logback etc).
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public interface LogWriter {
	/**
	 * <p>createExceptionOutput.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param exceptionName a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	String createExceptionOutput(String message, String exceptionName);
	
	/**
	 * <p>createErrorOutput.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	String createErrorOutput(String message);
	
	/**
	 * <p>createErrorOutputWithException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param exceptionName a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	String createErrorOutputWithException(String message, String exceptionName);
	/**
	 * Provides code that is needed to initialize the logger.
	 *
	 * @param className a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	String createLoggerInitialization(String className);
}
 
