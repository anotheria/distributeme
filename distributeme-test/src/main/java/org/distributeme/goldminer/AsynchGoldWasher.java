package org.distributeme.goldminer;

import org.distributeme.core.ServiceLocator;
import org.distributeme.core.asynch.MultiCallCollector;
import org.distributeme.goldminer.generated.AsynchGoldMinerService;

public class AsynchGoldWasher {
	public static void main(String[] args) throws Exception{
		GoldMinerService service = ServiceLocator.getAsynchRemote(GoldMinerService.class);
		AsynchGoldMinerService asynchService = (AsynchGoldMinerService)service;
		int washTime = 10;
		int calls = 5;
		System.out.println("Washing gold for "+washTime+" seconds in "+calls+" calls.");
		long start = System.currentTimeMillis();
		MultiCallCollector collector = new MultiCallCollector(calls);
		for (int i=0; i<calls; i++){
			asynchService.asynchWashGold(washTime*1000L, collector.createSubCallHandler(""+i));
		}
		collector.waitForResults(11000);
		int washed = 0;
		for (int i=0; i<calls; i++){
			washed += (Integer)collector.getReturnValue(""+i);
		}
		
		long duration = System.currentTimeMillis() - start;
		System.out.println("Washed "+washed+" gold clumps in "+duration+" ms.");
		asynchService.shutdown();
		
	}
}
