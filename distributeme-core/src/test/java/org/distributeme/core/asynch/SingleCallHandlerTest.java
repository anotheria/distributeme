package org.distributeme.core.asynch;

import org.junit.Test;
import static org.junit.Assert.*;

public class SingleCallHandlerTest {
	@Test public void testImmediateSuccess() throws InterruptedException{
		System.out.println("testImmediateSuccess");
		long startTime = System.currentTimeMillis();
		SingleCallHandler handler = new SingleCallHandler();
		TestRunner runner = new TestRunner(handler, null, CallType.SUC, 0);
		Thread t = new Thread(runner);
		t.start();
		handler.waitForResults(1000);
		long duration = System.currentTimeMillis() - startTime;
		assertTrue(duration<100);
		System.out.println("duration "+duration);
		assertTrue(handler.isFinished());
	}
	
	@Test public void testTimeoutSuccess() throws InterruptedException{
		System.out.println("testTimeoutSuccess");
		long startTime = System.currentTimeMillis();
		SingleCallHandler handler = new SingleCallHandler();
		TestRunner runner = new TestRunner(handler, null, CallType.SUC, 2000);
		Thread t = new Thread(runner);
		t.start();
		handler.waitForResults(1000);
		long duration = System.currentTimeMillis() - startTime;
		assertTrue(duration>100);
		assertTrue(duration<1100);
		System.out.println("duration "+duration);
		assertFalse(handler.isFinished());
	}
}
