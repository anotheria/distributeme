package org.distributeme.test.concurrencycontrol;

import java.util.concurrent.atomic.AtomicLong;

public class TestServiceImpl implements TestService{

	private AtomicLong clientSideLimitedConcurrentRequestCounter;
	private AtomicLong serverSideLimitedConcurrentRequestCounter;
	private AtomicLong bothSidesLimitedConcurrentRequestCounter;
	private AtomicLong clazzLevelLimitedConcurrentRequestCounter;
	private AtomicLong clientSideLimitedMaxRequestCount; 
	private AtomicLong serverSideLimitedMaxRequestCount;
	private AtomicLong bothSidesLimitedMaxRequestCount;
	private AtomicLong clazzLevelLimitedMaxRequestCount;
	
	public TestServiceImpl(){
		resetStats();
	}
	
	@Override
	public  long clientSideLimited(long parameter) {
		long concurrentRequests = clientSideLimitedConcurrentRequestCounter.incrementAndGet();
		long currentMaxRequest = clientSideLimitedMaxRequestCount.get();
		if (currentMaxRequest < concurrentRequests)
			clientSideLimitedMaxRequestCount.compareAndSet(currentMaxRequest, concurrentRequests);
		try{
			Thread.sleep(5);
		}catch(InterruptedException ignored){
			//ignore
		}finally{
			clientSideLimitedConcurrentRequestCounter.decrementAndGet();
		}
		return parameter;
	}

	@Override
	public long serverSideLimited(long parameter) {
		long concurrentRequests = serverSideLimitedConcurrentRequestCounter.incrementAndGet();
		long currentMaxRequest = serverSideLimitedMaxRequestCount.get();
		if (currentMaxRequest < concurrentRequests)
			serverSideLimitedMaxRequestCount.compareAndSet(currentMaxRequest, concurrentRequests);
		try{
			Thread.sleep(50);
		}catch(InterruptedException ignored){
			//ignore
		}finally{
			serverSideLimitedConcurrentRequestCounter.decrementAndGet();
		}
		return parameter;
	}
	
	@Override
	public long bothSideLimited(long parameter) {
		long concurrentRequests = bothSidesLimitedConcurrentRequestCounter.incrementAndGet();
		long currentMaxRequest = bothSidesLimitedMaxRequestCount.get();
		if (currentMaxRequest < concurrentRequests)
			bothSidesLimitedMaxRequestCount.compareAndSet(currentMaxRequest, concurrentRequests);
		try{
			Thread.sleep(50);
		}catch(InterruptedException ignored){
			//ignore
		}finally{
			bothSidesLimitedConcurrentRequestCounter.decrementAndGet();
		}
		return parameter;
	}

	@Override
	public long clazzLevelServerSideLimited(long parameter) {
		long concurrentRequests = clazzLevelLimitedConcurrentRequestCounter.incrementAndGet();
		long currentMaxRequest = clazzLevelLimitedMaxRequestCount.get();
		if (currentMaxRequest < concurrentRequests)
			clazzLevelLimitedMaxRequestCount.compareAndSet(currentMaxRequest, concurrentRequests);
		try{
			Thread.sleep(50);
		}catch(InterruptedException ignored){
			//ignore
		}finally{
			clazzLevelLimitedConcurrentRequestCounter.decrementAndGet();
		}
		return parameter;
	}

	@Override
	public void printAndResetStats() {
		printStats();
		resetStats();
		
	}

	@Override
	public void printStats() {
		System.out.println("Client side lock: current: "+clientSideLimitedConcurrentRequestCounter.get()+", max: "+clientSideLimitedMaxRequestCount.get());
		System.out.println("Server side lock: current: "+serverSideLimitedConcurrentRequestCounter.get()+", max: "+serverSideLimitedMaxRequestCount.get());
		System.out.println("Both   side lock: current: "+bothSidesLimitedConcurrentRequestCounter.get()+", max: "+bothSidesLimitedMaxRequestCount.get());
		System.out.println("Clazz level lock: current: "+clazzLevelLimitedConcurrentRequestCounter.get()+", max: "+clazzLevelLimitedMaxRequestCount.get());
		System.out.println("======================================");
	}

	private void resetStats(){
		clientSideLimitedConcurrentRequestCounter = new AtomicLong(0);
		serverSideLimitedConcurrentRequestCounter = new AtomicLong(0);
		bothSidesLimitedConcurrentRequestCounter = new AtomicLong(0);
		clazzLevelLimitedConcurrentRequestCounter = new AtomicLong(0);
		clientSideLimitedMaxRequestCount = new AtomicLong(0);
		serverSideLimitedMaxRequestCount = new AtomicLong(0);
		bothSidesLimitedMaxRequestCount = new AtomicLong(0);
		clazzLevelLimitedMaxRequestCount = new AtomicLong(0);
	}
}
