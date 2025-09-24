package org.distributeme.support.eventservice;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventChannelConsumerProxy;
import net.anotheria.anoprise.eventservice.EventServiceConsumer;
import net.anotheria.anoprise.eventservice.RemoteEventChannelSupplierProxy;
import net.anotheria.util.ByteArraySerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class DiMeRemoteEventChannelSupplierProxy implements RemoteEventChannelSupplierProxy{
	
	private String channelName;
	private CopyOnWriteArrayList<EventChannelConsumerProxy> consumerProxies = new CopyOnWriteArrayList<EventChannelConsumerProxy>();
	
	private static Logger log = LoggerFactory.getLogger(DiMeRemoteEventChannelSupplierProxy.class);
	
	DiMeRemoteEventChannelSupplierProxy(String aChannelName){
		channelName = aChannelName;
	}

	@Override
	public void deliverEvent(byte[] eventData) {
		Event event = null;
		try{
			event = (Event)ByteArraySerializer.deserializeObject(eventData);
		}catch(IOException e){
			log.warn("Couldn't deserialize event data, throwing away. ", e);
			return;
		}
		if (event.isNonExistent())
			return;
		push(event);
	}

	@Override
	public void addConsumer(EventServiceConsumer consumer) {
		throw new UnsupportedOperationException("addConsumer");
	}

	@Override
	public String getName() {
		return channelName;
	}

	@Override
	public void push(Event e) {
		for (EventChannelConsumerProxy proxy : consumerProxies){
			if (proxy.isLocal())
				proxy.pushEvent(e);
		}
	}

	@Override
	public void removeConsumer(EventServiceConsumer consumer) {
		throw new UnsupportedOperationException("removeConsumer");
	}

	@Override
	public void addConsumerProxy(EventChannelConsumerProxy proxy) {
		consumerProxies.add(proxy);
		
	}

	@Override
	public void removeConsumerProxy(EventChannelConsumerProxy proxy) {
		consumerProxies.remove(proxy);
	}

	@Override
	public boolean isLocal() {
		return false;
	}

}
