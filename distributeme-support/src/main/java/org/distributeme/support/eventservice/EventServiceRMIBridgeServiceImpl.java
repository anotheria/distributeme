package org.distributeme.support.eventservice;

import net.anotheria.anoprise.eventservice.EventTransportShell;
import org.distributeme.core.ServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventServiceRMIBridgeServiceImpl implements EventServiceRMIBridgeService{

	private DiMeRemoteEventChannelRMISupport support;
	
	private static final Logger LOG = LoggerFactory.getLogger(EventServiceRMIBridgeServiceImpl.class);
	
	public EventServiceRMIBridgeServiceImpl(DiMeRemoteEventChannelRMISupport aSupport) {
		support = aSupport;
	}
	
	@Override
	public void deliverEvent(EventTransportShell shell)
			throws EventServiceRMIBridgeServiceException {
		if (LOG.isDebugEnabled())
			LOG.debug("Deliver event "+shell);
		support.deliverEvent(shell);
		
	}

	@Override
	public void registerRemoteConsumer(String channelName,
			ServiceDescriptor myReference)
			throws EventServiceRMIBridgeServiceException {
		support.registerRemoteConsumer(channelName, myReference);
		
	}

	@Override
	public void registerRemoteSupplier(String channelName,
			ServiceDescriptor myReference)
			throws EventServiceRMIBridgeServiceException {
		support.registerRemoteSupplier(channelName, myReference);
		
	}

	@Override
	public String getInstanceId() throws EventServiceRMIBridgeServiceException {
		return support.getHomeReference().getInstanceId();
	}
	
	
	
}
