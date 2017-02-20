package org.distributeme.core.asynch;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SingleCallHandlerTest {
	@Test public void testImmediateSuccess() throws InterruptedException{
		long startTime = System.currentTimeMillis();
		SingleCallHandler handler = new SingleCallHandler();
		TestRunner runner = new TestRunner(handler, null, CallType.SUC, 0);
		Thread t = new Thread(runner);
		t.start();
		handler.waitForResults(1000);
		long duration = System.currentTimeMillis() - startTime;
		assertTrue(duration<100);
		assertTrue(handler.isFinished());
	}
	
	@Test public void testTimeoutSuccess() throws InterruptedException{
		long startTime = System.currentTimeMillis();
		SingleCallHandler handler = new SingleCallHandler();
		TestRunner runner = new TestRunner(handler, null, CallType.SUC, 2000);
		Thread t = new Thread(runner);
		t.start();
		handler.waitForResults(1000);
		long duration = System.currentTimeMillis() - startTime;
		assertTrue(duration>100);
		assertTrue(duration<1100);
		assertFalse(handler.isFinished());
	}
}
