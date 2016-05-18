package org.distributeme.test.interception;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;

import java.util.Map;

@DistributeMe
public interface TestService extends Service{
	void callByValue(Map<String,String> parameters);

	void callByReference(Map<String,String> parameters);
	
	int sum(int a, int b);
	
	int modifiedSum(int a, int b);
	
	int modifiedSumParameters(int a, int b);
	
	/**
	 * This method demonstrates modification of the return value by various interceptors.
	 * @return
	 */
	String returnString();
	
	/**
	 * This method demonstrates how an interceptor in the stub can prevent the call from being sent to server at all.
	 * @return
	 */
	String returnStringCaughtInClient();
	
	/**
	 * This method sleeps long....
	 * @param timeToSleep
	 */
	void sleepingCall(long timeToSleep);
}
