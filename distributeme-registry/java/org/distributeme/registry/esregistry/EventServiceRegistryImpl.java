package org.distributeme.registry.esregistry;

import org.distributeme.core.ServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of the event service registry.
 * @author lrosenberg
 *
 */
public class EventServiceRegistryImpl implements EventServiceRegistry{
	

	private static final EventServiceRegistry instance = new EventServiceRegistryImpl();
	
	public static final EventServiceRegistry getInstance(){
		return instance;
	}
	
	/**
	 * Targets of the operations.
	 * @author another
	 *
	 */
	private static enum Target{
		/**
		 * This target is a supplier.
		 */
		SUPPLIER,
		/**
		 * This target is a consumer.
		 */
		CONSUMER;
	}
	/**
	 * Logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(EventServiceRegistryImpl.class);
	
	/**
	 * Registered channels.
	 */
	private ConcurrentMap<String, ChannelDescriptor> channels;
	
	/**
	 * Creates a new registry.
	 */
	EventServiceRegistryImpl() {
		channels = new ConcurrentHashMap<String, ChannelDescriptor>();
	}
	
	
	@Override 
	public void reset(){
		LOG.warn("reset called!");
		channels.clear();
	}
	
	@Override
	public List<ServiceDescriptor> addConsumer(String channelName,
			ServiceDescriptor descriptor) {
		
		ChannelDescriptor channel = addDescriptorToChannel(channelName, descriptor, Target.CONSUMER); 
		return channel.getSuppliers();
	}

	@Override
	public List<ServiceDescriptor> addSupplier(String channelName,
			ServiceDescriptor descriptor) {
		ChannelDescriptor channel = addDescriptorToChannel(channelName, descriptor, Target.SUPPLIER); 
		return channel.getConsumers();
	}

	@Override
	public List<String> getChannelNames() {
		ArrayList<String> ret = new ArrayList<String>(channels.size());
		ret.addAll(channels.keySet());
		return ret;
	}

	@Override
	public List<ChannelDescriptor> getChannels() {
		ArrayList<ChannelDescriptor> ret = new ArrayList<ChannelDescriptor>(channels.size());
		ret.addAll(channels.values());
		return ret;
	}
	
	@Override public ChannelDescriptor getChannel(String channelName){
		return channels.get(channelName);
	}

	@Override
	public void notifyConsumerUnavailable(ServiceDescriptor descriptor) {
		removeDescriptorFromAllChannels(descriptor, Target.CONSUMER);
	}

	@Override
	public void notifySupplierUnavailable(ServiceDescriptor descriptor) {
		removeDescriptorFromAllChannels(descriptor, Target.SUPPLIER);
	}
	
	/**
	 * Adds a service descriptor to the channel.
	 * @param channelName name of the channel.
	 * @param descriptor newly added service descriptor.
	 * @param target target to add as - consumer or supplier.
	 * @return
	 */
	private ChannelDescriptor addDescriptorToChannel(String channelName, ServiceDescriptor descriptor, Target target){
		ChannelDescriptor channel = channels.get(channelName);
		if (channel==null){
			channel = new ChannelDescriptor(channelName);
			ChannelDescriptor oldchannel = channels.putIfAbsent(channelName, channel);
			if (oldchannel!=null)
				channel = oldchannel;
		}
		if (target == Target.CONSUMER)
			channel.addConsumer(descriptor);
		else
			channel.addSupplier(descriptor);
		return channel;
	}
	
	private void removeDescriptorFromAllChannels(ServiceDescriptor descriptor, Target target){
		int total = 0;
		for (ChannelDescriptor channel : channels.values()){
			boolean result = target == Target.CONSUMER ? 
					channel.removeConsumer(descriptor) : channel.removeSupplier(descriptor);
			if (result)
				total++;
		}
		LOG.debug("Removed "+descriptor+" as "+target+" from "+total+" channels");
	}

}
