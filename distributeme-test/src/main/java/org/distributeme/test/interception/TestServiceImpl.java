package org.distributeme.test.interception;

import net.anotheria.util.NumberUtils;

import java.util.Map;

public class TestServiceImpl implements TestService{

	@Override
	public void callByValue(Map<String, String> parameters) {
		parameters.put("Greeting from service", NumberUtils.makeISO8601TimestampString(System.currentTimeMillis()));
	}

	@Override
	public void callByReference(Map<String, String> parameters) {
		callByValue(parameters);
		
	}

	@Override
	public int sum(int a, int b) {
		return a+b;
	}

	@Override
	public int modifiedSum(int a, int b) {
		return sum(a,b);
	}

	@Override
	public int modifiedSumParameters(int a, int b) {
		return sum(a,b);
	}
	
	@Override public String returnString(){
		return "Hello from server";
	}

	@Override
	public String returnStringCaughtInClient() {
		return returnString();
	}

	@Override
	public void sleepingCall(long timeToSleep) {
		System.out.println("Starting to sleep ... ");
		try{
			Thread.sleep(timeToSleep);
		}catch(InterruptedException e){
			System.out.println("INTERRRRRRRRRUPTED");
			e.printStackTrace();
		}
		System.out.println("Sleeped well");
	}


}
