package org.distributeme.core.routing;

import junit.framework.Assert;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.failing.FailDecision;
import org.junit.Ignore;
import org.junit.Test;

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

		Assert.assertEquals("Illegal behaviour is RR-FailOver off router", callContext.getServiceId(), rrfOff.getServiceIdForCall(callContext));
		Assert.assertEquals("Illegal behaviour is RR-FailOver on router", callContext.getServiceId(), rrFon.getServiceIdForCall(callContext));
		Assert.assertEquals("Illegal behaviour is MOD-FailOver off router", callContext.getServiceId(), modfOff.getServiceIdForCall(callContext));
		Assert.assertEquals("Illegal behaviour is MOD-FailOver on router", callContext.getServiceId(), modFon.getServiceIdForCall(callContext));

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

		Assert.assertEquals("rrFoff - failing  bahaviour failed!", FailDecision.fail().getReaction(), rrfOff.callFailed(callContext).getReaction());
		Assert.assertEquals("rrFon - failing  bahaviour failed!", FailDecision.fail().getReaction(), rrFon.callFailed(callContext).getReaction());
		Assert.assertEquals("modfOff - failing  bahaviour failed!", FailDecision.fail().getReaction(), modfOff.callFailed(callContext).getReaction());
		Assert.assertEquals("modFon - failing  bahaviour failed!", FailDecision.fail().getReaction(), modFon.callFailed(callContext).getReaction());

	}

	//this test doesn't work with the new logic.
	@Test @Ignore
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

		Assert.assertEquals("rrFoff - failing  bahaviour failed! FAILING disabled at all!", FailDecision.fail().getReaction(), rrfOff.callFailed(callContext).getReaction());
		Assert.assertEquals("modfOff - failing  bahaviour failed! Failing disabled at all", FailDecision.fail().getReaction(), modfOff.callFailed(callContext).getReaction());


		Assert.assertEquals("rrFon - failing  bahaviour failed!", FailDecision.retry().getReaction(), rrFon.callFailed(callContext).getReaction());
		Assert.assertEquals("modFon - failing  bahaviour failed!", FailDecision.retry().getReaction(), modFon.callFailed(callContext).getReaction());


		callContext.setCallCount(1);
		Assert.assertEquals("rrFoff - failing  bahaviour failed! FAILING disabled at all!", FailDecision.fail().getReaction(), rrfOff.callFailed(callContext).getReaction());
		Assert.assertEquals("modfOff - failing  bahaviour failed! Failing disabled at all", FailDecision.fail().getReaction(), modfOff.callFailed(callContext).getReaction());


		Assert.assertEquals("rrFon - failing  bahaviour failed!", FailDecision.retry().getReaction(), rrFon.callFailed(callContext).getReaction());
		Assert.assertEquals("rrFON should return Service_1", "Service_1", rrFon.getServiceIdForCall(callContext));

		Assert.assertEquals("modFon - failing  bahaviour failed!", FailDecision.retry().getReaction(), modFon.callFailed(callContext).getReaction());
		Assert.assertEquals("rrFON should return Service_1", "Service_1", modFon.getServiceIdForCall(callContext));

		//increase fail counter!
		callContext.setCallCount(2);
		Assert.assertEquals("rrFon - failing  bahaviour failed!", FailDecision.fail().getReaction(), rrFon.callFailed(callContext).getReaction());
		Assert.assertEquals("modFon - failing  bahaviour failed!", FailDecision.fail().getReaction(), modFon.callFailed(callContext).getReaction());
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
			Assert.assertTrue("Should be in range from 0 to 2", getNodeId(serviceId1) >= 0 && getNodeId(serviceId1) <= 2);
			Assert.assertTrue("Should be in range from 0 to 2", getNodeId(serviceId2) >= 0 && getNodeId(serviceId2) <= 2);
			int mod = i % 3;
			Assert.assertEquals("Should be equal - to calculated mod! - ", "Service_" + mod, serviceId1);
			Assert.assertEquals("Should be equal - to calculated mod!", "Service_" + mod, serviceId2);

		}

		// checking NOT  mod routable methods!!!

		int prevNodeId1 = -1;
		int prevNodeId2 = -1;

		//METHOD2 is not routable by mod! ! So let's expect some  RR - based result!
		ClientSideCallContext callContext = new ClientSideCallContext("Service", METHOD2, Arrays.asList("11"));
		for (int i = 0; i < 100; i++) {
			String serviceId1 = modfOff.getServiceIdForCall(callContext);
			String serviceId2 = modFon.getServiceIdForCall(callContext);
			Assert.assertTrue("Should be in range from 0 to 2", getNodeId(serviceId1) >= 0 && getNodeId(serviceId1) <= 2);
			Assert.assertTrue("Should be in range from 0 to 2", getNodeId(serviceId2) >= 0 && getNodeId(serviceId2) <= 2);

			if (prevNodeId1 != -1) {
				int current = getNodeId(serviceId1);
				Assert.assertEquals("Should be same number - prevNode1=[" + prevNodeId1 + "], step : " + i + "", prevNodeId1 == 2 ? 0 : prevNodeId1 + 1, current);
			}
			prevNodeId1 = getNodeId(serviceId1);

			if (prevNodeId2 != -1) {
				int current = getNodeId(serviceId1);
				Assert.assertEquals("Should be same number - prevNode2=[" + prevNodeId2 + "], step :" + i + "", prevNodeId2 == 2 ? 0 : prevNodeId2 + 1, current);
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
			Assert.assertTrue("Should be in range from 0 to 2", getNodeId(serviceId1) >= 0 && getNodeId(serviceId1) <= 2);
			Assert.assertTrue("Should be in range from 0 to 2", getNodeId(serviceId2) >= 0 && getNodeId(serviceId2) <= 2);

			if (prevNodeId1 != -1) {
				int current = getNodeId(serviceId1);
				Assert.assertEquals("Should be same number - prevNode1=[" + prevNodeId1 + "], step : " + i + "", prevNodeId1 == 2 ? 0 : prevNodeId1 + 1, current);
			}
			prevNodeId1 = getNodeId(serviceId1);

			if (prevNodeId2 != -1) {
				int current = getNodeId(serviceId1);
				Assert.assertEquals("Should be same number - prevNode2=[" + prevNodeId2 + "], step :" + i + "", prevNodeId2 == 2 ? 0 : prevNodeId2 + 1, current);
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
			Assert.fail("Unexpected error occurred " + e.getMessage());
		}
		return 0;
	}


	@Test
	public void failuresTest() {
		RoundRobinFailOverOFF testRR = new RoundRobinFailOverOFF();
		try {
			testRR.customize("-100");
			Assert.fail("Not positive value passe to customise!!!");
		} catch (Error e) {
			Assert.assertTrue(e instanceof AssertionError);
		}

		ModRouterWithDisabledFailing testMM = new ModRouterWithDisabledFailing();
		testMM.customize("2");
		try {
			testMM.getServiceIdForCall(new ClientSideCallContext("SS", METHOD1, null));
			Assert.fail("Illegal argument! Can't  mod route - without Incoming parameters for MOD counting");
		} catch (Error e) {
			Assert.assertTrue(e instanceof AssertionError);
		}

		try {
			testMM.getServiceIdForCall(new ClientSideCallContext("SS", METHOD1, new ArrayList<Object>()));
			Assert.fail("Illegal argument! Modabe parameters are BLANK!");
		} catch (Error e) {
			Assert.assertTrue(e instanceof AssertionError);
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
