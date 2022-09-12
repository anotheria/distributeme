package org.distributeme.firstexample;

/**
 * Example client that just makes a local call to the service impl.
 */
public class LocalClient {
	public static void main(String[] args) {
		FirstService service = new FirstServiceImpl();
		System.out.println("client: Service replied "+service.greet("Hello from client"));
	}
}
 