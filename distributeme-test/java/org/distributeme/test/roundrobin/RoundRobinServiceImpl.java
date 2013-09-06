package org.distributeme.test.roundrobin;

import net.anotheria.util.IdCodeGenerator;

public class RoundRobinServiceImpl implements RoundRobinService{

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
	}
		
	
}
