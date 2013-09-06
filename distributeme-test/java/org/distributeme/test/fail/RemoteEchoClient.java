package org.distributeme.test.fail;

import org.distributeme.core.ServiceLocator;

public class RemoteEchoClient {
	public static void main(String a[]){
		int limit = 100;
		System.out.println("Executing "+limit+" calls to target service");
		FailableService service = ServiceLocator.getRemote(FailableService.class);
		
		int success = 0, failures = 0, errors = 0;
		
		for (int i=0; i<limit; i++){
			long time = System.currentTimeMillis();
			try{
				long reply = service.retryEcho(time);
				if (reply==time)
					success++;
				else
					errors++;
			}catch(Exception e){
				System.out.println(e.getMessage());
				failures++;
			}
			
		}
		
		System.out.println("successes: "+success+", failures: "+failures+", errors: "+errors);
		
	}
}
