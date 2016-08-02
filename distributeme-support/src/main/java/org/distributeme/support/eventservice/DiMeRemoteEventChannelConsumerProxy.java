package org.distributeme.support.eventservice;

import net.anotheria.anoprise.eventservice.*;
import net.anotheria.net.util.ByteArraySerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DiMeRemoteEventChannelConsumerProxy implements RemoteEventChannelConsumerProxy{
	
	/**
	 * Logger.
	 */
	private static Logger LOG = LoggerFactory.getLogger(DiMeRemoteEventChannelConsumerProxy.class);
	
	/**
	 * List of registered consumers.
	 */
	private List<RemoteEventServiceConsumer> consumers = new CopyOnWriteArrayList<>();
	/**
	 * Name of the channel
	 */
	private String channelName;
	
	DiMeRemoteEventChannelConsumerProxy(String aChannelName){
		channelName = aChannelName;
	}
	
	@Override
	public void addRemoteConsumer(RemoteEventServiceConsumer consumer) {
		consumers.add(consumer);
	}

	@Override
	public void removeRemoteConsumer(RemoteEventServiceConsumer consumer) {
		consumers.remove(consumer);
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
		throw new UnsupportedOperationException("addConsumer");
	}

	@Override
	public void removeConsumer(EventServiceConsumer consumer) {
		throw new UnsupportedOperationException("addConsumer");
	}

	@Override
	public void pushEvent(Event e) {
		EventTransportShell shell = new EventTransportShell();
        shell.setChannelName(channelName);
		try{
			shell.setData(ByteArraySerializer.serializeObject(e));
		}catch(IOException ex){
			//maybe we should throw an exception, but this
			LOG.error("Can't serialize event data, aborting event", ex);
			throw new IllegalArgumentException("Event "+e+" contains not serializeable data part", ex);
		}
		for (RemoteEventServiceConsumer consumer : consumers){
			try{
				consumer.deliverEvent(shell);
			}catch(Exception ex){
				LOG.warn("Consumer "+consumer +" failed, skipping", ex);
			}
		}
	}

	@Override
	public boolean isLocal() {
		return false;
	}

}
