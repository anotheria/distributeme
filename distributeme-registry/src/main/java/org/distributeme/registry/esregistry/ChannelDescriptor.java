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
	private final String name;
	/**
	 * Registered suppliers for the channel.
	 */
	private final List<ServiceDescriptor> suppliers;
	/**
	 * Registered consumers for the channel.
	 */
	private final List<ServiceDescriptor> consumers;

	/**
	 * Creates anew channel description.
	 * @param aName
	 */
	public ChannelDescriptor(String aName){
		name = aName;
		suppliers = new CopyOnWriteArrayList<ServiceDescriptor>();
		consumers = new CopyOnWriteArrayList<ServiceDescriptor>();
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
	 * @param descriptor descriptor to add.
	 */
	public void addConsumer(ServiceDescriptor descriptor){
		//only allow one add at a time to prevent adding two equivalent descriptors.
		synchronized (consumers) {
			//skip if same descriptor already present.
			for (ServiceDescriptor d : consumers){
				if (d.equalsByEndpoint(descriptor)) {
					//instead of not adding new descriptor we remove old descriptor. The reasoning is
					//that we want to keep the most current instanceid and timestamp.
					consumers.remove(d);
				}
			}
			consumers.add(descriptor);
		}
	}
	/**
	 * Removes a consumer from the channel.
	 * @param descriptor descriptor to add.
	 * @return true if the descriptor was really removed or false if there was no such descriptor registered.
	 */
	public boolean removeConsumer(ServiceDescriptor descriptor){
		return consumers.remove(descriptor);
	}
	
	/**
	 * Removes a supplier from the channel.
	 * @param descriptor descriptor to add.
	 * @return true if the descriptor was really removed or false if there was no such descriptor registered.
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
		return getName()+", Suppliers: "+getSuppliers()+", Consumers: "+getConsumers();
	}
}
