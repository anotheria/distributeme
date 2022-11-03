package org.distributeme.test.moskitojourney;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.moskito.core.calltrace.CurrentlyTracedCall;
import net.anotheria.moskito.core.calltrace.RunningTraceContainer;
import net.anotheria.moskito.core.calltrace.TracedCall;
import net.anotheria.moskito.core.journey.Journey;
import net.anotheria.moskito.core.journey.JourneyManager;
import net.anotheria.moskito.core.journey.JourneyManagerFactory;
import net.anotheria.moskito.core.stats.TimeUnit;
import net.anotheria.moskito.webui.embedded.StartMoSKitoInspectBackendForRemote;
import net.anotheria.moskito.webui.journey.api.*;
import org.distributeme.core.ServiceLocator;

import java.util.List;

public class TestClient {
	public static void main(String[] args) throws AServiceException, APIException ,Exception {
		StartMoSKitoInspectBackendForRemote.startMoSKitoInspectBackend();
		JourneyManager manager = JourneyManagerFactory.getJourneyManager();
		Journey myJourney = manager.createJourney("dimetestjourney");
		RunningTraceContainer.startTracedCall("TestClient.main");
		AService aService = ServiceLocator.getRemote(AService.class);
		try {
			System.out.println(aService.aMethod("hello from client"));
		}catch(Exception e){
			System.out.println("ERROR");
		}
		//System.out.println(aService.aMethod("hello 2 from client"));

		
		TracedCall last = RunningTraceContainer.endTrace();
		myJourney.addCall((CurrentlyTracedCall)last);

		System.out.println("Journey: "+myJourney);
		System.out.println("  calls: "+myJourney.getTracedCalls());


		for (CurrentlyTracedCall call : myJourney.getTracedCalls()){
			System.out.println(call.getName() + "- "+call.getNumberOfSteps());

		}


		JourneyAPI api = APIFinder.findAPI(JourneyAPI.class);
		JourneyAO journeyAO = api.getJourney("dimetestjourney", TimeUnit.MILLISECONDS);
		System.out.println("Journey AO: "+journeyAO);
		List<JourneySingleTracedCallAO> calls =  journeyAO.getCalls();
		System.out.println("Calls: "+calls);
		TracedCallAO callAO = api.getTracedCallByName("dimetestjourney", "TestClient.main", TimeUnit.MILLISECONDS);
		List<TracedCallStepAO> elements = callAO.getElements();
		for (TracedCallStepAO step : elements){
			System.out.println(getIdent(step.getLayer())+" "+step);
		}
		
	}

	private static String getIdent(int recursion){
		StringBuilder s = new StringBuilder();
		for (int i=0; i<recursion; i++)
			s.append(" ");
		return s.toString();
	}
}
