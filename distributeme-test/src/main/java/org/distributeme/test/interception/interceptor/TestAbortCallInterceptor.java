package org.distributeme.test.interception.interceptor;

import net.anotheria.util.NumberUtils;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.interceptor.AbstractClientSideRequestInterceptor;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptorResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TestAbortCallInterceptor extends AbstractClientSideRequestInterceptor{

	//warning this is not threadsafe, but its only a test class, use concurrent hashmap or synchronize for prod.
	private Map<Thread, Entry> activeCalls = new HashMap<>();
	
	Timer timer;
	
	public TestAbortCallInterceptor(){
		timer = new Timer("Interceptor", true);
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("Timer is checking: "+activeCalls);
				for (Entry e : activeCalls.values()){
					if (e.isExpired()){
						System.out.println(e + " expired ");
						e.targetThread.interrupt();
					}
				}
			}
		}, 0, 1000L);
	}
	
	@Override
	public InterceptorResponse beforeServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {
	
		if (!context.getServiceId().equals("org_distributeme_test_interception_TestService"))
			return InterceptorResponse.CONTINUE;
		if (!context.getMethodName().equals("sleepingCall"))
			return InterceptorResponse.CONTINUE;

		activeCalls.put(Thread.currentThread(), new Entry(Thread.currentThread()));
		System.out.println("added "+Thread.currentThread());		
		return super.beforeServiceCall(context, iContext);
	}

	@Override
	public InterceptorResponse afterServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {
		if (!context.getServiceId().equals("org_distributeme_test_interception_TestService"))
			return InterceptorResponse.CONTINUE;
		if (!context.getMethodName().equals("sleepingCall"))
			return InterceptorResponse.CONTINUE;
		
		activeCalls.remove(Thread.currentThread());
		System.out.println("removed "+Thread.currentThread());		

		return super.afterServiceCall(context, iContext);
	}
	
	

	static class Entry{
		Thread targetThread;
		long passTime;
		
		Entry(Thread aTargetThread){
			targetThread = aTargetThread;
			passTime = System.currentTimeMillis();
		}
		
		public boolean isExpired(){
			return System.currentTimeMillis() - passTime > 1500;
		}
		
		public String toString(){
			return targetThread+" at "+NumberUtils.makeISO8601TimestampString(passTime);
		}
	}
}
