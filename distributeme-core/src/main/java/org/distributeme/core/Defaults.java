package org.distributeme.core;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.ConfigureMe;
import org.distributeme.core.concurrencycontrol.ConcurrencyControlStrategy;
import org.distributeme.core.concurrencycontrol.UnlimitedConcurrencyStrategy;
import org.distributeme.core.failing.DefaultFailingStrategy;
import org.distributeme.core.failing.FailingStrategy;

/**
 * This class provides methods for runtime overriding of the default behaviour.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class Defaults {
	/**
	 * The config.
	 */
	private static DefaultsConfiguration config;
	static{
		config = new DefaultsConfiguration();
		try{
			ConfigurationManager.INSTANCE.configure(config);
		}catch(Exception ignored){
			//falling back to defaults
		}
	}
	
	/**
	 * Returns the default failing strategy.
	 *
	 * @return a {@link org.distributeme.core.failing.FailingStrategy} object.
	 */
	public static final FailingStrategy getDefaultFailingStrategy(){
		return new DefaultFailingStrategy();
	}
	
	/**
	 * <p>getDefaultConcurrencyControlStrategy.</p>
	 *
	 * @return a {@link org.distributeme.core.concurrencycontrol.ConcurrencyControlStrategy} object.
	 */
	public static final ConcurrencyControlStrategy getDefaultConcurrencyControlStrategy(){
		return new UnlimitedConcurrencyStrategy(); 
	}
	
	/**
	 * <p>getDefaultAsynchCallTimeout.</p>
	 *
	 * @return a long.
	 */
	public static final long getDefaultAsynchCallTimeout(){
		return config.getDefaultAsynchCallTimeout();
	}
	
	/**
	 * <p>getAsynchExecutorPoolSize.</p>
	 *
	 * @return a int.
	 */
	public static final int getAsynchExecutorPoolSize(){
		return config.getAsynchExecutorPoolSize();
	}
	
	/**
	 * Inner configuration holder class.
	 * @author lrosenberg
	 *
	 */
	@ConfigureMe(allfields=true, name="distributeme")
	public static class DefaultsConfiguration{
		/**
		 * The name of the default failing strategy.
		 */
		private String defaultFailingStrategyClassName;
		/**
		 * The name of the default concurrency control strategy. If no name specified, org.distributeme.core.concurrencycontrol.UnlimitedConcurrencyStrategy is used.
		 */
		private String defaultConcurrencyControlStrategyClassName;
		
		/**
		 * Default timeout for asynchronous calls.
		 */
		private long defaultAsynchCallTimeout = 10000L;

		/**
		 * Default number of thread in the asynch executor, which are used in asynch stubs.
		 */
		private int asynchExecutorPoolSize = 50;

		public String getDefaultFailingStrategyClassName() {
			return defaultFailingStrategyClassName;
		}

		public void setDefaultFailingStrategyClassName(
				String defaultFailingStrategyClassName) {
			this.defaultFailingStrategyClassName = defaultFailingStrategyClassName;
		}

		public String getDefaultConcurrencyControlStrategyClassName() {
			return defaultConcurrencyControlStrategyClassName;
		}

		public void setDefaultConcurrencyControlStrategyClassName(
				String defaultConcurrencyControlStrategyClassName) {
			this.defaultConcurrencyControlStrategyClassName = defaultConcurrencyControlStrategyClassName;
		}

		public long getDefaultAsynchCallTimeout() {
			return defaultAsynchCallTimeout;
		}

		public void setDefaultAsynchCallTimeout(long defaultAsynchCallTimeout) {
			this.defaultAsynchCallTimeout = defaultAsynchCallTimeout;
		}

		public int getAsynchExecutorPoolSize() {
			return asynchExecutorPoolSize;
		}

		public void setAsynchExecutorPoolSize(int asynchExecutorPoolSize) {
			this.asynchExecutorPoolSize = asynchExecutorPoolSize;
		}
	}
}
