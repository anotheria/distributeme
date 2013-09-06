package org.distributeme.test.inheritance;

import org.distributeme.core.ServiceLocator;

public class TestClient {
	
	private static long time = System.currentTimeMillis();
	
	public static void main(String a[]){
		testA();
		testB();
		testBase();
	}
	
	private static void testA(){
		System.out.println("TEST A");
		test(ServiceLocator.getRemote(AService.class));
		
	}
	private static void testB(){
		System.out.println("TEST B");
		test(ServiceLocator.getRemote(BService.class));
	}
	
	private static void testBase(){
		System.out.println("TEST Base");
		test(ServiceLocator.getRemote(BaseService.class));
	}

	private static void test(BaseService service){
		System.out.println("Testing with "+service);
		System.out.println(time+" --> "+service.echo(time));
	}
}
  