package org.distributeme.test.moskitojourney;

import net.anotheria.moskito.core.calltrace.CurrentlyTracedCall;
import net.anotheria.moskito.core.calltrace.RunningTraceContainer;
import net.anotheria.moskito.core.calltrace.TracedCall;
import net.anotheria.moskito.core.journey.Journey;
import net.anotheria.moskito.core.journey.JourneyManager;
import net.anotheria.moskito.core.journey.JourneyManagerFactory;
import org.distributeme.core.ServiceLocator;

public class TestClient {
	public static void main(String[] args) throws AServiceException{
		JourneyManager manager = JourneyManagerFactory.getJourneyManager();
		Journey myJourney = manager.createJourney("dimetestjourney");
		RunningTraceContainer.startTracedCall("TestClient.main");
		AService aService = ServiceLocator.getRemote(AService.class);
		System.out.println(aService.aMethod("hello from client"));
		System.out.println(aService.aMethod("hello 2 from client"));

		
		TracedCall last = RunningTraceContainer.endTrace();
		myJourney.addUseCase((CurrentlyTracedCall)last);

		System.out.println("Journey: "+myJourney);
		System.out.println("  calls: "+myJourney.getTracedCalls());

		/*
		for (CurrentlyTracedCall call : myJourney.getTracedCalls()){
			System.out.println(call.toDetails());
		}
		*/
		
	}
}
