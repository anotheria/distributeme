package org.distributeme.test.fail;

import org.distributeme.core.ServiceLocator;

/**
 * Tests failover and return in 10 seconds strategy.
 */
public class FailoverAndReturnInTenSeconds {
	public static void main(String[] args) {
		FailableService service = ServiceLocator.getRemote(FailableService.class);
		int counter=0,errorcounter = 0;
		while(true){
			counter++;
			try{
				if (counter/10000*10000==counter)
					System.out.println("Error "+errorcounter+", requests "+counter);
				service.failoverPrintAndStayFoTenSeconds("Hello number "+counter);
			}catch(Exception e){
				errorcounter++;
				System.out.println("Error "+errorcounter+", requests "+counter+" - "+e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
