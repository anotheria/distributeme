package org.distributeme.core.asynch;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MultiCallCollectorTest {
	@Test public void testForDuplicateIds(){
		MultiCallCollector executor = new MultiCallCollector(3);
		executor.createSubCallHandler("a");
		try{
			executor.createSubCallHandler("a");
			fail("IllegalArgumentException expected");
		}catch(IllegalArgumentException e){
		}
	}
	
	@Test public void testForSize(){
		MultiCallCollector executor = new MultiCallCollector(3);
		executor.createSubCallHandler("a");
		executor.createSubCallHandler("b");
		executor.createSubCallHandler("c");
		try{
			executor.createSubCallHandler("d");
			fail("IllegalStateException expected");
		}catch(IllegalStateException e){
		}
	}

	@Test public void testImmediateSuccess() throws InterruptedException{
		//System.out.println("testImmediateSuccess");
		long startTime = System.currentTimeMillis();
		MultiCallCollector executor = new MultiCallCollector(3);
		for (int i=0; i<3; i++){
			CallBackHandler handler = executor.createSubCallHandler(""+i);
			TestRunner runner = new TestRunner(handler, null, CallType.SUC, 0);
			Thread t = new Thread(runner);
			t.start();
		}
		executor.waitForResults(1000);
		long duration = System.currentTimeMillis() - startTime;
		assertTrue(duration<100);
		//System.out.println("duration "+duration);
		assertTrue(executor.isFinished());
	}

	@Test public void testTimeoutSuccess() throws InterruptedException{
		long startTime = System.currentTimeMillis();
		MultiCallCollector executor = new MultiCallCollector(3);
		for (int i=0; i<3; i++){
			CallBackHandler handler = executor.createSubCallHandler(""+i);
			TestRunner runner = new TestRunner(handler, null, CallType.SUC, 2000);
			Thread t = new Thread(runner);
			t.start();
		}
		executor.waitForResults(1000);
		long duration = System.currentTimeMillis() - startTime;
		assertTrue(duration>100);
		assertTrue(duration<1100);
		assertFalse(executor.isFinished());
	}

}
