package org.distributeme.test.mod.client;

import org.distributeme.core.ServiceLocator;
import org.distributeme.test.mod.ModedService;

public class EchoClient{
	public static void main(String a[]) throws Exception{
		ModedService service = ServiceLocator.getRemote(ModedService.class);

		long start = System.currentTimeMillis();
		int errors = 0 ;
		int mistakes = 0;
		int LIMIT = 100;
		for (int i = 0; i<LIMIT; i++){
			try{
				long reply = service.modEcho(i);
				if (!(reply==(long)i))
					mistakes++;
			}catch(Exception e){
				e.printStackTrace();
				errors++;
			}
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("Tries "+LIMIT+", errors: "+errors+", wrong answers: "+mistakes+", time: "+(end-start)+" ms");
	
	}

}
