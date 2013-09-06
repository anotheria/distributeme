package org.distributeme.firstexample;

public class LocalClient {
	public static void main(String[] args) {
		FirstService service = new FirstServiceImpl();
		System.out.println("client: Service replied "+service.greet("Hello from client"));
	}
}
 