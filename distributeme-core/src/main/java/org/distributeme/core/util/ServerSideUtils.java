package org.distributeme.core.util;

import org.distributeme.core.AbstractCallContext;
import org.distributeme.core.routing.Constants;

import java.security.Permission;
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

	public static void setSecurityManagerIfRequired(){
		if (shouldSecurityManagerBeSet()){
			if (System.getSecurityManager()==null)
				// We allow all operations.
				System.setSecurityManager(new SecurityManager(){
					public void checkPermission(Permission perm) { }
				});
		}
	}

	/* TEST VISIBILITY */ static boolean shouldSecurityManagerBeSet(String jvmVersionString){
		if (jvmVersionString == null)
			return false;
		if (jvmVersionString.contains(".")){
			return Integer.parseInt(jvmVersionString.substring(0, jvmVersionString.indexOf('.')))<17;
		}
		return false;

	}

	private static final boolean shouldSecurityManagerBeSet(){
		return shouldSecurityManagerBeSet(System.getProperty("java.version"));
	}
}
