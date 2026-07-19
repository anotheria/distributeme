package org.distributeme.core.routing;

import org.junit.jupiter.api.Assertions;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.failing.FailDecision;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Junit test.
 *
 * @author h3llka
 */
public class AbstractRouterWithFailOversToNextNodeTest {

	private static final String METHOD1 = "method1";
	private static final String METHOD2 = "method2";


	@Test
	public void testWithDefaultServersAmount() {
		RoundRobinFailOverOn rrFon = new RoundRobinFailOverOn();
		RoundRobinFailOverOFF rrfOff = new RoundRobinFailOverOFF();
		ModRouterWithEnabledFailing modFon = new ModRouterWithEnabledFailing();
		ModRouterWithDisabledFailing modfOff = new ModRouterWithDisabledFailing();
		//let's call  customization with illegal argument!

		rrfOff.customize("asd");
		rrFon.customize("asd");
		modfOff.customize("asd");
		modFon.customize("asd");


		ClientSideCallContext callContext = new ClientSideCallContext("Service_default", METHOD1, Arrays.asList("11"));

		Assertions.assertEquals(callContext.getServiceId(), rrfOff.getServiceIdForCall(callContext), "Illegal behaviour is RR-FailOver off router");
		Assertions.assertEquals(callContext.getServiceId(), rrFon.getServiceIdForCall(callContext), "Illegal behaviour is RR-FailOver on router");
		Assertions.assertEquals(callContext.getServiceId(), modfOff.getServiceIdForCall(callContext), "Illegal behaviour is MOD-FailOver off router");
		Assertions.assertEquals(callContext.getServiceId(), modFon.getServiceIdForCall(callContext), "Illegal behaviour is MOD-FailOver on router");

	}


	@Test
	public void testFailingWithDefaults() {
		RoundRobinFailOverOn rrFon = new RoundRobinFailOverOn();
		RoundRobinFailOverOFF rrfOff = new RoundRobinFailOverOFF();
		ModRouterWithEnabledFailing modFon = new ModRouterWithEnabledFailing();
		ModRouterWithDisabledFailing modfOff = new ModRouterWithDisabledFailing();
		//let's call  customization with illegal argument!

		rrfOff.customize("asd");
		rrFon.customize("asd");
		modfOff.customize("asd");
		modFon.customize("asd");

		ClientSideCallContext callContext = new ClientSideCallContext("Service_default", METHOD1, Arrays.asList("11"));

		Assertions.assertEquals(FailDecision.fail().getReaction(), rrfOff.callFailed(callContext).getReaction(), "rrFoff - failing  bahaviour failed!");
		Assertions.assertEquals(FailDecision.fail().getReaction(), rrFon.callFailed(callContext).getReaction(), "rrFon - failing  bahaviour failed!");
		Assertions.assertEquals(FailDecision.fail().getReaction(), modfOff.callFailed(callContext).getReaction(), "modfOff - failing  bahaviour failed!");
		Assertions.assertEquals(FailDecision.fail().getReaction(), modFon.callFailed(callContext).getReaction(), "modFon - failing  bahaviour failed!");

	}

	//this test doesn't work with the new logic.
	@Test @Disabled
	public void testFailingWith3Nodes() {
		RoundRobinFailOverOn rrFon = new RoundRobinFailOverOn();
		RoundRobinFailOverOFF rrfOff = new RoundRobinFailOverOFF();
		ModRouterWithEnabledFailing modFon = new ModRouterWithEnabledFailing();
		ModRouterWithDisabledFailing modfOff = new ModRouterWithDisabledFailing();
		//let's call  customization with illegal argument!

		rrfOff.customize("3");
		rrFon.customize("3");
		modfOff.customize("3");
		modFon.customize("3");


		ClientSideCallContext callContext = new ClientSideCallContext("Service_0", METHOD1, Arrays.asList("11"));
		callContext.setCallCount(0);

		Assertions.assertEquals(FailDecision.fail().getReaction(), rrfOff.callFailed(callContext).getReaction(), "rrFoff - failing  bahaviour failed! FAILING disabled at all!");
		Assertions.assertEquals(FailDecision.fail().getReaction(), modfOff.callFailed(callContext).getReaction(), "modfOff - failing  bahaviour failed! Failing disabled at all");


		Assertions.assertEquals(FailDecision.retry().getReaction(), rrFon.callFailed(callContext).getReaction(), "rrFon - failing  bahaviour failed!");
		Assertions.assertEquals(FailDecision.retry().getReaction(), modFon.callFailed(callContext).getReaction(), "modFon - failing  bahaviour failed!");


		callContext.setCallCount(1);
		Assertions.assertEquals(FailDecision.fail().getReaction(), rrfOff.callFailed(callContext).getReaction(), "rrFoff - failing  bahaviour failed! FAILING disabled at all!");
		Assertions.assertEquals(FailDecision.fail().getReaction(), modfOff.callFailed(callContext).getReaction(), "modfOff - failing  bahaviour failed! Failing disabled at all");


		Assertions.assertEquals(FailDecision.retry().getReaction(), rrFon.callFailed(callContext).getReaction(), "rrFon - failing  bahaviour failed!");
		Assertions.assertEquals("Service_1", rrFon.getServiceIdForCall(callContext), "rrFON should return Service_1");

		Assertions.assertEquals(FailDecision.retry().getReaction(), modFon.callFailed(callContext).getReaction(), "modFon - failing  bahaviour failed!");
		Assertions.assertEquals("Service_1", modFon.getServiceIdForCall(callContext), "rrFON should return Service_1");

		//increase fail counter!
		callContext.setCallCount(2);
		Assertions.assertEquals(FailDecision.fail().getReaction(), rrFon.callFailed(callContext).getReaction(), "rrFon - failing  bahaviour failed!");
		Assertions.assertEquals(FailDecision.fail().getReaction(), modFon.callFailed(callContext).getReaction(), "modFon - failing  bahaviour failed!");
	}


	@Test
	public void testModRoutingOn3Nodes() {

		ModRouterWithEnabledFailing modFon = new ModRouterWithEnabledFailing();
		ModRouterWithDisabledFailing modfOff = new ModRouterWithDisabledFailing();
		modFon.customize("3");
		modfOff.customize("3");


		for (int i = 0; i < 50; i++) {
			ClientSideCallContext callContext = new ClientSideCallContext("Service", METHOD1, Arrays.asList(i));

			String serviceId1 = modfOff.getServiceIdForCall(callContext);
			String serviceId2 = modFon.getServiceIdForCall(callContext);
			Assertions.assertTrue(getNodeId(serviceId1) >= 0 && getNodeId(serviceId1) <= 2, "Should be in range from 0 to 2");
			Assertions.assertTrue(getNodeId(serviceId2) >= 0 && getNodeId(serviceId2) <= 2, "Should be in range from 0 to 2");
			int mod = i % 3;
			Assertions.assertEquals("Service_" + mod, serviceId1, "Should be equal - to calculated mod! - ");
			Assertions.assertEquals("Service_" + mod, serviceId2, "Should be equal - to calculated mod!");

		}

		// checking NOT  mod routable methods!!!

		int prevNodeId1 = -1;
		int prevNodeId2 = -1;

		//METHOD2 is not routable by mod! ! So let's expect some  RR - based result!
		ClientSideCallContext callContext = new ClientSideCallContext("Service", METHOD2, Arrays.asList("11"));
		for (int i = 0; i < 100; i++) {
			String serviceId1 = modfOff.getServiceIdForCall(callContext);
			String serviceId2 = modFon.getServiceIdForCall(callContext);
			Assertions.assertTrue(getNodeId(serviceId1) >= 0 && getNodeId(serviceId1) <= 2, "Should be in range from 0 to 2");
			Assertions.assertTrue(getNodeId(serviceId2) >= 0 && getNodeId(serviceId2) <= 2, "Should be in range from 0 to 2");

			if (prevNodeId1 != -1) {
				int current = getNodeId(serviceId1);
				Assertions.assertEquals(prevNodeId1 == 2 ? 0 : prevNodeId1 + 1, current, "Should be same number - prevNode1=[" + prevNodeId1 + "], step : " + i + "");
			}
			prevNodeId1 = getNodeId(serviceId1);

			if (prevNodeId2 != -1) {
				int current = getNodeId(serviceId1);
				Assertions.assertEquals(prevNodeId2 == 2 ? 0 : prevNodeId2 + 1, current, "Should be same number - prevNode2=[" + prevNodeId2 + "], step :" + i + "");
			}
			prevNodeId2 = getNodeId(serviceId2);


		}

	}

	@Test
	public void testRRRoutingOn3Nodes() {

		RoundRobinFailOverOn rrFon = new RoundRobinFailOverOn();
		RoundRobinFailOverOFF rrfOff = new RoundRobinFailOverOFF();
		rrfOff.customize("3");
		rrFon.customize("3");
		ClientSideCallContext callContext = new ClientSideCallContext("Service", METHOD1, Arrays.asList("11"));

		int prevNodeId1 = -1;
		int prevNodeId2 = -1;
		for (int i = 0; i < 100; i++) {
			String serviceId1 = rrFon.getServiceIdForCall(callContext);
			String serviceId2 = rrfOff.getServiceIdForCall(callContext);
			Assertions.assertTrue(getNodeId(serviceId1) >= 0 && getNodeId(serviceId1) <= 2, "Should be in range from 0 to 2");
			Assertions.assertTrue(getNodeId(serviceId2) >= 0 && getNodeId(serviceId2) <= 2, "Should be in range from 0 to 2");

			if (prevNodeId1 != -1) {
				int current = getNodeId(serviceId1);
				Assertions.assertEquals(prevNodeId1 == 2 ? 0 : prevNodeId1 + 1, current, "Should be same number - prevNode1=[" + prevNodeId1 + "], step : " + i + "");
			}
			prevNodeId1 = getNodeId(serviceId1);

			if (prevNodeId2 != -1) {
				int current = getNodeId(serviceId1);
				Assertions.assertEquals(prevNodeId2 == 2 ? 0 : prevNodeId2 + 1, current, "Should be same number - prevNode2=[" + prevNodeId2 + "], step :" + i + "");
			}
			prevNodeId2 = getNodeId(serviceId2);


		}
	}


	/**
	 * Return node number!
	 *
	 * @param serviceId some service id - to which call will be delegated
	 * @return int
	 */
	private int getNodeId(String serviceId) {
		int lastUnderscore = serviceId.lastIndexOf("_");
		try {
			return Integer.parseInt(serviceId.substring(lastUnderscore + 1));
		} catch (NumberFormatException e) {
			Assertions.fail("Unexpected error occurred " + e.getMessage());
		}
		return 0;
	}


	@Test
	public void failuresTest() {
		RoundRobinFailOverOFF testRR = new RoundRobinFailOverOFF();
		try {
			testRR.customize("-100");
			Assertions.fail("Not positive value passe to customise!!!");
		} catch (Error e) {
			Assertions.assertTrue(e instanceof AssertionError);
		}

		ModRouterWithDisabledFailing testMM = new ModRouterWithDisabledFailing();
		testMM.customize("2");
		try {
			testMM.getServiceIdForCall(new ClientSideCallContext("SS", METHOD1, null));
			Assertions.fail("Illegal argument! Can't  mod route - without Incoming parameters for MOD counting");
		} catch (Error e) {
			Assertions.assertTrue(e instanceof AssertionError);
		}

		try {
			testMM.getServiceIdForCall(new ClientSideCallContext("SS", METHOD1, new ArrayList<Object>()));
			Assertions.fail("Illegal argument! Modabe parameters are BLANK!");
		} catch (Error e) {
			Assertions.assertTrue(e instanceof AssertionError);
		}
	}


	/**
	 * RR - with enabled fail over.
	 */
	private static class RoundRobinFailOverOn extends AbstractRouterWithFailOverToNextNode {
		@Override
		protected boolean failingSupported() {
			return true;
		}

		@Override
		protected RouterStrategy getStrategy() {
			return RouterStrategy.RR_ROUTER;
		}

		@Override
		protected long getModableValue(Object parameter) {
			return 0l; // not required for RR routing
		}
	}

	/**
	 * RR - with disabled fail over.
	 */
	private static class RoundRobinFailOverOFF extends AbstractRouterWithFailOverToNextNode {
		@Override
		protected boolean failingSupported() {
			return false;
		}

		@Override
		protected RouterStrategy getStrategy() {
			return RouterStrategy.RR_ROUTER;
		}

		@Override
		protected long getModableValue(Object parameter) {
			return 0l; // not required for RR routing
		}
	}


	/**
	 * Mod router with enabled failing.
	 */
	private static class ModRouterWithEnabledFailing extends AbstractParameterBasedModRouterWithFailOverToNextNode {
		/**
		 * Constructor - which add to registry some  methods which should be modable routed.
		 */
		public ModRouterWithEnabledFailing() {
			super();
			addModRoutedMethod("method1");
		}

		@Override
		protected long getModableValue(Object parameter) {
			if (parameter instanceof String)
				return Long.valueOf(String.class.cast(parameter));

			if (parameter instanceof Integer)
				return Integer.class.cast(parameter);

			if (parameter instanceof Long)
				return Long.class.cast(parameter);

			throw new AssertionError("NOT supported parameter " + parameter);
		}
	}


	/**
	 * Mod router with enabled failing.
	 */
	private static class ModRouterWithDisabledFailing extends AbstractParameterBasedModRouterWithFailOverToNextNode {

		/**
		 * Constructor - which add to registry some  methods which should be modable routed.
		 */
		public ModRouterWithDisabledFailing() {
			super();
			addModRoutedMethod("method1");
		}


		@Override // disable  failing by this!
		protected boolean failingSupported() {
			return false;
		}


		@Override
		protected long getModableValue(Object parameter) {
			if (parameter instanceof String)
				return Long.valueOf(String.class.cast(parameter));

			if (parameter instanceof Integer)
				return Integer.class.cast(parameter);

			if (parameter instanceof Long)
				return Long.class.cast(parameter);

			throw new AssertionError("NOT supported parameter " + parameter);
		}
	}


}
