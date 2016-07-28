package org.distributeme.registry.esregistry;

import org.distributeme.core.ServiceDescriptor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Describes a channel for internal usage.
 * @author lrosenberg
 *
 */
public class ChannelDescriptor {
	/**
	 * Name of the channel.
	 */
	private String name;
	/**
	 * Registered suppliers for the channel.
	 */
	private List<ServiceDescriptor> suppliers;
	/**
	 * Registered consumers for the channel.
	 */
	private List<ServiceDescriptor> consumers;
	/**
	 * Creates anew channel description.
	 * @param aName
	 */
	public ChannelDescriptor(String aName){
		name = aName;
		suppliers = new CopyOnWriteArrayList<>();
		consumers = new CopyOnWriteArrayList<>();
	}
	/**
	 * Adds a supplier to the channel.
	 * @param descriptor
	 */
	public void addSupplier(ServiceDescriptor descriptor){
		suppliers.add(descriptor);
	}
	
	/**
	 * Adds a consumer to the channel.
	 * @param descriptor
	 */
	public void addConsumer(ServiceDescriptor descriptor){
		consumers.add(descriptor);
	}
	/**
	 * Removes a consumer from the channel.
	 * @param descriptor
	 * @return
	 */
	public boolean removeConsumer(ServiceDescriptor descriptor){
		return consumers.remove(descriptor);
	}
	
	/**
	 * Removes a supplier from the channel.
	 * @param descriptor
	 * @return
	 */
	public boolean removeSupplier(ServiceDescriptor descriptor){
		return suppliers.remove(descriptor);
	}

	/**
	 * Returns the name of the channel.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the suppliers of the channel.
	 * @return
	 */
	public List<ServiceDescriptor> getSuppliers() {
		return suppliers;
	}

	/**
	 * Returns the consumers of the channel.
	 * @return
	 */
	public List<ServiceDescriptor> getConsumers() {
		return consumers;
	}

	@Override public int hashCode(){
		return name.hashCode();
	}
	
	@Override public boolean equals(Object o){
		return o instanceof ChannelDescriptor && 
				name.equals(((ChannelDescriptor)o).name);
	}
	
	@Override public String toString(){
        return name +", Suppliers: "+ suppliers +", Consumers: "+ consumers;
	}
}
