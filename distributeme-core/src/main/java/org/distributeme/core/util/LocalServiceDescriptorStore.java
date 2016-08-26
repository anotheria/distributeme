package org.distributeme.core.util;

import org.distributeme.core.ServiceDescriptor;

import java.util.LinkedList;
import java.util.List;

/**
 * This utility contains locally known service descriptors, which means service descriptors that have been created in this vm.
 *
 * @author lrosenberg
 * @since 22.02.15 18:25
 * @version $Id: $Id
 */
public class LocalServiceDescriptorStore {
	private List<ServiceDescriptor> descriptors = new LinkedList<ServiceDescriptor>();

	private static LocalServiceDescriptorStore instance = new LocalServiceDescriptorStore();

	/**
	 * <p>Getter for the field <code>instance</code>.</p>
	 *
	 * @return a {@link org.distributeme.core.util.LocalServiceDescriptorStore} object.
	 */
	public static final LocalServiceDescriptorStore getInstance(){
		return instance;
	}

	/**
	 * <p>addServiceDescriptor.</p>
	 *
	 * @param serviceDescriptor a {@link org.distributeme.core.ServiceDescriptor} object.
	 */
	public void addServiceDescriptor(ServiceDescriptor serviceDescriptor){
		descriptors.add(serviceDescriptor);
	}

	/**
	 * <p>getServiceDescriptors.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<ServiceDescriptor> getServiceDescriptors(){
		return descriptors;
	}
}
