package org.distributeme.core.interceptor;
/**
 * Returned by the interceptor as a result of call interception.
 * @author lrosenberg
 *
 */
public class InterceptorResponse {
	/**
	 * The command.
	 * @author lrosenberg
	 *
	 */
	public static enum InterceptorCommand{
		/**
		 * Proceed with request.
		 */
		CONTINUE, 
		/**
		 * Abort the request, an exception should be attached to this response and thrown by the DiMe code. 
		 */
		ABORT,
		/**
		 * Don't further process the call and return a preset value instead. 
		 */
		RETURN,
		/**
		 * Overwrite the return value, but allow the call to be executed regularly. Only works in AFTER phases.
		 */
		OVERWRITE_RETURN_AND_CONTINUE,
		/**
		 * Force the method to fail. Only works if fail strategy is configured.
		 */
		ABORT_AND_FAIL,
		/**
		 * Return current call, but mark the service as failed.
		 */
		RETURN_AND_FAIL
	};
	
	/**
	 * An exception if the call should be aborted.
	 */
	private Exception exception;
	/**
	 * The return value which should be returned to the caller.
	 */
	private Object returnValue;
	/**
	 * The interception command.
	 */
	private InterceptorCommand command;
	/**
	 * Default reply variable. Helps save heap space ;-).
	 */
	public static final InterceptorResponse CONTINUE = new InterceptorResponse(InterceptorCommand.CONTINUE);

	/**
	 * Default reply variable. Helps save heap space ;-).
	 */
	public static final InterceptorResponse RETURN_AND_FAIL = new InterceptorResponse(InterceptorCommand.RETURN_AND_FAIL);

	public InterceptorResponse(InterceptorCommand aCommand){
		command = aCommand;
	}
	
	public InterceptorCommand getCommand(){
		return command;
	}
	
	public String toString(){
		return "InterceptionResponse "+getCommand();
	}

	
	public Exception getException(){
		return exception;
	}
	
	public Object getReturnValue(){
		return returnValue;
	}
	
	/**
	 * Factory method to create a ready to use response. This response signalizes that the execution should be immediately stopped and a return value returned to the caller.
	 * @param returnValue return value which should be returned to the caller.
	 * @return the ready-to-use InterceptorResponse object.
	 */
	public static final InterceptorResponse returnNow(Object returnValue){
		InterceptorResponse response = new InterceptorResponse(InterceptorCommand.RETURN);
		response.returnValue = returnValue;
		return response;
	}
	
	/**
	 * Factory method to create a ready to use response. This response signalizes that the execution should be continued and a return value returned to the caller later.
	 * This is useful if you want to give other interceptors a chance to interact with the new return value (logging, debug etc). 
	 * @param returnValue return value which should be returned to the caller.
	 * @return the ready-to-use InterceptorResponse object.
	 */
	public static final InterceptorResponse returnLater(Object returnValue){
		InterceptorResponse response = new InterceptorResponse(InterceptorCommand.OVERWRITE_RETURN_AND_CONTINUE);
		response.returnValue = returnValue;
		return response;
	}

	/**
	 * Factory method to create a ready to use response. Aborts immediately and throws the exception.
	 * @param anException exception to be thrown on the client side.
	 * @return the ready-to-use InterceptorResponse object.
	 */
	public static final InterceptorResponse abortNow(Exception anException){
		InterceptorResponse response = new InterceptorResponse(InterceptorCommand.ABORT);
		response.exception = anException;
		return response;
	}
}
