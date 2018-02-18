package org.distributeme.core.interceptor.moskito;

import net.anotheria.moskito.core.calltrace.CurrentlyTracedCall;
import net.anotheria.moskito.core.calltrace.NoTracedCall;
import net.anotheria.moskito.core.calltrace.RunningTraceContainer;
import org.distributeme.core.ServerSideCallContext;
import org.distributeme.core.interceptor.InterceptionContext;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

@Ignore
public class MoskitoJourneyInterceptorTest {

	//this test relied on moskito toDetails (getCurrentlyTracedCall()).toDetails() method, that has been removed (replaced with toString in test).

	//all test go through same interceptor, as it is with prod environments.
	MoskitoJourneyInterceptor interceptor = new MoskitoJourneyInterceptor();

	@Test public void testNoTracingIsStartedWithoutFlag(){
		ServerSideCallContext ctx = new ServerSideCallContext("FOO", new HashMap());
		InterceptionContext ictx = new InterceptionContext();
		
		interceptor.beforeServantCall(ctx, ictx);
		assertEquals("There should be no trace" , NoTracedCall.INSTANCE, RunningTraceContainer.getCurrentlyTracedCall());
		assertFalse("There should be no trace running" , RunningTraceContainer.isTraceRunning());
		interceptor.afterServantCall(ctx, ictx);
		assertEquals("There should be no trace" , NoTracedCall.INSTANCE, RunningTraceContainer.getCurrentlyTracedCall());
		assertFalse("There should be no trace running" , RunningTraceContainer.isTraceRunning());
	}
	
	@Test public void testServantSide(){
		ServerSideCallContext ctx = new ServerSideCallContext("FOO", new HashMap());
		ctx.getTransportableCallContext().put(MoskitoJourneyInterceptor.testGetCONTEXT_ATTRIBUTE_TRACE_FLAGname(), Boolean.TRUE);
		InterceptionContext ictx = new InterceptionContext();
		
		//distributeme will call this.
		interceptor.beforeServantCall(ctx, ictx);
		
		//now we should have started a trace 
		assertNotSame("There should be a running trace" , NoTracedCall.INSTANCE, RunningTraceContainer.getCurrentlyTracedCall());
		assertTrue("There should be a trace running" , RunningTraceContainer.isTraceRunning());
		
		//assume service actually performs some work
		((CurrentlyTracedCall)RunningTraceContainer.getCurrentlyTracedCall()).startStep("TEST");
		((CurrentlyTracedCall)RunningTraceContainer.getCurrentlyTracedCall()).endStep();
		
		assertEquals(((CurrentlyTracedCall)RunningTraceContainer.getCurrentlyTracedCall()).getNumberOfSteps(), 3);
		//ensure that the use-case step TEST we added previously is part of the call-tree.
		assertTrue(((CurrentlyTracedCall)RunningTraceContainer.getCurrentlyTracedCall()).toString().indexOf("TEST")>0);
		
		//distributeme will call this.
		interceptor.afterServantCall(ctx, ictx);
		assertEquals("There should be no trace" , NoTracedCall.INSTANCE, RunningTraceContainer.getCurrentlyTracedCall());
		assertFalse("There should be no trace running" , RunningTraceContainer.isTraceRunning());

		assertNotNull(ctx.getTransportableCallContext().get(interceptor.testGetCONTEXT_ATTRIBUTE_STEPBACKFROMSERVERName()));
		
	}

	@Test public void testNoTracingIsStartedWithoutFlagAfterATracingTest(){
		ServerSideCallContext ctx = new ServerSideCallContext("FOO", new HashMap());
		InterceptionContext ictx = new InterceptionContext();
		
		interceptor.beforeServantCall(ctx, ictx);
		assertEquals("There should be no trace" , NoTracedCall.INSTANCE, RunningTraceContainer.getCurrentlyTracedCall());
		assertFalse("There should be no trace running" , RunningTraceContainer.isTraceRunning());
		interceptor.afterServantCall(ctx, ictx);
		assertEquals("There should be no trace" , NoTracedCall.INSTANCE, RunningTraceContainer.getCurrentlyTracedCall());
		assertFalse("There should be no trace running" , RunningTraceContainer.isTraceRunning());
	}
	
}
