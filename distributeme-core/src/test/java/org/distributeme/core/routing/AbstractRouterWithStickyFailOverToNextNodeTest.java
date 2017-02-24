package org.distributeme.core.routing;

import org.distributeme.core.routing.blacklisting.DefaultBlacklistingStrategy;
import org.distributeme.core.routing.blacklisting.NoOpBlacklistingStrategy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


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

	private static class Router extends AbstractRouterWithStickyFailOverToNextNode {

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
	}

}