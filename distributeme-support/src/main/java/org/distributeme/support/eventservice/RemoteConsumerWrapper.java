package org.distributeme.support.eventservice;

import net.anotheria.anoprise.eventservice.EventTransportShell;
import net.anotheria.anoprise.eventservice.RemoteEventServiceConsumer;
import org.distributeme.core.Defaults;
import org.distributeme.core.ServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A local wrapper for a remove event service cnsumer.
 */
public class RemoteConsumerWrapper implements RemoteEventServiceConsumer{
	/**
	 * Logger.
	 */
	private static Logger LOG = LoggerFactory.getLogger(RemoteConsumerWrapper.class);
	/**
	 * The name of the channel this wrapper is attached to.
	 */
	private String channelName;
	private DiMeRemoteEventChannelRMISupport support;

	/**
	 * The home instance this event consumer talks too.
	 */
	private ServiceDescriptor myHomeReference;
	private EventServiceRMIBridgeService bridgeToHome;

	/**
	 * Error counter.
	 */
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
		LOG.warn("Couldn't deliver to "+myHomeReference+" error "+errorCount.get()+" of "+Defaults.getRemoteConsumerWrapperErrorLimit());
		if (errorCount.get()> Defaults.getRemoteConsumerWrapperErrorLimit()){
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
		return myHomeReference.equalsByEndpoint(anotherObj.myHomeReference);

	}
}
