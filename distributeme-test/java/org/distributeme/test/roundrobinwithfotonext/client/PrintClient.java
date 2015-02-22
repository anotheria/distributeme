package org.distributeme.test.roundrobinwithfotonext.client;

import net.anotheria.util.NumberUtils;
import org.distributeme.core.ServiceLocator;
import org.distributeme.test.roundrobinwithfotonext.RoundRobinService;


public class PrintClient extends Client{
	public static void main(String a[]) throws Exception{
		RoundRobinService service = ServiceLocator.getRemote(RoundRobinService.class);

		long start = System.currentTimeMillis();
		int errors = 0 ; int replies = 0;
		int LIMIT = 100;
		for (int i = 0; i<LIMIT; i++){
			try{
				service.print("Hello Nr. "+NumberUtils.itoa(i, 3));
				replies++;
			}catch(Exception e){
				errors++;
			}
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("Tries "+LIMIT+", errors: "+errors+", replies: "+replies+", time: "+(end-start)+" ms");
		//now print results
		service.printResults();
		service.printResults();
		service.printResults();
		service.printResults();

	}

}
