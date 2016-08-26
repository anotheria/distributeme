package org.distributeme.core.asynch;
/**
 * This interface describes objects that are submitted to the asynch call methods.
 * The asynch stub calls either subject or error on each of the submitted callback handlers.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public interface CallBackHandler {
	/**
	 * Called upon successful execution of a method. If the called method was a void method the parameter o will be null.
	 *
	 * @param o null or function call result.
	 */
	void success(Object o);
	
	/**
	 * Called upon erroneous execution of a method.
	 *
	 * @param e exception that occured in the method.
	 */
	void error(Exception e);


}
