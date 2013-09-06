package org.distributeme.core.routing;

import static org.junit.Assert.assertEquals;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.failing.FailDecision;
import org.junit.Test;

public class RoundRobinRouterWithFailoverToNextNodeTest {
	@Test public void testForFive(){
		test(5);
	}
	
	@Test public void testForTen(){
		test(10);
	}

	private void test(int limit){
		RoundRobinRouterWithFailoverToNextNode router = new RoundRobinRouterWithFailoverToNextNode();
		router.customize(""+limit);
		
		for (int i=0; i<1000; i++){
			router.getServiceIdForCall(new ClientSideCallContext("foo", "foo", null));
		}
	}
	
	@Test public void testUncustomized(){
		RoundRobinRouterWithFailoverToNextNode router = new RoundRobinRouterWithFailoverToNextNode();
		assertEquals("aaa", router.getServiceIdForCall(new ClientSideCallContext("aaa", "aaa", null)));
	}
	@Test public void testCustomizationErrors(){
		RoundRobinRouterWithFailoverToNextNode router = new RoundRobinRouterWithFailoverToNextNode();
		router.customize("a");
		//uncustomized router should always return the same service id.
		assertEquals("aaa", router.getServiceIdForCall(new ClientSideCallContext("aaa", "aaa", null)));
	}
	
	@Test public void testFailingUnrouted(){
		RoundRobinRouterWithFailoverToNextNode router = new RoundRobinRouterWithFailoverToNextNode();
		router.customize("3");
		assertEquals("foo", router.getServiceIdForCall(createContext("foo", "bar", 1)));
	}
	
	@Test public void testFailingRouted(){
		RoundRobinRouterWithFailoverToNextNode router = new RoundRobinRouterWithFailoverToNextNode();
		router.customize("3");
		assertEquals("foo_2", router.getServiceIdForCall(createContext("foo_1", "bar", 1)));
	}

	@Test public void testFailingRoutedWithModOverflow(){
		RoundRobinRouterWithFailoverToNextNode router = new RoundRobinRouterWithFailoverToNextNode();
		router.customize("3");
		assertEquals("foo_0", router.getServiceIdForCall(createContext("foo_2", "bar", 2)));
	}

	@Test public void testFailingDecisionWith3(){
		RoundRobinRouterWithFailoverToNextNode router = new RoundRobinRouterWithFailoverToNextNode();
		router.customize("3");
		assertEquals(FailDecision.Reaction.RETRY, router.callFailed(createContext("foo_0", "bar", 0)).getReaction());
		assertEquals(FailDecision.Reaction.RETRY, router.callFailed(createContext("foo_1", "bar", 1)).getReaction());
		assertEquals(FailDecision.Reaction.FAIL, router.callFailed(createContext("foo_2", "bar", 2)).getReaction());
	}

	@Test public void testFailingDecisionWith2(){
		RoundRobinRouterWithFailoverToNextNode router = new RoundRobinRouterWithFailoverToNextNode();
		router.customize("2");
		assertEquals(FailDecision.Reaction.RETRY, router.callFailed(createContext("foo_1", "bar", 0)).getReaction());
		assertEquals(FailDecision.Reaction.FAIL, router.callFailed(createContext("foo_2", "bar", 1)).getReaction());
	}

	private static ClientSideCallContext createContext(String ifc, String method, int callCount){
		ClientSideCallContext ctx = new ClientSideCallContext(ifc, method, null);
		ctx.setCallCount(callCount);
		return ctx;
	}
}
