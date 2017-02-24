package org.distributeme.test.blacklisting;

import org.distributeme.core.ServiceLocator;


public class BlacklistingTestServiceClient {

	public static void main(String[] args) throws Exception{
		int counter = 0;
		BlacklistingTestService blacklistingTestService = ServiceLocator.getRemote(BlacklistingTestService.class);


		int success = 0, error = 0;
		while (true) {
			counter++;
			Thread.sleep(250);
			try {
				blacklistingTestService.doSomeThing(counter);
				System.out.println("success ");
				success++;
			} catch (Exception exception) {
				System.out.println("exception "+ exception.getMessage());
//				exception.printStackTrace();


				error++;
			}

			if (counter/100*100==counter){
				System.out.println("Counter: "+counter+" success: "+success+" error: "+error);
			}

		}
	}
}
