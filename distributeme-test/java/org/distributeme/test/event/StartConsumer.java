package org.distributeme.test.event;

import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventService;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.ProxyType;

import org.distributeme.support.eventservice.DiMeRemoteEventChannelRMISupport;

public class StartConsumer {
	public static void main(String a[]){
		DiMeRemoteEventChannelRMISupport.initEventService();
		
		EventService es = EventServiceFactory.createEventService();
		EventChannel forConsumer = es.obtainEventChannel("backoffice_channel", ProxyType.PUSH_CONSUMER_PROXY);
		PushConsumer consumer = new PushConsumer();
		forConsumer.addConsumer(consumer);
		System.out.println("Setup finished");
		
		while(true){
			try{
				Thread.sleep(2000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

}
