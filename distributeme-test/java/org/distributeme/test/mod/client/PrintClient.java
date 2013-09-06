package org.distributeme.test.mod.client;

import net.anotheria.util.IdCodeGenerator;

import org.distributeme.core.ServiceLocator;
import org.distributeme.test.mod.ModedService;

public class PrintClient{
	public static void main(String a[]) throws Exception{
		ModedService service = ServiceLocator.getRemote(ModedService.class);
		
		for (int i = 0; i<10; i++){
			String toPrint = IdCodeGenerator.generateCode(10);
			System.out.println("Will ask code to print "+toPrint);
			System.out.println("You should see the same string printed out by the router in the line below and on the server side...");
			service.printString(toPrint);
		}
	
	}
}
