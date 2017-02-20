package org.distributeme.core.util;

import org.distributeme.core.AbstractCallContext;
import org.distributeme.core.routing.Constants;

import java.util.Map;

/**
 * This utility helps server side to find out more information about what is happening.
 *
 * @author lrosenberg
 * @since 23.09.15 14:29
 * @version $Id: $Id
 */
public class ServerSideUtils {
	/**
	 * Returns true if current call is failover.
	 *
	 * @return a boolean.
	 */
	public static final boolean isFailoverCall(){
		Map transportableCallContext = AbstractCallContext.getCurrentTransportableCallContext();
		if (transportableCallContext == null){
			return false;
		}

		//some routers add callCount:
		Integer callCount = (Integer)transportableCallContext.get(Constants.ATT_CALL_COUNT);
		if (callCount != null && callCount > 0)
			return true;


		if (_isBlacklisted(transportableCallContext))
			return true;

		//further possibilites


		return false;
	}

	/**
	 * <p>isBlacklisted.</p>
	 *
	 * @return a boolean.
	 */
	public static final boolean isBlacklisted() {
		Map transportableCallContext = AbstractCallContext.getCurrentTransportableCallContext();
		if (transportableCallContext == null) {
			return false;
		}

		return _isBlacklisted(transportableCallContext);
	}



	private static final boolean _isBlacklisted(Map transportableCallContext){
		Boolean blacklistedFlag = (Boolean)transportableCallContext.get(Constants.ATT_BLACKLISTED);
		return blacklistedFlag != null && blacklistedFlag;
	}
}
