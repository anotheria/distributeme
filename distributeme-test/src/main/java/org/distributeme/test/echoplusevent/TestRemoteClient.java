package org.distributeme.test.echoplusevent;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventServiceConsumer;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.EventServicePushConsumer;
import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.ServiceFactory;
import org.distributeme.support.eventservice.DiMeRemoteEventChannelRMISupport;
import org.distributeme.test.echo.Echo;


public class TestRemoteClient implements EventServicePushConsumer{
	public static void main(String a[]) throws Exception{
		MetaFactory.addFactoryClass(EchoPlusEventService.class, Extension.REMOTE, (Class<ServiceFactory<EchoPlusEventService>>)Class.forName("org.distributeme.test.echoplusevent.generated.RemoteEchoPlusEventServiceFactory"));
		EchoPlusEventService service = MetaFactory.get(EchoPlusEventService.class, Extension.REMOTE);
		DiMeRemoteEventChannelRMISupport.initEventService();
		EventServiceConsumer consumer = new TestRemoteClient();
		EventChannel channel = EventServiceFactory.createEventService().obtainEventChannel("echoplusevent", consumer);
		channel.addConsumer(consumer);
		
		
		service.printHello();
		System.out.println("sent printHello");
		
		Echo echo = new Echo();
		System.out.println("sending echo: "+echo);
		echo = service.echo(echo);
		System.out.println("received echo: "+echo);
		
		long limit = 10000;
		long start = System.nanoTime();
		for (int i=0; i<10000; i++)
			echo = service.echo(echo);
		long end = System.nanoTime();
		double duration = ((double)(end-start)) / 1000 / 1000;
		System.out.println("Sent "+limit+" requests in "+duration);
		System.out.println("avg req dur: "+(duration/limit)+" ms.");
		
		System.out.println("Now waiting 100 seconds");
		Thread.currentThread().sleep(1000*100);
		System.out.println("Finished...");
	}
	
	public void push(Event e){
		System.out.println("Got event "+e);
	}
}
