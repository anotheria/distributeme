package org.distributeme.test.asynch;

import org.distributeme.test.asynch.generated.AsynchTestService;
import org.distributeme.test.asynch.generated.AsynchTestServiceStub;
import org.distributeme.test.asynch.generated.RemoteTestServiceStub;

public class SimpleClient {
	public static void main(String[] args) {
		AsynchTestService asynchTestService = new AsynchTestServiceStub();
		TestService synchTestService = new RemoteTestServiceStub();
		//prepare service for work.
		synchTestService.ping(System.currentTimeMillis());
		asynchTestService.ping(System.currentTimeMillis());
		System.out.println("Test Asynchron ");
		testException(asynchTestService);
		asynchTestService.shutdown();
		System.out.println("Test Synchron ");
		testException(synchTestService);
	}
	
	private static final void test(TestService testService){
		long start = System.currentTimeMillis();
		long random = -1;
		try{
			 random = testService.sleepAndReturnRandom(1000);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			long end = System.currentTimeMillis();
			System.out.println("Service replied "+random+" in "+(end-start)+" ms");
		}

	}

	private static final void testSleep(TestService testService){
		long start = System.currentTimeMillis();
		try{
			testService.sleep(3000);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			long end = System.currentTimeMillis();
			System.out.println("Service replied in "+(end-start)+" ms");
		}

	}

	private static final void testException(TestService testService){
		long start = System.currentTimeMillis();
		try{
			 testService.sleepAndThrowTypedException(1000);
		}catch(TestServiceException e){
			long end = System.currentTimeMillis();
			System.out.println("Service replied in "+(end-start)+" ms ---- "+e.getMessage());
		}

	}
	private static final void testException2(TestService testService){
		long start = System.currentTimeMillis();
		try{
			 testService.sleepAndThrowRuntimeException(50);
		}catch(RuntimeException e){
			long end = System.currentTimeMillis();
			System.out.println("Service replied in "+(end-start)+" ms ---- "+e.getMessage());
		}

	}

}
