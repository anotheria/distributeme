package org.distributeme.test.echo;

import org.distributeme.core.ServiceLocator;

import java.util.HashMap;


public class TestRemoteClient {
	public static void main(String a[]) throws Exception{
		
		EchoService service = ServiceLocator.getRemote(EchoService.class);
		
		service.printHello();
		System.out.println("sent printHello");
		
		Echo echo = new Echo();
		System.out.println("sending echo: "+echo);
		echo = service.echo(echo);
		System.out.println("received echo: "+echo);
		
		long limit = 1000;
		long start = System.nanoTime();
		for (int i=0; i<limit; i++)
			echo = service.echo(echo);
		long end = System.nanoTime();
		double duration = ((double)(end-start)) / 1000 / 1000;
		System.out.println("Sent "+limit+" requests in "+duration);
		System.out.println("avg req dur: "+(duration/limit)+" ms.");
		
		String msg = "bla";
		try{
			
			service.throwException(msg);
		}catch(EchoServiceException e){
			System.out.println("Expected exception "+msg+", got: "+e.getMessage());
		}
		
		//and now the very special test.
		System.out.println("=== TESTING testCallByRef ===");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("CLIENTSIDE", "hello from client");
		System.out.println("Client side parameters.... "+params);
		service.testCallByRef(params);
		System.out.println("Client side parameters after call "+params);

		//and now the very special test.
		System.out.println("=== TESTING testCallByRefWithInterceptors ===");
		params = new HashMap<String, String>();
		params.put("CLIENTSIDE", "hello from client");
		System.out.println("Client side parameters.... "+params);
		service.testCallByRefWithInterceptors(params);
		System.out.println("Client side parameters after call "+params);
		
	}
}
