package org.distributeme.test.concurrencycontrol;

import java.lang.reflect.Method;


public class TestClientSideCalls {

	public static void main(String[] args) throws Exception{
		int alimit = 100;
		try{
			alimit = Integer.parseInt(args[0]);
		}catch(Exception ignored){
		};

		Method m = TestService.class.getMethod("clientSideLimited", long.class);
		TestRun.test(alimit, m);
		
	}
}
