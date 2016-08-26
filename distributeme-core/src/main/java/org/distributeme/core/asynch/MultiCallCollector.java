package org.distributeme.core.asynch;

import org.distributeme.core.Defaults;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A helper class to synchronously execute multiple asynchronous calls and collect results.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class MultiCallCollector {
	/**
	 * Internal latch which counts down executed calls.
	 */
	private final CountDownLatch latch;
	/**
	 * Subhandlers for calls.
	 */
	private ConcurrentMap<String, SubCallBackHandler> handlers;
	/**
	 * Number of total calls that should be executed here.
	 */
	private int numberOfCalls;
	
	/**
	 * <p>Constructor for MultiCallCollector.</p>
	 *
	 * @param aNumberOfCalls a int.
	 */
	public MultiCallCollector(int aNumberOfCalls){
		numberOfCalls = aNumberOfCalls;
		latch = new CountDownLatch(numberOfCalls);
		handlers = new ConcurrentHashMap<String, MultiCallCollector.SubCallBackHandler>();
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
	
	/**
	 * <p>createSubCallHandler.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 * @return a {@link org.distributeme.core.asynch.CallBackHandler} object.
	 */
	public CallBackHandler createSubCallHandler(String id){
		if (handlers.size()>=numberOfCalls)
			throw new IllegalStateException("There are already "+numberOfCalls+" calls running");
		SubCallBackHandler newHandler = new SubCallBackHandler(this);
		if (handlers.putIfAbsent(id, newHandler)!=null){
			throw new IllegalArgumentException("Call with id "+id+" is already started!");
		}
		return newHandler;
	}
	
	/**
	 * <p>notifySubCallFinished.</p>
	 */
	protected void notifySubCallFinished(){
		latch.countDown();
	}

	
	/**
	 * An instance of this class is used as a call handler for each subcall.
	 * @author lrosenberg
	 *
	 */
	public static class SubCallBackHandler implements CallBackHandler{
		/**
		 * Link to the collector.
		 */
		private MultiCallCollector parent;
		/**
		 * Return value.
		 */
		private volatile Object returnValue;
		/**
		 * Exception if thrown.
		 */
		private volatile Exception returnException;
		/**
		 * Creates a new subcallbackhandler.
		 * @param aParent
		 */
		public SubCallBackHandler(MultiCallCollector aParent) {
			parent = aParent;
		}

		@Override
		public void success(Object o) {
			returnValue = o;
			parent.notifySubCallFinished();
		}

		@Override
		public void error(Exception e) {
			returnException = e;
			parent.notifySubCallFinished();
		}
	}
	/**
	 * Returns the handler for a call id.
	 * @param id
	 * @return
	 */
	private SubCallBackHandler getHandler(String id){
		return handlers.get(id);
	}
	
	/**
	 * <p>isError.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean isError(String id){
		return isFinished() && getHandler(id).returnException!=null;
	}
	
	/**
	 * <p>isSuccess.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean isSuccess(String id){
		return isFinished() && getHandler(id).returnException==null;
	}
	
	/**
	 * <p>getReturnValue.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getReturnValue(String id){
		return getHandler(id).returnValue;
	}
	
	/**
	 * <p>getReturnException.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 * @return a {@link java.lang.Exception} object.
	 */
	public Exception getReturnException(String id){
		return getHandler(id).returnException;
	}

}
