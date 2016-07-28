package org.distributeme.core.interceptor.availabilitytesting;

import net.anotheria.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility to handle availabilitytesting properties..
 * @author lrosenberg
 *
 */
public class PropertyInterceptorUtil {
	/**
	 * Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(PropertyInterceptorUtil.class);
	/**
	 * Configured service ids.
	 */
	private static List<String> serviceIds = extractServiceIds();

	public static List<String> extractServiceIds() {
		List<String> propertyValues = new ArrayList<>();
		String pValue = System.getProperty(Constants.PROPERTY_SERVICE_ID);
		if (pValue!=null && !pValue.isEmpty()){
			String tokens[] = StringUtils.tokenize(pValue, ',');
            Collections.addAll(propertyValues, tokens);
		}
		return propertyValues;
	}
	
	public static final long getSlowDownTime(){
		try{
			String slowDownTimeAsString = System.getProperty(Constants.PROPERTY_SLOWDOWN_TIME_IN_MILLIS);
			if (slowDownTimeAsString!=null && !slowDownTimeAsString.isEmpty()){
				return Long.parseLong(slowDownTimeAsString);
			}
		}catch(Exception e){
			log.warn("Couldn't parse slowdowntime, returning default value "+Constants.DEFAULT_SLOW_DOWN_TIME, e);
		}
		return Constants.DEFAULT_SLOW_DOWN_TIME;
	}

	public static boolean isServiceIdConfiguredByProperty(String serviceId){
		return serviceIds.contains(serviceId) || serviceIds.contains("*");
	}
	
	/**
	 * Returns true if the service should be flipped.
	 * @return
	 */
	public static boolean flip(){
		return false;
	}
	
}
