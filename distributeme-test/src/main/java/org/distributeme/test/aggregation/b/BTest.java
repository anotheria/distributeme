package org.distributeme.test.aggregation.b;

import org.distributeme.core.ServiceLocator;

public class BTest {
	public static void main(String[] args) {
		System.out.println("--- CALLING ---");
		ServiceLocator.getRemote(BService.class).bMethod();
		System.out.println("--- DONE ---");
	}
}
