package org.distributeme.registry.esregistry;

import net.anotheria.util.IdCodeGenerator;
import org.distributeme.core.ServiceDescriptor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test is used to confirm that duplicate registration of consumers is prevented.
 *
 * @author lrosenberg
 * @since 04.02.21 10:54
 */
public class DuplicateConsumersTest {

	//prior to the fix, if I register two identical consumers they will both show up.
	//This test should prevent this from happening.
	@Test public void testBugFunctionality(){
		ServiceDescriptor s1 = createDescriptor("localhost", 9250);
		ServiceDescriptor s2 = createDescriptor("localhost", 9250);
		EventServiceRegistry registry = EventServiceRegistryImpl.getInstance();

		registry.addConsumer("TEST-BUG", s1);
		registry.addConsumer("TEST-BUG", s2);

		assertEquals(1, registry.getChannel("TEST-BUG").getConsumers().size());

	}

	//behaviour for different ports or hosts shouldn't be affected.
	@Test public void testRegularFunctionality(){
		ServiceDescriptor s1 = createDescriptor("localhost", 9250);
		ServiceDescriptor s2 = createDescriptor("localhost", 9251);
		ServiceDescriptor s3 = createDescriptor("anotherhost", 9251);
		EventServiceRegistry registry = EventServiceRegistryImpl.getInstance();

		registry.addConsumer("TEST-REG", s1);
		registry.addConsumer("TEST-REG", s2);
		registry.addConsumer("TEST-REG", s3);

		assertEquals(3, registry.getChannel("TEST-REG").getConsumers().size());

	}

	private ServiceDescriptor createDescriptor(String host, int port){
		return new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, EventServiceRegistryTest.class.getName(), IdCodeGenerator.generateCode(5), host, port);
	}

}


