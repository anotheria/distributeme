package org.distributeme.test.moskitojourney;


import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.moskito.core.calltrace.CurrentlyTracedCall;
import net.anotheria.moskito.core.calltrace.RunningTraceContainer;
import net.anotheria.moskito.core.calltrace.TracedCall;
import net.anotheria.moskito.core.dynamic.ProxyUtils;
import net.anotheria.moskito.core.journey.Journey;
import net.anotheria.moskito.core.journey.JourneyManager;
import net.anotheria.moskito.core.journey.JourneyManagerFactory;
import org.distributeme.core.ServiceLocator;

public class TestClient2 {
	public static void main(String[] args) throws Exception{
//		System.out.println("Ready.");
//		while(true){
//			System.out.println("Hit anything and press enter ");
//			System.in.read();
			test();
//		}
	}
	
	private static CService local = ProxyUtils.createServiceInstance(new CServiceImpl(), "default", CService.class, Service.class);

	public static void test() throws Exception{
		JourneyManager manager = JourneyManagerFactory.getJourneyManager();
		Journey myJourney = manager.createJourney("dimetestjourney");
		RunningTraceContainer.startTracedCall("TestClient.main");
		
		CService cService = ServiceLocator.getRemote(CService.class);
		System.out.println(local.cMethod("1"));
		System.out.println(cService.cMethod("hello from client"));
		System.out.println(local.cMethod("2"));
		System.out.println(cService.cMethod("and again"));
		System.out.println(local.cMethod("3"));
		
		TracedCall last = RunningTraceContainer.endTrace();
		myJourney.addUseCase((CurrentlyTracedCall)last);

		System.out.println("Journey: "+myJourney);
		System.out.println("  calls: "+myJourney.getTracedCalls());
		
		for (CurrentlyTracedCall call : myJourney.getTracedCalls()){
			System.out.println(call.toDetails());
		}

	}
	
	
	
}
