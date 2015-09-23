package org.distributeme.core.routing;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 21.09.15 22:27
 */
public class Constants {
	public static final String ROUTING_LOGGER_NAME = "DistributeMeRouting";

	/**
	 * This attribute defines the call count in the current call. If the call count is > 1, this means, that this call is a failover call.
	 */
	public static final String ATT_CALL_COUNT = "routing.callCount";

	/**
	 * This attribute is set when the call has been re-routed due to blacklisting.
	 */
	public static final String ATT_BLACKLISTED = "routing.blacklisted";

}
