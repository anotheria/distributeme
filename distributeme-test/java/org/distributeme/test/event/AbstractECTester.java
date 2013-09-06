package org.distributeme.test.event;

import java.util.concurrent.atomic.AtomicInteger;

public class AbstractECTester {
	private static final AtomicInteger instanceCounter = new AtomicInteger(0);
	private int myNumber = instanceCounter.incrementAndGet();
	
	protected void out(Object o){
		System.out.println("["+(getClass().getSimpleName()) +myNumber+"] "+o);
	}
	
	protected int getInstanceNumber(){
		return myNumber;
	}
}
