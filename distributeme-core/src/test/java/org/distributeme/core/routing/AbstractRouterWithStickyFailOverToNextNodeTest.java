package org.distributeme.core.routing;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.routing.blacklisting.DefaultBlacklistingStrategy;
import org.distributeme.core.routing.blacklisting.NoOpBlacklistingStrategy;
import org.distributeme.core.stats.RoutingStatsCollector;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


/**
 * Created by rboehling on 2/21/17.
 */
public class AbstractRouterWithStickyFailOverToNextNodeTest {

	private AbstractRouterWithStickyFailOverToNextNode routerWithStickyFailOverToNextNode = new Router();

	@Test
	public void useBlacklistingStrategyFromConfiguration() {
		routerWithStickyFailOverToNextNode.setConfigurationName("someServiceId", "blacklisting-routing-test-regular");
		assertTrue("Router not of specified clazz", routerWithStickyFailOverToNextNode.getBlacklistingStrategy() instanceof NoOpBlacklistingStrategy);
	}

	@Test
	public void useDefaultBlacklistingStrategyIfConfigurationFalty() {
		routerWithStickyFailOverToNextNode.setConfigurationName("someServiceId", "blacklisting-routing-test-faulty");
		assertTrue("Router not of specified clazz", routerWithStickyFailOverToNextNode.getBlacklistingStrategy() instanceof DefaultBlacklistingStrategy);
	}

	@Test
	public void useDefaultBlacklistingStrategyIfConfigurationStrategyIsEmpty() {
		routerWithStickyFailOverToNextNode.setConfigurationName("someServiceId", "blacklisting-routing-test-empty");
		assertTrue("Router not of specified clazz", routerWithStickyFailOverToNextNode.getBlacklistingStrategy() instanceof DefaultBlacklistingStrategy);
	}

	@Test
	public void callFailed_CalculatesTheRoutingStatsCorrect() throws Exception {
		routerWithStickyFailOverToNextNode.setConfigurationName("myService", "blacklisting-routing-test-empty");
		String serviceId = "myService";
		ClientSideCallContext clientSideCallContext = new ClientSideCallContext(serviceId, "test", Collections.emptyList());
		routerWithStickyFailOverToNextNode.callFailed(clientSideCallContext);

		RoutingStatsCollectorForTest routingStats = (RoutingStatsCollectorForTest) routerWithStickyFailOverToNextNode.getRoutingStats(serviceId);

		assertThat(routingStats.getFailedCall(), is(1));
		assertThat(routingStats.getFailedDecision(), is(1));
		assertThat(routingStats.getRetryDecision(), is(0));
		assertThat(routingStats.getRequestRoutedTo(), is(0));
		assertThat(routingStats.getBlacklisted(), is(0));
	}

	@Test
	public void getServiceIdForCall_CalculatesTheRoutingStatsCorrect_WithServiceAmountZero() throws Exception {
		String serviceId = "myService";
		ClientSideCallContext clientSideCallContext = new ClientSideCallContext(serviceId, "test", Collections.emptyList());
		String resultServiceId = routerWithStickyFailOverToNextNode.getServiceIdForCall(clientSideCallContext);

		RoutingStatsCollectorForTest routingStats = (RoutingStatsCollectorForTest) routerWithStickyFailOverToNextNode.getRoutingStats(resultServiceId);

		assertThat(resultServiceId, is(serviceId));
		assertThat(routingStats.getFailedCall(), is(0));
		assertThat(routingStats.getFailedDecision(), is(0));
		assertThat(routingStats.getRetryDecision(), is(0));
		assertThat(routingStats.getRequestRoutedTo(), is(1));
		assertThat(routingStats.getBlacklisted(), is(0));
	}

	@Test
	public void getServiceIdForCall_CalculatesTheRoutingStatsCorrect_WithOneService() throws Exception {
		routerWithStickyFailOverToNextNode.setConfigurationName("someServiceId", "blacklisting-routing-test-regular");

		String serviceId = "myService";
		ClientSideCallContext clientSideCallContext = new ClientSideCallContext(serviceId, "test", Collections.emptyList());
		String resultServiceId = routerWithStickyFailOverToNextNode.getServiceIdForCall(clientSideCallContext);

		RoutingStatsCollectorForTest routingStats = (RoutingStatsCollectorForTest) routerWithStickyFailOverToNextNode.getRoutingStats(resultServiceId);

		assertThat(resultServiceId, is(serviceId + "_0"));
		assertThat(routingStats.getFailedCall(), is(0));
		assertThat(routingStats.getFailedDecision(), is(0));
		assertThat(routingStats.getRetryDecision(), is(0));
		assertThat(routingStats.getRequestRoutedTo(), is(1));
		assertThat(routingStats.getBlacklisted(), is(0));
	}

	@Test
	public void getServiceIdForCall_CalculatesTheRoutingStatsCorrect_WithTwoServiceAndFirstIsBlacklistedButRequestGoesToOtherOne() throws Exception {
		routerWithStickyFailOverToNextNode.setConfigurationName("someServiceId", "blacklisting-routing-test-two-services");

		String serviceIdFailed = "myService_0";
		ClientSideCallContext failedContext = new ClientSideCallContext(serviceIdFailed, "test", Collections.emptyList());
		routerWithStickyFailOverToNextNode.callFailed(failedContext);

		String serviceId = "myService";
		ClientSideCallContext clientSideCallContext = new ClientSideCallContext(serviceId, "test", Collections.emptyList());
		String resultServiceId = routerWithStickyFailOverToNextNode.getServiceIdForCall(clientSideCallContext);

		assertThat(resultServiceId, is(serviceId + "_1"));

		RoutingStatsCollectorForTest routingStatsService0 = (RoutingStatsCollectorForTest) routerWithStickyFailOverToNextNode.getRoutingStats(serviceIdFailed);
		assertThat(routingStatsService0.getFailedCall(), is(1));
		assertThat(routingStatsService0.getFailedDecision(), is(1));
		assertThat(routingStatsService0.getRetryDecision(), is(0));
		assertThat(routingStatsService0.getRequestRoutedTo(), is(0));
		assertThat(routingStatsService0.getBlacklisted(), is(0));

		RoutingStatsCollectorForTest routingStatsService1 = (RoutingStatsCollectorForTest) routerWithStickyFailOverToNextNode.getRoutingStats(resultServiceId);
		assertThat(routingStatsService1.getFailedCall(), is(0));
		assertThat(routingStatsService1.getFailedDecision(), is(0));
		assertThat(routingStatsService1.getRetryDecision(), is(0));
		assertThat(routingStatsService1.getRequestRoutedTo(), is(1));
		assertThat(routingStatsService1.getBlacklisted(), is(0));
	}

	@Test
	public void getServiceIdForCall_CalculatesTheRoutingStatsCorrect_WithTwoServiceAndRequestGoesToBlacklistedService() throws Exception {
		routerWithStickyFailOverToNextNode.setConfigurationName("someServiceId", "blacklisting-routing-test-two-services");

		String serviceIdFailed = "myService_1";
		ClientSideCallContext failedContext = new ClientSideCallContext(serviceIdFailed, "test", Collections.emptyList());
		routerWithStickyFailOverToNextNode.callFailed(failedContext);

		String serviceId = "myService";
		ClientSideCallContext clientSideCallContext = new ClientSideCallContext(serviceId, "test", Collections.emptyList());
		String resultServiceId = routerWithStickyFailOverToNextNode.getServiceIdForCall(clientSideCallContext);
		assertThat(resultServiceId, is(serviceId + "_0"));

		RoutingStatsCollectorForTest routingStatsService1 = (RoutingStatsCollectorForTest) routerWithStickyFailOverToNextNode.getRoutingStats(serviceIdFailed);
		assertThat(routingStatsService1.getFailedCall(), is(1));
		assertThat(routingStatsService1.getFailedDecision(), is(1));
		assertThat(routingStatsService1.getRetryDecision(), is(0));
		assertThat(routingStatsService1.getRequestRoutedTo(), is(0));
		assertThat(routingStatsService1.getBlacklisted(), is(1));

		RoutingStatsCollectorForTest routingStatsService0 = (RoutingStatsCollectorForTest) routerWithStickyFailOverToNextNode.getRoutingStats(resultServiceId);
		assertThat(routingStatsService0.getFailedCall(), is(0));
		assertThat(routingStatsService0.getFailedDecision(), is(0));
		assertThat(routingStatsService0.getRetryDecision(), is(0));
		assertThat(routingStatsService0.getRequestRoutedTo(), is(1));
		assertThat(routingStatsService0.getBlacklisted(), is(0));
	}

	@Test @Ignore
	public void getServiceIdForCall_CalculatesTheRoutingStatsCorrect_WithThreeServiceAndRequestGoesToBlacklistedService() throws Exception {
		routerWithStickyFailOverToNextNode.setConfigurationName("someServiceId", "blacklisting-routing-test-three-services");

		String serviceIdFailed1 = "myService_1";
		ClientSideCallContext failedContext1 = new ClientSideCallContext(serviceIdFailed1, "test", Collections.emptyList());
		routerWithStickyFailOverToNextNode.callFailed(failedContext1);

		String serviceIdFailed2 = "myService_2";
		ClientSideCallContext failedContext2 = new ClientSideCallContext(serviceIdFailed2, "test", Collections.emptyList());
		routerWithStickyFailOverToNextNode.callFailed(failedContext2);

		String serviceId = "myService";
		ClientSideCallContext clientSideCallContext = new ClientSideCallContext(serviceId, "test", Collections.emptyList());
		String resultServiceId = routerWithStickyFailOverToNextNode.getServiceIdForCall(clientSideCallContext);
		assertThat(resultServiceId, is(serviceId + "_0"));

		RoutingStatsCollectorForTest routingStatsService1 = (RoutingStatsCollectorForTest) routerWithStickyFailOverToNextNode.getRoutingStats(serviceIdFailed1);
		assertThat(routingStatsService1.getFailedCall(), is(1));
		assertThat(routingStatsService1.getFailedDecision(), is(1));
		assertThat(routingStatsService1.getRetryDecision(), is(0));
		assertThat(routingStatsService1.getRequestRoutedTo(), is(0));
		assertThat(routingStatsService1.getBlacklisted(), is(1));

		RoutingStatsCollectorForTest routingStatsService2 = (RoutingStatsCollectorForTest) routerWithStickyFailOverToNextNode.getRoutingStats(serviceIdFailed2);
		assertThat(routingStatsService2.getFailedCall(), is(1));
		assertThat(routingStatsService2.getFailedDecision(), is(1));
		assertThat(routingStatsService2.getRetryDecision(), is(0));
		assertThat(routingStatsService2.getRequestRoutedTo(), is(0));
		assertThat(routingStatsService2.getBlacklisted(), is(1));

		RoutingStatsCollectorForTest routingStatsService0 = (RoutingStatsCollectorForTest) routerWithStickyFailOverToNextNode.getRoutingStats(resultServiceId);
		assertThat(routingStatsService0.getFailedCall(), is(0));
		assertThat(routingStatsService0.getFailedDecision(), is(0));
		assertThat(routingStatsService0.getRetryDecision(), is(0));
		assertThat(routingStatsService0.getRequestRoutedTo(), is(1));
		assertThat(routingStatsService0.getBlacklisted(), is(0));
	}

	private static class Router extends AbstractRouterWithStickyFailOverToNextNode {

		Map<String, RoutingStatsCollector> statsCollectorMap = new HashMap<>();

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
			return 0;
		}

		@Override
		protected RoutingStatsCollector getRoutingStats(final String serviceId) {
			if (!statsCollectorMap.containsKey(serviceId)) {
				statsCollectorMap.put(serviceId, new RoutingStatsCollectorForTest());
			}
			return statsCollectorMap.get(serviceId);
		}

		@Override
		protected int getRandomInt(final int length) {
			return 1;
		}
	}

}