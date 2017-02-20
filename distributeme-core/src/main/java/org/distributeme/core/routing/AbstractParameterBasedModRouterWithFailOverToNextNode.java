package org.distributeme.core.routing;

/**
 * Abstract implementation of MOD  strategy router which supports FailOver to next cluster node - strategy.
 *
 * @author h3llka
 * @version $Id: $Id
 */
public abstract class AbstractParameterBasedModRouterWithFailOverToNextNode extends AbstractRouterWithFailOverToNextNode {

	/** {@inheritDoc} */
	@Override
	protected boolean failingSupported() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected RouterStrategy getStrategy() {
		return RouterStrategy.MOD_ROUTER;
	}
}
