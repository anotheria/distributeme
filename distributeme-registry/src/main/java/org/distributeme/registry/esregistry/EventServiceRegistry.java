package org.distributeme.registry.esregistry;

import java.util.List;

import org.distributeme.core.ServiceDescriptor;
/**
 * Defines the registry for the event service.
 * @author lrosenberg
 *
 */
public interface EventServiceRegistry {
	/**
	 * Return the names of all registered channels.
	 * @return
	 */
	List<String> getChannelNames();
	/**
	 * Returns all registered channels.
	 * @return
	 */
	List<ChannelDescriptor> getChannels();
	/**
	 * Registers a new supplier on a channel. Returns the list of all registered consumers on the same channel.
	 * @param channelName name of the channel.
	 * @param descriptor
	 * @return
	 */
	List<ServiceDescriptor> addSupplier(String channelName, ServiceDescriptor descriptor);
	/**
	 * Registers a new consumer on a channel. Returns the list of all registered suppliers on the same channel.
	 * @param channelName name of the channel.
	 * @param descriptor the new consumers descriptor.
	 * @return
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
