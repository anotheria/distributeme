package org.distributeme.test.event;

import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventService;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.ProxyType;
import org.distributeme.support.eventservice.DiMeRemoteEventChannelRMISupport;

public class StartSupplier {
	public static void main(String a[]){
		//EventService es = DiMeRemoteEventChannelRMISupport.attachToEventService();
		DiMeRemoteEventChannelRMISupport.initEventService();
		EventService es = EventServiceFactory.createEventService();
		
		EventChannel forSupplier = es.obtainEventChannel("backoffice_channel", ProxyType.PUSH_SUPPLIER_PROXY);
		PushSupplier supplier = new PushSupplier(forSupplier);
		System.out.println("Supplier initied.");
		
		/*EventChannel forConsumer = es.obtainEventChannel("TEST", ProxyType.PUSH_CONSUMER_PROXY);
		PushConsumer consumer = new PushConsumer();
		forConsumer.addConsumer(consumer);
		System.out.println("Consumer initied.");*/

		supplier.start();
		System.out.println("Setup finished");

		test1();
		test2();
		test3();
		
	}
	
	private static void test1(){
		EventService es = EventServiceFactory.createEventService();
		EventChannel forSupplier = es.obtainEventChannel("TEST", ProxyType.PUSH_SUPPLIER_PROXY);
		PushSupplier supplier = new PushSupplier(forSupplier);
		supplier.start();
		System.out.println("Supplier initied.");
		
	}

	private static void test2(){
		EventService es = EventServiceFactory.createEventService();
		EventChannel forSupplier = es.obtainEventChannel("User", ProxyType.PUSH_SUPPLIER_PROXY);
		PushSupplier supplier = new PushSupplier(forSupplier);
		supplier.start();
		System.out.println("Supplier initied.");
		
	}

	private static void test3(){
		EventService es = EventServiceFactory.createEventService();
		EventChannel forSupplier = es.obtainEventChannel("long-and_funny_name", ProxyType.PUSH_SUPPLIER_PROXY);
		PushSupplier supplier = new PushSupplier(forSupplier);
		supplier.start();
		System.out.println("Supplier initied.");
		
	}
}
