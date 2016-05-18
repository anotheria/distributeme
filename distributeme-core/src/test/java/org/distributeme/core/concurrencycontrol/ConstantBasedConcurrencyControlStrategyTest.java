package org.distributeme.core.concurrencycontrol;

import org.junit.Test;

public class ConstantBasedConcurrencyControlStrategyTest {
	@Test public void testUnderLimitClientSide() throws InterruptedException{
		ConstantBasedConcurrencyControlStrategy strategy = new ConstantBasedConcurrencyControlStrategy();
		strategy.customize("5");
		ConcurrencyControlStrategyTestUtil.testClientSide(strategy, false, 4);
	}
	@Test public void testOverLimitClientSide()throws InterruptedException{
		ConstantBasedConcurrencyControlStrategy strategy = new ConstantBasedConcurrencyControlStrategy();
		strategy.customize("5");
		ConcurrencyControlStrategyTestUtil.testClientSide(strategy, true, 6);
	}
	@Test public void testUnderLimitServerSide()throws InterruptedException{
		ConstantBasedConcurrencyControlStrategy strategy = new ConstantBasedConcurrencyControlStrategy();
		strategy.customize("0,5");
		ConcurrencyControlStrategyTestUtil.testServerSide(strategy, false, 4);
		
	}
	@Test public void testOverLimitServerSide()throws InterruptedException{
		ConstantBasedConcurrencyControlStrategy strategy = new ConstantBasedConcurrencyControlStrategy();
		strategy.customize("0,5");
		ConcurrencyControlStrategyTestUtil.testServerSide(strategy, true, 6);
	}
	
	
	
}
 