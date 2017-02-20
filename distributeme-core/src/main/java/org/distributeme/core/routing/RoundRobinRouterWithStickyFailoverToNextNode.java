package org.distributeme.core.routing;

/**
 * This router sends each call to another instance. It is useful if you want to cluster a service.
 * In case of a failure it will resend the request to another instance in the cluster. As long as one service instance remains operational the whole cluster works.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class RoundRobinRouterWithStickyFailoverToNextNode extends AbstractRouterWithStickyFailOverToNextNode {

	/** {@inheritDoc} */
	@Override
	protected boolean failingSupported() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected RouterStrategy getStrategy() {
		return RouterStrategy.RR_ROUTER;
	}

	/** {@inheritDoc} */
	@Override
	protected long getModableValue(Object parameter) {
		throw new UnsupportedOperationException("RoundRobinRouter - does not supports getModableValue(" + parameter + ") method");
	}
}
