package org.distributeme.core.concurrencycontrol;

import org.junit.Test;

/**
 * Unlimited ConcurrencyControlStrategy should never produce an error, therefore we always expect it to run (parameter false in all calls to utility).
 * @author lrosenberg
 *
 */
public class UnlimitedConcurrencyStrategyTest {
	@Test public void testUnderLimitClientSide() throws InterruptedException{
		UnlimitedConcurrencyStrategy strategy = new UnlimitedConcurrencyStrategy();
		strategy.customize("5");
		ConcurrencyControlStrategyTestUtil.testClientSide(strategy, false, 4);
	}
	@Test public void testOverLimitClientSide()throws InterruptedException{
		UnlimitedConcurrencyStrategy strategy = new UnlimitedConcurrencyStrategy();
		strategy.customize("5");
		ConcurrencyControlStrategyTestUtil.testClientSide(strategy, false, 6);
	}
	@Test public void testUnderLimitServerSide()throws InterruptedException{
		UnlimitedConcurrencyStrategy strategy = new UnlimitedConcurrencyStrategy();
		strategy.customize("5");
		ConcurrencyControlStrategyTestUtil.testServerSide(strategy, false, 4);
		
	}
	@Test public void testOverLimitServerSide()throws InterruptedException{
		UnlimitedConcurrencyStrategy strategy = new UnlimitedConcurrencyStrategy();
		strategy.customize("5");
		ConcurrencyControlStrategyTestUtil.testServerSide(strategy, false, 6);
	}

}
 