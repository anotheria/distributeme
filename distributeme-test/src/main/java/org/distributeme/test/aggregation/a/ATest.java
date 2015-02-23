package org.distributeme.test.aggregation.a;

import org.distributeme.core.ServiceLocator;

public class ATest {
	public static void main(String[] args) {
		System.out.println("--- CALLING ---");
		ServiceLocator.getRemote(AService.class).aMethod();
		System.out.println("--- DONE ---");
	}
}
