package org.distributeme.test.echoplusevent;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventService;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.ProxyType;

import org.distributeme.support.eventservice.DiMeRemoteEventChannelRMISupport;
import org.distributeme.test.echo.EchoService;
import org.distributeme.test.echo.EchoServiceImpl;

public class EchoPlusEventServiceImpl extends EchoServiceImpl implements EchoPlusEventService{
	
	
	public EchoPlusEventServiceImpl() {
		super();
		new Thread(){
			public void run(){
				System.out.println("Starting PUSH Thread");

				//DiMeRemoteEventChannelRMISupport.initEventService();
				
				String data = "event from echo+eventservice";
				EventService es = EventServiceFactory.createEventService();
				System.out.println("EVENT SERVICE: "+es);

				EventChannel channel = es.obtainEventChannel("echoplusevent", ProxyType.PUSH_SUPPLIER_PROXY);
				while(true){
					try{
						sleep(1000);
					}catch(InterruptedException ignored){}
					Event event = new Event(data);
					System.out.println("Sending "+event);
					channel.push(event);
				}
			}
		}.start();
	}
}
