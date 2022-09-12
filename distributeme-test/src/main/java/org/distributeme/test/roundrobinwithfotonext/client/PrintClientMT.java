package org.distributeme.test.roundrobinwithfotonext.client;

import net.anotheria.util.IdCodeGenerator;
import net.anotheria.util.NumberUtils;
import org.distributeme.core.ServiceLocator;
import org.distributeme.test.roundrobinwithfotonext.RoundRobinService;

import java.util.concurrent.CountDownLatch;

/**
 * This is a test class that shows routing in a multi-threading scenario.
 */
@SuppressWarnings("PMD")
public class PrintClientMT extends Client{

	static int  errors = 0 ; static int replies = 0;

	public static void main(String a[]) throws Exception{
		final RoundRobinService service = ServiceLocator.getRemote(RoundRobinService.class);

		long start = System.currentTimeMillis();

		int threads = 10;
		final int LIMIT = 10;

		final CountDownLatch ready = new CountDownLatch(threads);
		final CountDownLatch go = new CountDownLatch(1);
		final CountDownLatch finish = new CountDownLatch(threads);

		for (int t=0; t<threads; t++){
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					String name = IdCodeGenerator.generateCode(3);
					ready.countDown();
					try {
						go.await();
					}catch(InterruptedException e){}
					for (int i = 0; i<LIMIT; i++){
						try{
							service.print("Hello from "+name+" Nr. "+ NumberUtils.itoa(i, 3));
							replies++;
						}catch(Exception e){
							errors++;
						}
					}
					finish.countDown();

				}
			});
			thread.start();
		}

		System.out.println("Ready for ready");
		ready.await();
		go.countDown();
		finish.await();
		long end = System.currentTimeMillis();
		
		System.out.println("Tries "+LIMIT+", errors: "+errors+", replies: "+replies+", time: "+(end-start)+" ms");
		//now print results
		service.printResults();
		service.printResults();
		service.printResults();
		service.printResults();

	}

}
