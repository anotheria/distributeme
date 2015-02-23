package org.distributeme.test.interception.interceptor;

public class LogOutBeforeCallInterceptor extends LogOutEverythingInterceptor{
	@Override
	protected String getMessage() {
		return "LOG ENTERING";
	}

}
