package org.distributeme.core.routing;

import net.anotheria.util.IdCodeGenerator;
import org.distributeme.core.ClientSideCallContext;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NoOpRouterTest {
	@Test public void testGetServiceId(){
		NoOpRouter router = new NoOpRouter();
		
		for (int i=0; i<100; i++){
			String serviceId = IdCodeGenerator.generateCode(100);
			assertEquals(serviceId, router.getServiceIdForCall(new ClientSideCallContext(serviceId, IdCodeGenerator.generateCode(10), new ArrayList())));
		}
	}
	
	@Test public void crashClass(){
		NoOpRouter router = new NoOpRouter();
		
		router.customize(null);
		router.customize("abc");
		router.customize("");
		
		assertNull(router.getServiceIdForCall(new ClientSideCallContext(null, null, null)));
		
	}
}
 