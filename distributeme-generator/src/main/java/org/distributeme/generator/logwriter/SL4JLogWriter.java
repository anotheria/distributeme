package org.distributeme.generator.logwriter;

/**
 * Log4j based implementation fo the log writer.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class SL4JLogWriter implements LogWriter{

	/** {@inheritDoc} */
	@Override
	public String createExceptionOutput(String message, String exceptionName) {
		return "log.error("+message+", "+exceptionName+")";
	}
	/** {@inheritDoc} */
	@Override
	public String createLoggerInitialization(String className) {
		return "private static Logger log = org.slf4j.LoggerFactory.getLogger("+className+".class)";
	}
	/** {@inheritDoc} */
	@Override
	public String createErrorOutput(String message) {
		return "log.error("+message+")"; 
	}
	/** {@inheritDoc} */
	@Override
	public String createErrorOutputWithException(String message, String exceptionName) {
		return "log.error("+message+","+exceptionName+")"; 
	}

}
