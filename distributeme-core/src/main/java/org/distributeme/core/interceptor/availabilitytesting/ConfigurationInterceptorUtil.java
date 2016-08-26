package org.distributeme.core.interceptor.availabilitytesting;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Utility to handle availabilitytesting configuration.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class ConfigurationInterceptorUtil {
	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(ConfigurationInterceptorUtil.class);

	/**
	 * Random for chance calculation.
	 */
	private static Random rnd = new Random(System.nanoTime());
	
	/**
	 * Config object.
	 */
	private static ConfigurationObject config;
	static{
		try{
			config = new ConfigurationObject();
			ConfigurationManager.INSTANCE.configure(config);
		}catch(IllegalArgumentException noConfigFound){
			log.warn("couldn't find configuration for "+ConfigurationInterceptorUtil.class.getSimpleName(), noConfigFound);
		}
	}
	
	
	/**
	 * <p>getSlowDownTime.</p>
	 *
	 * @return a long.
	 */
	public static final long getSlowDownTime(){
		return config.getSlowDownTimeInMillis();
	}
	/**
	 * Returns true if this call should be fliped. Fliping mean that an exceptional situation can occure but doesn't occure all the time.
	 *
	 * @return a boolean.
	 */
	public static final boolean flip(){
		if (config.getFlipChanceInPercent()==0)
			return false;
		if (config.getFlipChanceInPercent()==100)
			return true;
		return rnd.nextInt(100)<config.getFlipChanceInPercent();
		
	}

	/**
	 * <p>isServiceIdConfiguredByProperty.</p>
	 *
	 * @param serviceId a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public static boolean isServiceIdConfiguredByProperty(String serviceId){
		List<String> serviceIds = config.getServiceIdsList();
		return serviceIds.contains(serviceId) || serviceIds.contains("*");
	}

	/**
	 * Helper class that holds the config and is re/configured by distributeme.
	 * @author lrosenberg
	 *
	 */
	@ConfigureMe(name="availabilitytesting", allfields=true)
	public static class ConfigurationObject{
		/**
		 * Configured service ids array. This is an array, because json supports arrays natively. 
		 */
		private String[] serviceIds = new String[0];
		/**
		 * Slow down time.
		 */
		private long slowDownTimeInMillis = Constants.DEFAULT_SLOW_DOWN_TIME;
		/**
		 * Internal serviceIdsList.
		 */
		private List<String> serviceIdsList;
		
		/**
		 * Flip chance in percent, values 0-100 are acceptable.
		 */
		private int flipChanceInPercent;
		
		
		public String[] getServiceIds() {
			return serviceIds;
		}
		public void setServiceIds(String[] serviceIds) {
			this.serviceIds = serviceIds;
		}
		public long getSlowDownTimeInMillis() {
			return slowDownTimeInMillis;
		}
		public void setSlowDownTimeInMillis(long slowDownTimeInMillis) {
			this.slowDownTimeInMillis = slowDownTimeInMillis;
		}
		
		@Override public String toString(){
			return "service ids: "+getServiceIdsList()+", slowDownTime: "+slowDownTimeInMillis+", flipChance: "+flipChanceInPercent+"/100";
		}
		
		@AfterConfiguration public void setHelper(){
			if (serviceIds!=null)
				serviceIdsList=Collections.unmodifiableList(Arrays.asList(serviceIds));
			else
				serviceIdsList = (List<String>)Collections.EMPTY_LIST;
		}
		
		@AfterConfiguration public void debugOut(){
			System.out.println("availability testing interceptors reconfigured: "+this);
			log.debug("availability testing interceptors reconfigured: "+this);
		}
		public List<String> getServiceIdsList() {
			return serviceIdsList;
		}
		public int getFlipChanceInPercent() {
			return flipChanceInPercent;
		}
		public void setFlipChanceInPercent(int flipChanceInPercent) {
			if (flipChanceInPercent<0 || flipChanceInPercent>100)
				throw new IllegalArgumentException("flip chance must be between 0 and 100");
			this.flipChanceInPercent = flipChanceInPercent;
		}
	}
	
	/**
	 * <p>debugOutConfig.</p>
	 */
	public static void debugOutConfig() {
		System.out.println(config);
	}
}
