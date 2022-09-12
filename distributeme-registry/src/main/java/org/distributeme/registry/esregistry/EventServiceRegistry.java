package org.distributeme.registry.esregistry;

import org.distributeme.core.ServiceDescriptor;

import java.util.List;
/**
 * Defines the registry for the event service.
 * @author lrosenberg
 *
 */
public interface EventServiceRegistry {
	/**
	 * Return the names of all registered channels.
	 * @return list of channel names.
	 */
	List<String> getChannelNames();
	/**
	 * Returns all registered channels.
	 * @return list of all channels.
	 */
	List<ChannelDescriptor> getChannels();
	/**
	 * Registers a new supplier on a channel. Returns the list of all registered consumers on the same channel.
	 * @param channelName name of the channel.
	 * @param descriptor supplier descriptor.
	 * @return list of consumers for this supplier to interact with.
	 */
	List<ServiceDescriptor> addSupplier(String channelName, ServiceDescriptor descriptor);
	/**
	 * Registers a new consumer on a channel. Returns the list of all registered suppliers on the same channel.
	 * @param channelName name of the channel.
	 * @param descriptor the new consumer's descriptor.
	 * @return list of suppliers for this consumer to interact with.
	 */
	List<ServiceDescriptor> addConsumer(String channelName, ServiceDescriptor descriptor);
	/**
	 * Called by a supplier to notify that a consumer is not available anymore and should be removed.
	 * @param descriptor
	 */
	void notifyConsumerUnavailable(ServiceDescriptor descriptor);
	/**
	 * Called by a consumer to notify that a supplier is not available anymore and should be removed.
	 * @param descriptor
	 */
	void notifySupplierUnavailable(ServiceDescriptor descriptor);
	
	/**
	 * Returns the channel by the name of null if no channel with this name available yet.
	 * @param channelName
	 * @return
	 */
	ChannelDescriptor getChannel(String channelName);
	
	/**
	 * Resets the registry state. Use with care!
	 */
	void reset();
}
