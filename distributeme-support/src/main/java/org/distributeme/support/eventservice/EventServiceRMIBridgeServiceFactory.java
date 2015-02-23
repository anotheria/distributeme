package org.distributeme.support.eventservice;

import net.anotheria.anoprise.eventservice.EventService;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.EventServiceImpl;
import net.anotheria.anoprise.metafactory.ServiceFactory;

public class EventServiceRMIBridgeServiceFactory implements ServiceFactory<EventServiceRMIBridgeService>{

	@Override
	public EventServiceRMIBridgeService create() {
		
		EventService es = EventServiceFactory.createEventService();
		DiMeRemoteEventChannelRMISupport supportObject = new DiMeRemoteEventChannelRMISupport();
		((EventServiceImpl)es).setRemoteSupportFactory(supportObject);
		es.addListener(supportObject);
		
		EventServiceRMIBridgeServiceImpl instance = new EventServiceRMIBridgeServiceImpl(supportObject);
		return instance;
		
	}
	
}
