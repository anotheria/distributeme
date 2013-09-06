package org.distributeme.core.routing;

/**
 * Abstract implementation of MOD  strategy router which supports FailOver to next cluster node - strategy.
 *
 * @author h3llka
 */
public abstract class AbstractParameterBasedModRouterWithFailOverToNextNode extends AbstractRouterWithFailOverToNextNode {

	@Override
	protected boolean failingSupported() {
		return true;
	}

	@Override
	protected RouterStrategy getStrategy() {
		return RouterStrategy.MOD_ROUTER;
	}
}
