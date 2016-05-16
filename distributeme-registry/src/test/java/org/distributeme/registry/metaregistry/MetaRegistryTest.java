package org.distributeme.registry.metaregistry;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Contract test for the meta registry
 * @author lrosenberg.
 *
 */
public class MetaRegistryTest {
	private static MetaRegistry registry;
	
	@BeforeClass public static void createRegistry(){
		registry = MetaRegistryImpl.getInstance();
	}
	
	@Ignore @Test public void basicFunctionalityTest(){
		/*
		assertTrue(registry.bind("myservice","localhost"));
		
		assertEquals("localhost", registry.resolve("myservice"));
		assertNull("No value expected", registry.resolve("non-existent"));
		
		List<? extends Binding> bindings = registry.list();
		assertEquals(1, bindings.size());
		Binding my = bindings.get(0);
		assertEquals("myservice", my.getServiceId());
		assertEquals("localhost", my.getHost());
		
		// REMOVING SERVICE 
		assertTrue(registry.unbind("myservice", "localhost"));

		assertNull(registry.resolve("myservice"));
		assertNull("No value expected", registry.resolve("non-existent"));
		
		bindings = registry.list();
		assertEquals(0, bindings.size());
*/
	}
}
