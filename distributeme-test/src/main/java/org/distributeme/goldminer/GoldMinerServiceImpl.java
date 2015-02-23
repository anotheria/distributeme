package org.distributeme.goldminer;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class GoldMinerServiceImpl implements GoldMinerService{

	private Random rnd = new Random(System.currentTimeMillis());
	private static final int MAX_SEARCH_SLEEP = 10;
	private static AtomicLong callCounter = new AtomicLong();
	
	@Override
	public boolean searchForGold() {
		long callNumber = callCounter.incrementAndGet();
		int r = rnd.nextInt(MAX_SEARCH_SLEEP)+1;
		System.out.println(callNumber+" - Searching for gold ("+r+").");
		try{
			Thread.sleep(1000L*r);
		}catch(InterruptedException ignored){}
		System.out.println(callNumber+" - Finished searching for gold ("+r+") - "+callNumber);
		return rnd.nextInt(r)==0;
	}

	@Override
	public int washGold(long duration) {
		long callNumber = callCounter.incrementAndGet();
		System.out.println(callNumber+" - Washing gold for "+(duration/1000L)+" seconds");
		try{
			Thread.sleep(duration);
		}catch(InterruptedException ignored){}
		System.out.println(callNumber+" - Finished washing gold");
		return (int)(duration/1000);
	}

}
