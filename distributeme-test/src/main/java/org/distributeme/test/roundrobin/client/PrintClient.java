package org.distributeme.test.roundrobin.client;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.util.NumberUtils;
import org.distributeme.test.roundrobin.RoundRobinService;

public class PrintClient extends Client{
	public static void main(String a[]) throws Exception{
		RoundRobinService service = MetaFactory.create(RoundRobinService.class, Extension.REMOTE);

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
	
	}

}
