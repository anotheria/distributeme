package org.distributeme.goldminer;

import org.distributeme.core.ServiceLocator;

public class SynchGoldWasher {
	public static void main(String[] args) throws Exception{
		GoldMinerService service = ServiceLocator.getRemote(GoldMinerService.class);
		int washTime = 10;
		System.out.println("Washing gold for "+washTime+" seconds.");
		long start = System.currentTimeMillis();
		int washed = service.washGold(washTime*1000L);
		long duration = System.currentTimeMillis() - start;
		System.out.println("Washed "+washed+" gold clumps in "+duration+" ms.");
		
	}
}
