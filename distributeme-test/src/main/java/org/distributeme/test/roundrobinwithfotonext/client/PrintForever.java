package org.distributeme.test.roundrobinwithfotonext.client;

import net.anotheria.moskito.webui.embedded.StartMoSKitoInspectBackendForRemote;
import org.distributeme.core.ServiceLocator;
import org.distributeme.test.roundrobinwithfotonext.RoundRobinService;


public class PrintForever extends Client{
	public static void main(String a[]) throws Exception{

		StartMoSKitoInspectBackendForRemote.startMoSKitoInspectBackend();

		RoundRobinService service = ServiceLocator.getRemote(RoundRobinService.class);

		long start = System.currentTimeMillis();
		int errors = 0 ; int replies = 0;
		long counter = 0;
		while(true){
			try{
				service.print("Hello Nr. "+(counter++));
				replies++;
			}catch(Exception e){
				errors++;
			}
			if (replies/1000*1000==replies) {
				long end = System.currentTimeMillis();
				System.out.println("errors: " + errors + ", replies: " + replies + ", time: " + (end - start) + " ms");
				start = System.currentTimeMillis();

			}
		}
		
	}

}
