package org.distributeme.test.asynch;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class TestServiceImpl implements TestService {

	private AtomicInteger requestCounter = new AtomicInteger();
	private Random rnd = new Random(System.currentTimeMillis());
	
	@Override 
	public long ping(long param){ return param; }

	
	@Override
	public long sleepAndReturnRandom(long sleepTime) {
		int reqNumber = requestCounter.incrementAndGet();
		System.out.println("sarr REQ BEGIN "+reqNumber);
		int ret = rnd.nextInt(1000);
		try{
			Thread.sleep(sleepTime);
		}catch(InterruptedException e){}
		System.out.println("sarr REQ FINISH "+reqNumber+", will return "+ret);
		return ret;
		
	}

	@Override
	public void sleep(long sleepTime) {
		int reqNumber = requestCounter.incrementAndGet();
		System.out.println("sleep REQ BEGIN "+reqNumber);
		try{
			Thread.sleep(sleepTime);
		}catch(InterruptedException e){}
		System.out.println("sleep REQ FINISH "+reqNumber);
	}
	
	@Override
	public void sleepAndThrowTypedException(long sleepTime) throws TestServiceException{
		int reqNumber = requestCounter.incrementAndGet();
		System.out.println("sleep REQ BEGIN "+reqNumber);
		try{
			Thread.sleep(sleepTime);
		}catch(InterruptedException e){}
		System.out.println("sleep REQ FINISH "+reqNumber);
		throw new TestServiceException("Exception in req: "+reqNumber);
	}

	@Override
	public void sleepAndThrowRuntimeException(long sleepTime){
		int reqNumber = requestCounter.incrementAndGet();
		System.out.println("sleep REQ BEGIN "+reqNumber);
		try{
			Thread.sleep(sleepTime);
		}catch(InterruptedException e){}
		System.out.println("sleep REQ FINISH "+reqNumber);
		throw new RuntimeException("RT Exception in req: "+reqNumber);
	}
}
