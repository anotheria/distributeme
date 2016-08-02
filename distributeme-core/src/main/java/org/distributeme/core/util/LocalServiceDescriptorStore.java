package org.distributeme.core.util;

import org.distributeme.core.ServiceDescriptor;

import java.util.LinkedList;
import java.util.List;

/**
 * This utility contains locally known service descriptors, which means service descriptors that have been created in this vm.
 *
 * @author lrosenberg
 * @since 22.02.15 18:25
 */
public class LocalServiceDescriptorStore {
	private List<ServiceDescriptor> descriptors = new LinkedList<>();

	private static LocalServiceDescriptorStore instance = new LocalServiceDescriptorStore();

	public static final LocalServiceDescriptorStore getInstance(){
		return instance;
	}

	public void addServiceDescriptor(ServiceDescriptor serviceDescriptor){
		descriptors.add(serviceDescriptor);
	}

	public List<ServiceDescriptor> getServiceDescriptors(){
		return descriptors;
	}
}
