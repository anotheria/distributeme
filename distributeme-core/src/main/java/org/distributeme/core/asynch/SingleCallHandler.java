package org.distributeme.core.asynch;

import org.distributeme.core.Defaults;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
/**
 * This is a callhandler implementation which is used in generated asynchronous stubs.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class SingleCallHandler implements CallBackHandler{
	/**
	 * The return value of the call.
	 */
	private Object returnValue;
	/**
	 * The exception of the call.
	 */
	private Exception returnException;
	/**
	 * A latch which is used to synchronize the sync and asynch calls which each other.
	 */
	private final CountDownLatch latch;
	
	/**
	 * <p>Constructor for SingleCallHandler.</p>
	 */
	public SingleCallHandler(){
		latch = new CountDownLatch(1);
	}
	
	/**
	 * <p>waitForResults.</p>
	 *
	 * @param timeout a long.
	 * @throws java.lang.InterruptedException if any.
	 */
	public void waitForResults(long timeout) throws InterruptedException{
		latch.await(timeout, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * <p>waitForResults.</p>
	 *
	 * @throws java.lang.InterruptedException if any.
	 */
	public void waitForResults() throws InterruptedException{
		waitForResults(Defaults.getDefaultAsynchCallTimeout());
	}
	
	/**
	 * <p>isFinished.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isFinished(){
		return latch.getCount() == 0;
	}
	
	private void markFinished(){
		latch.countDown();
	}
	
	/** {@inheritDoc} */
	public void success(Object o){
		returnValue = o;
		markFinished();
	}
	
	/** {@inheritDoc} */
	public void error(Exception e){
		returnException = e;
		markFinished();
	}
	
	/**
	 * <p>isError.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isError(){
		return isFinished() && returnException!=null;
	}
	
	/**
	 * <p>isSuccess.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSuccess(){
		return isFinished() && returnException==null;
	}
	
	/**
	 * <p>Getter for the field <code>returnValue</code>.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getReturnValue(){
		return returnValue;
	}
	
	/**
	 * <p>Getter for the field <code>returnException</code>.</p>
	 *
	 * @return a {@link java.lang.Exception} object.
	 */
	public Exception getReturnException(){
		return returnException;
	}
	
}
