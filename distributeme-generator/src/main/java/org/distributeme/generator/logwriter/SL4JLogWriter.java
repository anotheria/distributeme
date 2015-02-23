package org.distributeme.generator.logwriter;

/**
 * Log4j based implementation fo the log writer.
 * @author lrosenberg
 *
 */
public class SL4JLogWriter implements LogWriter{

	@Override
	public String createExceptionOutput(String message, String exceptionName) {
		return "log.error("+message+", "+exceptionName+")";
	}
	@Override
	public String createLoggerInitialization(String className) {
		return "private static Logger log = org.slf4j.LoggerFactory.getLogger("+className+".class)";
	}
	@Override
	public String createErrorOutput(String message) {
		return "log.error("+message+")"; 
	}
	@Override
	public String createErrorOutputWithException(String message, String exceptionName) {
		return "log.error("+message+","+exceptionName+")"; 
	}

}
