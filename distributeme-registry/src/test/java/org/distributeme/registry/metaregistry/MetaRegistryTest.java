package org.distributeme.registry.metaregistry;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Contract test for the meta registry
 * @author lrosenberg.
 *
 */
public class MetaRegistryTest {
	private static MetaRegistry registry;
	
	@BeforeAll public static void createRegistry(){
		registry = MetaRegistryImpl.getInstance();
	}
	
	@Disabled @Test public void basicFunctionalityTest(){
		/*
		assertTrue(registry.bind("myservice","localhost"));
		
		assertEquals("localhost", registry.resolve("myservice"));
		assertNull(registry.resolve("non-existent"), "No value expected");
		
		List<? extends Binding> bindings = registry.list();
		assertEquals(1, bindings.size());
		Binding my = bindings.get(0);
		assertEquals("myservice", my.getServiceId());
		assertEquals("localhost", my.getHost());
		
		// REMOVING SERVICE 
		assertTrue(registry.unbind("myservice", "localhost"));

		assertNull(registry.resolve("myservice"));
		assertNull(registry.resolve("non-existent"), "No value expected");
		
		bindings = registry.list();
		assertEquals(0, bindings.size());
*/
	}
}
