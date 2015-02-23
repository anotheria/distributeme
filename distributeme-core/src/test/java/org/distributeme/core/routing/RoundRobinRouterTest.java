package org.distributeme.core.routing;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.RoundRobinRouter;
import org.junit.Test;
import static org.junit.Assert.*;

public class RoundRobinRouterTest {
	@Test public void testForFive(){
		test(5);
	}
	
	@Test public void testForTen(){
		test(10);
	}

	private void test(int limit){
		RoundRobinRouter router = new RoundRobinRouter();
		router.customize(""+limit);
		
		for (int i=0; i<1000; i++){
			router.getServiceIdForCall(new ClientSideCallContext("foo", "foo", null));
		}
	}
	
	@Test public void testUncustomized(){
		RoundRobinRouter router = new RoundRobinRouter();
		assertEquals("aaa", router.getServiceIdForCall(new ClientSideCallContext("aaa", "aaa", null)));
	}
	@Test public void testCustomizationErrors(){
		RoundRobinRouter router = new RoundRobinRouter();
		router.customize("a");
		//uncustomized router should always return the same service id.
		assertEquals("aaa", router.getServiceIdForCall(new ClientSideCallContext("aaa", "aaa", null)));
	}
}
