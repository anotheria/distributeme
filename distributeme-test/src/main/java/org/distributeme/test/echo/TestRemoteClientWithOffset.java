package org.distributeme.test.echo;

import org.distributeme.core.ServiceLocator;


public class TestRemoteClientWithOffset {
	public static void main(String a[]) throws Exception{
		
		EchoService service = ServiceLocator.getRemote(EchoService.class);
		
		System.out.println("Sleeping 3 s");
		Thread.currentThread().sleep(3000);
		
		service.printHello();
		System.out.println("sent printHello");
		
		Echo echo = new Echo();
		System.out.println("sending echo: "+echo);
		echo = service.echo(echo);
		System.out.println("received echo: "+echo);
		
		long limit = 10000;
		long start = System.nanoTime();
		for (int i=0; i<10000; i++)
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

	}
}
