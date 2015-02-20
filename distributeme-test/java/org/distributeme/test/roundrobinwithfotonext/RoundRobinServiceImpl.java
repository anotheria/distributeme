package org.distributeme.test.roundrobinwithfotonext;


import net.anotheria.util.IdCodeGenerator;

import java.util.concurrent.atomic.AtomicLong;

public class RoundRobinServiceImpl implements RoundRobinService{

	private AtomicLong requestCounter = new AtomicLong(0);

	private final String randomId = IdCodeGenerator.generateCode(10);
	
	@Override
	public int add(int a, int b) {
		return a+b;
	}

	@Override
	public String getRandomId() {
		return randomId;
	}

	@Override
	public void print(String parameter) {
		System.out.println("Received: "+parameter);
		requestCounter.incrementAndGet();
	}

	@Override
	public void printResults() {
		System.out.println("Requests: "+requestCounter);
		requestCounter.set(0);
	}
}
