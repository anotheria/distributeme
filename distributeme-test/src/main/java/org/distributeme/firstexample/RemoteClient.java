package org.distributeme.firstexample;

import org.distributeme.core.ServiceLocator;

public class RemoteClient {
	public static void main(String[] args) {
		FirstService service = ServiceLocator.getRemote(FirstService.class);
		System.out.println("client: Service replied "+service.greet("Hello from client"));
	}
}
 