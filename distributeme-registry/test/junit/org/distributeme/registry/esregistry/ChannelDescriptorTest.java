package org.distributeme.registry.esregistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.anotheria.util.IdCodeGenerator;

import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.ServiceDescriptor.Protocol;
import org.distributeme.registry.esregistry.ChannelDescriptor;
import org.junit.Test;

public class ChannelDescriptorTest {
	@Test public void testEqualsAndHashCode(){
		ChannelDescriptor emptyA = new ChannelDescriptor("A");
		ChannelDescriptor emptyB = new ChannelDescriptor("B");
		ChannelDescriptor emptyA2 = new ChannelDescriptor("A");
		ChannelDescriptor fullA = new ChannelDescriptor("A");
		fullA.addConsumer(createDummyServiceDescriptor());
		fullA.addSupplier(createDummyServiceDescriptor());
		
		assertEquals(emptyA, emptyA2);
		assertEquals(emptyA.hashCode(), emptyA2.hashCode());
		
		assertFalse(emptyA.equals(emptyB));
		assertFalse(emptyA.hashCode()==emptyB.hashCode());
		
		assertEquals(emptyA, fullA);
		assertEquals(emptyA.hashCode(), fullA.hashCode());
		
	}
	
	@Test public void testToString(){
		ChannelDescriptor c = new ChannelDescriptor("teststring1");
		c.addConsumer(new ServiceDescriptor(Protocol.RMI, "consumer1", IdCodeGenerator.generateCode(5), "consumerhost", 9250));
		c.addSupplier(new ServiceDescriptor(Protocol.RMI, "supplier1", IdCodeGenerator.generateCode(5), "supplierhost", 9260));
		
		String toString = c.toString();
		assertNotNull(toString);
		
		//assert all needed strings are included
		assertTrue(toString.indexOf("consumerhost")!=-1);
		assertTrue(toString.indexOf("consumer1")!=-1);
		assertTrue(toString.indexOf("9250")!=-1);
		assertTrue(toString.indexOf("9260")!=-1);
		assertTrue(toString.indexOf("supplier1")!=-1);
		assertTrue(toString.indexOf("supplierhost")!=-1);
	}
	
	@Test public void testAddRemove(){
		ChannelDescriptor c = new ChannelDescriptor("AAA");
		assertEquals(0, c.getConsumers().size());
		
		ServiceDescriptor c1 = createDummyServiceDescriptor();
		c.addConsumer(c1);
		assertEquals(1, c.getConsumers().size());
		
		c.removeConsumer(c1);
		assertEquals(0, c.getConsumers().size());

		//suppliers
		assertEquals(0, c.getSuppliers().size());
		
		ServiceDescriptor s1 = createDummyServiceDescriptor();
		c.addSupplier(s1);
		assertEquals(1, c.getSuppliers().size());
		
		c.removeSupplier(s1);
		assertEquals(0, c.getSuppliers().size());
	
	}

	private ServiceDescriptor createDummyServiceDescriptor(){
		return new ServiceDescriptor(Protocol.RMI, EventServiceRegistryTest.class.getName(), IdCodeGenerator.generateCode(5), "localhost", 9250);
	}

}
