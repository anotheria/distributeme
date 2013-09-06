package org.distributeme.test.event;

import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventService;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.ProxyType;

public class StartLocally {
	public static void main(String a[]){
		PushConsumer c1 = new PushConsumer();
		PushConsumer c2 = new PushConsumer();
		
		EventService es = EventServiceFactory.createEventService();
		EventChannel forConsumer = es.obtainEventChannel("TEST", c1);
		forConsumer.addConsumer(c1);
		forConsumer.addConsumer(c2);
		
		EventChannel forSupplier = es.obtainEventChannel("TEST", ProxyType.PUSH_SUPPLIER_PROXY);
		PushSupplier s1 = new PushSupplier(forSupplier);
		PushSupplier s2 = new PushSupplier(forSupplier);
		
		s1.start();
		s2.start();
		
		System.out.println("Setup finished");
	}
}
