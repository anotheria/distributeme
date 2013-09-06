package org.distributeme.goldminer;

import org.distributeme.core.ServiceLocator;
import org.distributeme.core.asynch.AsynchStub;
import org.distributeme.core.asynch.CallTimeoutedException;

public class AsynchGoldSearcher {
	public static void main(String[] args) throws Exception{
		GoldMinerService service = ServiceLocator.getAsynchRemote(GoldMinerService.class);
		long start = System.currentTimeMillis();
		int searchTime = 60;
		System.out.println("Searching for gold for "+searchTime+" seconds");
		long endTime = start + 1000L*searchTime;
		long now;
		int foundGold = 0;
		int attempts = 0;
		while ((now = System.currentTimeMillis())<endTime){
			System.out.println("Attempt "+(++attempts));
			try{
				if (service.searchForGold()){
					foundGold++;
					System.out.println("Found gold!");
				}else{
					System.out.println("Nothing here...");
				}
			}catch(CallTimeoutedException timeoutException){
				System.out.println("too deep, aborted...");
			}
		}
		System.out.println("Found "+foundGold+" gold in "+attempts+" attempts and "+(now - start)/1000+" seconds.");
		((AsynchStub)service).shutdown();
	}
}
