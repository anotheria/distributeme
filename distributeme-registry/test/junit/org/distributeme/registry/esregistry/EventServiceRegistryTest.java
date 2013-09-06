package org.distributeme.registry.esregistry;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.anotheria.util.IdCodeGenerator;

import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.ServiceDescriptor.Protocol;
import org.distributeme.registry.esregistry.EventServiceRegistry;
import org.distributeme.registry.esregistry.EventServiceRegistryImpl;
import org.junit.Test;

public class EventServiceRegistryTest {
	@Test public void testFunctionality(){
		ServiceDescriptor s1 = createDummyDescriptor();
		ServiceDescriptor s2 = createDummyDescriptor();
		ServiceDescriptor s3 = createDummyDescriptor();
		
		ServiceDescriptor c1 = createDummyDescriptor();
		ServiceDescriptor c2 = createDummyDescriptor();
		//ServiceDescriptor c3 = createDummyDescriptor();
		
		EventServiceRegistry registry = new EventServiceRegistryImpl();
		
		assertEquals(0, registry.getChannelNames().size());
		assertEquals(0, registry.getChannels().size());
		
		//creating a dummy channel...
		assertEquals(0, registry.addSupplier("FOO", s1).size());
		
		List<ServiceDescriptor> suppliers = registry.addConsumer("FOO", c1);
		assertEquals(1, suppliers.size());
		assertEquals(s1, suppliers.get(0));
		
		
		assertEquals(1, registry.addSupplier("FOO", s2).size());
		assertEquals(2, registry.addConsumer("FOO", c2).size());
		
		List<ServiceDescriptor> consumers = registry.addSupplier("FOO", s3);
		assertEquals(2, consumers.size());
		consumers.remove(c1); 		
		consumers.remove(c2);
		assertEquals(0, consumers.size());
		
		
	}
	
	@Test public void testMassRemove(){
		ServiceDescriptor s1 = createDummyDescriptor();
		EventServiceRegistry registry = new EventServiceRegistryImpl();
		
		registry.addSupplier("A", s1);
		registry.addSupplier("B", s1);
		registry.addSupplier("C", s1);
		
		assertEquals(1, registry.getChannel("A").getSuppliers().size());
		assertEquals(1, registry.getChannel("B").getSuppliers().size());
		assertEquals(1, registry.getChannel("C").getSuppliers().size());
		
		registry.notifySupplierUnavailable(s1);

		assertEquals(0, registry.getChannel("A").getSuppliers().size());
		assertEquals(0, registry.getChannel("B").getSuppliers().size());
		assertEquals(0, registry.getChannel("C").getSuppliers().size());
		
		////// same for consumers 

		ServiceDescriptor c1 = createDummyDescriptor();
		
		registry.addConsumer("A", c1);
		registry.addConsumer("B", c1);
		registry.addConsumer("C", c1);
		
		assertEquals(1, registry.getChannel("A").getConsumers().size());
		assertEquals(1, registry.getChannel("B").getConsumers().size());
		assertEquals(1, registry.getChannel("C").getConsumers().size());
		
		registry.notifyConsumerUnavailable(c1);

		assertEquals(0, registry.getChannel("A").getConsumers().size());
		assertEquals(0, registry.getChannel("B").getConsumers().size());
		assertEquals(0, registry.getChannel("C").getConsumers().size());
}
	
	private ServiceDescriptor createDummyDescriptor(){
		return new ServiceDescriptor(Protocol.RMI, EventServiceRegistryTest.class.getName(), IdCodeGenerator.generateCode(5), "localhost", 9250);
	}
}
