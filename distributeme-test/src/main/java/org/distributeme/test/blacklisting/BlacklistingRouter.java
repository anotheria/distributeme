package org.distributeme.test.blacklisting;

import org.distributeme.core.routing.AbstractRouterWithStickyFailOverToNextNode;
import org.distributeme.core.routing.RouterStrategy;


public class BlacklistingRouter extends AbstractRouterWithStickyFailOverToNextNode {

	@Override
	protected boolean failingSupported() {
		return true;
	}

	@Override
	protected RouterStrategy getStrategy() {
		return RouterStrategy.MOD_ROUTER;
	}

	@Override
	protected long getModableValue(Object parameter) {
		return ((Integer)parameter);
	}
}
