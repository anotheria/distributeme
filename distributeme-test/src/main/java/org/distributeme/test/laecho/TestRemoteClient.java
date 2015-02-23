package org.distributeme.test.laecho;

import org.distributeme.core.ServiceLocator;


public class TestRemoteClient {
	public static void main(String a[]) throws Exception{
		
		LifecycleAwareEchoService service = ServiceLocator.getRemote(LifecycleAwareEchoService.class);
		
		service.printHello();
		System.out.println("sent printHello");
		
		long echo = service.echo(System.currentTimeMillis());
		System.out.println("received echo: "+echo);
		
		System.out.println("Finished");
	}
}
