package org.distributeme.core.interceptor.moskito;

import net.anotheria.moskito.core.calltrace.CurrentlyTracedCall;
import net.anotheria.moskito.core.calltrace.NoTracedCall;
import net.anotheria.moskito.core.calltrace.RunningTraceContainer;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.ServerSideCallContext;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptorResponse;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * This test emulates both client and server side handling of the MoskitoJourneyInterceptor.
 * @author lrosenberg
 *
 */
@Ignore
public class MoskitoJourneyInterceptorFullCycleTest{

	//this test relied on moskito toDetails (getCurrentlyTracedCall()).toDetails() method, that has been removed (replaced with toString in test).


	//all test go through same interceptor, as it is with prod environments.
	MoskitoJourneyInterceptor interceptor = new MoskitoJourneyInterceptor();
	
	@Test public void testFullCycle() throws Exception {
		//first we have to create a new client side emulation
		
		//start journey
		RunningTraceContainer.startTracedCall("MY_TEST");
		//emulate method call
		final ClientSideCallContext cctx = new ClientSideCallContext("FOO", "BAR", null);
		InterceptionContext icctx = new InterceptionContext();
		assertEquals(InterceptorResponse.CONTINUE, interceptor.beforeServiceCall(cctx, icctx));
		//now the first interception phase is over, we should at least have the interception flag inside the cctx.
		assertEquals(Boolean.TRUE, cctx.getTransportableCallContext().get(interceptor.testGetCONTEXT_ATTRIBUTE_TRACE_FLAGname()));
		assertEquals(2, ((CurrentlyTracedCall)RunningTraceContainer.getCurrentlyTracedCall()).getNumberOfSteps());
		assertTrue(((CurrentlyTracedCall)RunningTraceContainer.getCurrentlyTracedCall()).toString().indexOf("NETWORK OUT")>0);
		
		//now we emulate a call by starting a new thread.
		Thread serverSide = new Thread(new Runnable() {
			
			@Override
			public void run() {
				ServerSideCallContext sctx = new ServerSideCallContext("FOO", new HashMap());
				sctx.getTransportableCallContext().putAll(cctx.getTransportableCallContext());
				InterceptionContext ictx = new InterceptionContext();
				
				//distributeme will call this.
				interceptor.beforeServantCall(sctx, ictx);
				
				//now we should have started a trace 
				assertNotSame("There should be a running trace" , NoTracedCall.INSTANCE, RunningTraceContainer.getCurrentlyTracedCall());
				assertTrue("There should be a trace running" , RunningTraceContainer.isTraceRunning());
				
				//assume service actually performs some work
				((CurrentlyTracedCall)RunningTraceContainer.getCurrentlyTracedCall()).startStep("SERVERSIDE");
				((CurrentlyTracedCall)RunningTraceContainer.getCurrentlyTracedCall()).endStep();
				
				assertEquals(((CurrentlyTracedCall)RunningTraceContainer.getCurrentlyTracedCall()).getNumberOfSteps(), 3);
				//ensure that the use-case step TEST we added previously is part of the call-tree.
				assertTrue(((CurrentlyTracedCall)RunningTraceContainer.getCurrentlyTracedCall()).toString().indexOf("SERVERSIDE")>0);
				
				//distributeme will call this.
				interceptor.afterServantCall(sctx, ictx);
				assertEquals("There should be no trace" , NoTracedCall.INSTANCE, RunningTraceContainer.getCurrentlyTracedCall());
				assertFalse("There should be no trace running" , RunningTraceContainer.isTraceRunning());
		
				assertNotNull(sctx.getTransportableCallContext().get(interceptor.testGetCONTEXT_ATTRIBUTE_STEPBACKFROMSERVERName()));
				//copy call back
				cctx.getTransportableCallContext().putAll(sctx.getTransportableCallContext());
			}
		});
		serverSide.start();
		serverSide.join();

		//System.out.println(((CurrentlyTracedCall)RunningTraceContainer.getCurrentlyTracedCall()).toDetails());
		//assert that we got server side thread not in the trace BEFORE afterServiceCall, which would mean an error...
		assertFalse(((CurrentlyTracedCall)RunningTraceContainer.getCurrentlyTracedCall()).toString().indexOf("SERVERSIDE")>0);

		
		assertEquals(InterceptorResponse.CONTINUE, interceptor.afterServiceCall(cctx, icctx));
		//System.out.println(((CurrentlyTracedCall)RunningTraceContainer.getCurrentlyTracedCall()).toDetails());
		
		//end journey
		CurrentlyTracedCall trace = (CurrentlyTracedCall)RunningTraceContainer.endTrace();
		//assert that we got server side thread in the trace
		assertTrue(trace.toString().indexOf("SERVERSIDE")>0);

	}
	
	


}
