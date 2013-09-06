package org.distributeme.core.concurrencycontrol;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.ServerSideCallContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;

public class ConcurrencyControlStrategyTestUtil {

	private static Logger log = LoggerFactory.getLogger(ConcurrencyControlStrategyTestUtil.class);


	static void testClientSide(final ConcurrencyControlStrategy strategy, boolean expectError, int numberOfThreads) throws InterruptedException{
//		System.out.println("Testing "+strategy+" errorExpected: "+expectError+" in "+numberOfThreads+" threads");
		
		final CountDownLatch prepare = new CountDownLatch(numberOfThreads);
		final CountDownLatch start = new CountDownLatch(1);
		final CountDownLatch lapFinish = new CountDownLatch(numberOfThreads);
		
		final CountDownLatch lapStart = new CountDownLatch(1);
		final CountDownLatch testFinish = new CountDownLatch(numberOfThreads);

		final ClientSideCallContext context = new ClientSideCallContext("blub");
		final AtomicBoolean result = new AtomicBoolean(false);
		
		for (int i=0; i<numberOfThreads; i++){
			Thread t = new Thread(){
				@Override
				public void run() {
					try{
						prepare.countDown();
//						System.out.println("STARTING");
						start.await();
//						System.out.println("RUNNING");
						strategy.notifyClientSideCallStarted(context);
						lapFinish.countDown();
						lapStart.await();
						strategy.notifyClientSideCallFinished(context);
						testFinish.countDown();
					}catch(InterruptedException e){
						e.printStackTrace();
					}catch(OutgoingRequestRefusedException e){
						result.set(true);
						lapFinish.countDown();
						try{
							lapStart.await();
						}catch(InterruptedException ignore){}
						testFinish.countDown();
					}
				}
			};
			t.start();
		
		}
		prepare.await();
//		System.out.println("TEST PREPARED");
		start.countDown();
//		System.out.println("TEST STARTED");
		lapFinish.await();
//		System.out.println("TEST LAP FINISHED");
		lapStart.countDown();
//		System.out.println("TEST LAP 2 is started");
		testFinish.await();
//		System.out.println("TEST is totally finished");
		assertEquals("Expected error should correspond with real error (expected, real)=("+expectError+", "+result.get()+")", expectError, result.get());
	}
	
	static void testServerSide(final ConcurrencyControlStrategy strategy, boolean expectError, int numberOfThreads) throws InterruptedException{
//		System.out.println("Testing "+strategy+" errorExpected: "+expectError+" in "+numberOfThreads+" threads");
		
		final CountDownLatch prepare = new CountDownLatch(numberOfThreads);
		final CountDownLatch start = new CountDownLatch(1);
		final CountDownLatch lapFinish = new CountDownLatch(numberOfThreads);
		
		final CountDownLatch lapStart = new CountDownLatch(1);
		final CountDownLatch testFinish = new CountDownLatch(numberOfThreads);

		final ServerSideCallContext context = new ServerSideCallContext("blub", new HashMap());
		final AtomicBoolean result = new AtomicBoolean(false);
		
		for (int i=0; i<numberOfThreads; i++){
			Thread t = new Thread(){
				@Override
				public void run() {
					try{
						prepare.countDown();
//						System.out.println("STARTING");
						start.await();
//						System.out.println("RUNNING");
						strategy.notifyServerSideCallStarted(context);
						lapFinish.countDown();
//						System.out.println("LAP FINISHED, WAITING FOR NEXT");
						lapStart.await();
//						System.out.println("NEXT LAP (FINISHED)");
						strategy.notifyServerSideCallFinished(context);
						testFinish.countDown();
//						System.out.println("ALL FINISHED");
					}catch(InterruptedException e){
						log.error("testServerSide", e);
					}catch(ServerRefusedRequestException e){
//						System.out.println("BLUB!");
						result.set(true);
						lapFinish.countDown();
						try{
							lapStart.await();
						}catch(InterruptedException ignore){}
						testFinish.countDown();
					}
				}
			};
			t.start();
		
		}
		prepare.await();
//		System.out.println("---TEST PREPARED");
		start.countDown();
//		System.out.println("---TEST STARTED");
		lapFinish.await();
//		System.out.println("---TEST LAP FINISHED");
		lapStart.countDown();
//		System.out.println("---TEST LAP 2 is started");
		testFinish.await();
//		System.out.println("---TEST is totally finished");
		assertEquals("Expected error should correspond with real error (expected, real)=("+expectError+", "+result.get()+")", expectError, result.get());
	}
	

}
