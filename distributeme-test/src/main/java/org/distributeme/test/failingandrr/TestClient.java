package org.distributeme.test.failingandrr;

import org.distributeme.core.ServiceLocator;

public class TestClient {
	public static void main(String[] args) {
		TestService service = ServiceLocator.getRemote(TestService.class);
		long requests, errors;
		requests = errors = 0;
		long a = 0;
		while (true){
			try{
				requests++;
				a = service.echo(a+1);
			}catch(Exception e){
				errors++;
				e.printStackTrace();
			}
			System.out.println("REQ "+requests+", ERR: "+errors);
		}
	}
}
