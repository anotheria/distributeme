package org.distributeme.test.concurrencycontrol;

import org.distributeme.core.ServiceLocator;
import org.distributeme.core.exception.DistributemeRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class TestRun {
	public static final int THREADS = 10;
	private static final CountDownLatch ready = new CountDownLatch(THREADS);
	private static final CountDownLatch start = new CountDownLatch(1);
	private static final CountDownLatch finish = new CountDownLatch(THREADS);
	
	static long tsuccesses, ttotals;

	public static void test(final int limit, final Method method) throws Exception{
		
		tsuccesses = ttotals = 0;
		System.out.println("STARTING "+limit+ ' ' +method);
		final TestService service = ServiceLocator.getRemote(TestService.class);
		
		for (int i=0; i<THREADS; i++){
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					Random rnd = new Random(System.currentTimeMillis());
					try{
						ready.countDown();
						start.await();
						int tries = 0, successes = 0;
						for (int i=0; i<limit; i++){
							tries++;
							try{
								method.invoke(service, System.currentTimeMillis());
								//service.serverSideLimited(System.currentTimeMillis());
								//service.clientSideLimited(System.currentTimeMillis());
								successes++;
							}catch(DistributemeRuntimeException | InvocationTargetException e){
								
							}
                            try{
								Thread.sleep(rnd.nextInt(20));
							}catch(Exception ignored){}
						}
						System.out.println("Thread "+this+" finished : tries: "+tries+" successes: "+successes);
						tsuccesses+=successes; ttotals+=tries;
						finish.countDown();
					}catch(Exception ignored){
						ignored.printStackTrace();
					}
				}
			});
			t.start();
		}
		
		System.out.println("Waiting for ready");
		ready.await();
		System.out.println("STARTING");
		start.countDown();
		finish.await();
		System.out.println("FINISHED "+tsuccesses+ '/' +ttotals+ " - "+(tsuccesses*100/ttotals)+" %.");
		service.printAndResetStats();
		
	}

}