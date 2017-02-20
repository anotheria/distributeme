package org.distributeme.core.interceptor.availabilitytesting;
/**
 * Constants for availability testing.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class Constants {
	/**
	 * Name of the service id system property.
	 */
	public static final String PROPERTY_SERVICE_ID = "availabilityTestingServiceId";
	/**
	 * Name of the property that specifies slowdowntime in milliseconds.
	 */
	public static final String PROPERTY_SLOWDOWN_TIME_IN_MILLIS = "availabilityTestingSlowDownTimeInMillis";
	/**
	 * Name of the property that specifies flip chance.
	 */
	public static final String PROPERTY_FLIP_CHANCE_IN_PERCENT = "availabilityTestingFlipChanceInPercent";
	/**
	 * Default slow down time.
	 */
	public static final long DEFAULT_SLOW_DOWN_TIME = 10000;
}
