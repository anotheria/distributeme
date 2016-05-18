package org.distributeme.core.asynch;

import org.distributeme.core.Defaults;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
/**
 * This is a callhandler implementation which is used in generated asynchronous stubs.
 * @author lrosenberg
 *
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
	
	public SingleCallHandler(){
		latch = new CountDownLatch(1);
	}
	
	public void waitForResults(long timeout) throws InterruptedException{
		latch.await(timeout, TimeUnit.MILLISECONDS);
	}
	
	public void waitForResults() throws InterruptedException{
		waitForResults(Defaults.getDefaultAsynchCallTimeout());
	}
	
	public boolean isFinished(){
		return latch.getCount() == 0;
	}
	
	private void markFinished(){
		latch.countDown();
	}
	
	public void success(Object o){
		returnValue = o;
		markFinished();
	}
	
	public void error(Exception e){
		returnException = e;
		markFinished();
	}
	
	public boolean isError(){
		return isFinished() && returnException!=null;
	}
	
	public boolean isSuccess(){
		return isFinished() && returnException==null;
	}
	
	public Object getReturnValue(){
		return returnValue;
	}
	
	public Exception getReturnException(){
		return returnException;
	}
	
}
