package org.distributeme.support.eventservice;

import net.anotheria.anoprise.eventservice.EventTransportShell;
import net.anotheria.anoprise.eventservice.RemoteEventServiceConsumer;
import org.distributeme.core.ServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class RemoteConsumerWrapper implements RemoteEventServiceConsumer{
	
	public static final int ERROR_LIMIT = 5;

	private static Logger LOG = LoggerFactory.getLogger(RemoteConsumerWrapper.class);
	
	private String channelName;
	private DiMeRemoteEventChannelRMISupport support;
	
	private ServiceDescriptor myHomeReference;
	private EventServiceRMIBridgeService bridgeToHome;
	
	private AtomicInteger errorCount = new AtomicInteger(0);
 	
	public RemoteConsumerWrapper(DiMeRemoteEventChannelRMISupport aSupport, String aChannelName, ServiceDescriptor aHomeReference, EventServiceRMIBridgeService aBridgeToHome) {
		myHomeReference = aHomeReference;
		bridgeToHome = aBridgeToHome;
		support = aSupport;
		channelName = aChannelName;
	}
	
	@Override
	public void deliverEvent(EventTransportShell event) {
		LOG.debug("Sending event "+event+" home; "+myHomeReference);
		try{
			bridgeToHome.deliverEvent(event);
			errorCount.set(0);
		}catch(EventServiceRMIBridgeServiceException e){
			handleError();
		}catch(RuntimeException e){
			handleError();
		}
	}
	
	private void handleError(){
		errorCount.incrementAndGet();
		LOG.warn("Couldn't deliver to "+myHomeReference+" error "+errorCount.get()+" of "+ERROR_LIMIT);
		if (errorCount.get()>ERROR_LIMIT){
			LOG.warn(myHomeReference+" is obviously offline, removing");
			support.notifyBrokenConsumer(this);
			bridgeToHome = null; //prevent myself from sending further events.
		}
	}
	
	public String getChannelName(){
		return channelName;
	}
	
	public ServiceDescriptor getHomeReference(){
		return myHomeReference;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RemoteConsumerWrapper))
			return false;
		RemoteConsumerWrapper anotherObj = (RemoteConsumerWrapper)obj;
		if (myHomeReference == null)
			return anotherObj.myHomeReference == null;
		return myHomeReference.equals(anotherObj.myHomeReference);

	}
}
