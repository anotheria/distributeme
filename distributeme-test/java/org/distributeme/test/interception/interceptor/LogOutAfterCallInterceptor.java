package org.distributeme.test.interception.interceptor;

public class LogOutAfterCallInterceptor extends LogOutEverythingInterceptor{

	@Override
	protected String getMessage() {
		return "LOG LEAVING ";
	}

}
