package org.distributeme.test.event;

import net.anotheria.anoprise.eventservice.util.QueuedEventSender;
import org.distributeme.support.eventservice.DiMeRemoteEventChannelRMISupport;

public class StartSupplierWithEventSender {
	public static void main(String a[]){
		DiMeRemoteEventChannelRMISupport.initEventService();
		//EventService service = EventServiceFactory.createEventService();
		//EventChannel channel = service.obtainEventChannel("TEST", ProxyType.PUSH_SUPPLIER_PROXY);
		
		QueuedEventSender sender = new QueuedEventSender("Sender", "TEST");
		sender.start();
		
		PushSupplierWithEventSender supplier = new PushSupplierWithEventSender(sender);
		System.out.println("PushSupplierWithEventSender initied.");
		
		supplier.start();
		System.out.println("Setup finished");
	}
}
