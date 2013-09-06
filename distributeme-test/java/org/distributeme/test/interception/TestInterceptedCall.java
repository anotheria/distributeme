package org.distributeme.test.interception;

import org.distributeme.core.ServiceLocator;

public class TestInterceptedCall {
	
	public static void main(String[] args) {
		System.out.println("Remember to use ./startWithInterceptors.sh "+TestInterceptedCall.class.getName()+" ");
		TestService testService = ServiceLocator.getRemote(TestService.class);
		long duration = 3000;
		try{
			duration = Integer.parseInt(args[0]);
		}catch(Exception ignored){}
		testService.sleepingCall(duration);
		System.out.println("Call finished");
	}
}
