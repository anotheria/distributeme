package org.distributeme.test.list;

import org.distributeme.core.routing.RoundRobinRouterWithStickyFailoverToNextNode;
import org.distributeme.core.routing.RouterStrategy;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 22.09.15 00:53
 */
public class ListRouter extends RoundRobinRouterWithStickyFailoverToNextNode /*RoundRobinRouterWithFailoverToNextNode*/ {
	public ListRouter(){
		addModRoutedMethod("getListObject", 0);
	}

	@Override
	protected RouterStrategy getStrategy() {
		return RouterStrategy.MOD_ROUTER;
	}

	@Override
	protected long getModableValue(Object parameter) {
		if (parameter instanceof ListObjectId)
			return Long.parseLong(((ListObjectId)parameter).getPrimary());
		return super.getModableValue(parameter);
	}
}
