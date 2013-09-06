package org.distributeme.test.interception;

import java.util.HashMap;

import org.distributeme.core.ServiceLocator;

public class TestClient {
	/**
	 * 'Official' instance.
	 */
	private static TestService testService;
	/**
	 * Local instance for testing purposes.
	 */
	private static TestServiceImpl localInstance = new TestServiceImpl();
	
	public static void main(String[] args) {
		System.out.println("Remember to use startWithInterceptors.sh");
		testService = ServiceLocator.getRemote(TestService.class);
//		System.out.println("========================================================================");
//		testReturnValueOverride();
//		System.out.println("========================================================================");
//		testReturnInterceptedInClient();
//		System.out.println("========================================================================");
//		testCallByReference();
//		System.out.println("========================================================================");
//		testSumOverride();
//		System.out.println("========================================================================");
//		testParameterInspection();
		System.out.println("========================================================================");
		testPiggybacking();
	}
	
	private static void testPiggybacking(){
		System.out.println("Passing some parameter map");
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("Client says" , "Hi");
		System.out.println("--------------");
		
		HashMap<String, String> p2 = (HashMap<String, String>)parameters.clone();
		System.out.println("Parameter map is "+p2);
		localInstance.callByValue(p2);
		System.out.println("After executing locally map is: "+p2);
		System.out.println("--------------");
		
		System.out.println("Calling same method remotely.");
		testService.callByValue(parameters);
		System.out.println(" --- now parameters are: "+parameters);
		System.out.println("--------------");
		
		System.out.println("Calling same method via intercepted method.");
		testService.callByReference(parameters);
		System.out.println(" --- now parameters are: "+parameters);
		
	}
	
	//this shows how parameter inspection works, call number 42 can be interceptred.
	private static void testParameterInspection(){
		for (int i=0; i<50; i++)
			testService.sum(i, i);
	}

	private static void testSumOverride(){
		int a = 10; int b = 15;
		System.out.println("Testing sum methods, parameters are "+a+" and "+b+", expected result is: "+(a+b));
		System.out.println("Calling sum expecting "+localInstance.sum(a, b));
		System.out.println("Result is "+testService.sum(a, b));
		System.out.println();
		System.out.println("Calling modifiedSum expecting "+localInstance.modifiedSum(a, b));
		System.out.println("Result is "+testService.modifiedSum(a, b));
		System.out.println();
		System.out.println("Calling modifiedSumParameters expecting "+localInstance.modifiedSumParameters(a, b));
		System.out.println("Result is "+testService.modifiedSumParameters(a, b));
	}
	
	private static void testReturnValueOverride(){
		System.out.println("Calling server, expecting answer: \""+localInstance.returnString()+"\"");
		System.out.println("Server returned: "+testService.returnString());
	}

	private static void testReturnInterceptedInClient(){
		System.out.println("Calling server, expecting answer: \""+localInstance.returnStringCaughtInClient()+"\"");
		System.out.println("Server returned: "+testService.returnStringCaughtInClient());
	}
}
