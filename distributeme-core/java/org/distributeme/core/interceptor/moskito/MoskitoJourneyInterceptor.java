package org.distributeme.core.interceptor.moskito;

import net.anotheria.moskito.core.calltrace.CurrentlyTracedCall;
import net.anotheria.moskito.core.calltrace.RunningTraceContainer;
import net.anotheria.moskito.core.calltrace.TraceStep;
import net.anotheria.moskito.core.calltrace.TracedCall;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.ServerSideCallContext;
import org.distributeme.core.interceptor.ClientSideRequestInterceptor;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptorResponse;
import org.distributeme.core.interceptor.ServerSideRequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interceptor supports moskito journeys over the network, it transmits server side call trace back to the client.
 * @author lrosenberg
 *
 */
public class MoskitoJourneyInterceptor implements ClientSideRequestInterceptor, ServerSideRequestInterceptor{

	/**
	 * Constant for the name of the context attribute that is used to transfer the step back to client.
	 */
	private static final String CONTEXT_ATTRIBUTE_STEPBACKFROMSERVER = MoskitoJourneyInterceptor.class.getName()+"Step";
	/**
	 * Constant for the attribute name that signals that journey tracking is enabled for current call.
	 */
	private static final String CONTEXT_ATTRIBUTE_TRACE_FLAG = MoskitoJourneyInterceptor.class.getName()+"TraceFlag";
	/**
	 * Constant for the name of the attribute that stores the start time locally in the interception context to calculate call execution on each side.
	 */
	private static final String ICONTEXT_ATTRIBUTE_STARTTIME = MoskitoJourneyInterceptor.class.getName()+"Start";

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(MoskitoJourneyInterceptor.class);
	
	@Override
	public InterceptorResponse beforeServantCall(ServerSideCallContext context,
			InterceptionContext iContext) {
		Boolean flag = (Boolean) context.getTransportableCallContext().get(CONTEXT_ATTRIBUTE_TRACE_FLAG);
		if (flag!=null && flag){
			CurrentlyTracedCall currentTrace =  new CurrentlyTracedCall("SERVER_SIDE");
			RunningTraceContainer.setCurrentlyTracedCall(currentTrace);
			currentTrace.startStep("--- NETWORK IN ---", MoskitoJourneyInterceptorStatsProducer.SKELETON);
			iContext.getLocalStore().put(ICONTEXT_ATTRIBUTE_STARTTIME, System.nanoTime());
		}

		return InterceptorResponse.CONTINUE;
	}

		

	@Override
	public InterceptorResponse afterServantCall(ServerSideCallContext context,
			InterceptionContext iContext) {
		TracedCall aTracedCall = RunningTraceContainer.getCurrentlyTracedCall();
		if (!aTracedCall.callTraced())
			return InterceptorResponse.CONTINUE;
		try{
			CurrentlyTracedCall currentTrace =  (CurrentlyTracedCall)aTracedCall;
			try{
				long endTime = (Long)iContext.getLocalStore().get(ICONTEXT_ATTRIBUTE_STARTTIME);
				currentTrace.getCurrentStep().setDuration(System.nanoTime()-endTime);
			}catch(Exception e){
				log.warn("Couldn't detect duration of current call "+currentTrace.getCurrentStep() +" in "+currentTrace, e);
			}
			context.getTransportableCallContext().put(CONTEXT_ATTRIBUTE_STEPBACKFROMSERVER, currentTrace.getCurrentStep());
			context.getTransportableCallContext().remove(CONTEXT_ATTRIBUTE_TRACE_FLAG);
			currentTrace.endStep();
		}finally{
			RunningTraceContainer.endTrace();
		}
		return InterceptorResponse.CONTINUE;
	}

	@Override
	public InterceptorResponse beforeServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {
		TracedCall aTracedCall = RunningTraceContainer.getCurrentlyTracedCall();
		if (!aTracedCall.callTraced()){
			return InterceptorResponse.CONTINUE;
		}
		CurrentlyTracedCall currentTrace = (CurrentlyTracedCall)aTracedCall;
		currentTrace.startStep("--- NETWORK OUT ---", MoskitoJourneyInterceptorStatsProducer.NETWORK);
		iContext.getLocalStore().put(ICONTEXT_ATTRIBUTE_STARTTIME, System.nanoTime());
		context.getTransportableCallContext().put(CONTEXT_ATTRIBUTE_TRACE_FLAG, Boolean.TRUE);
		return InterceptorResponse.CONTINUE;
		
		
	}

	@Override
	public InterceptorResponse afterServiceCall(ClientSideCallContext context,
			InterceptionContext iContext) {
		TracedCall aTracedCall = RunningTraceContainer.getCurrentlyTracedCall();
		if (!aTracedCall.callTraced()){
			return InterceptorResponse.CONTINUE;
		}

		CurrentlyTracedCall currentTrace =  (CurrentlyTracedCall)aTracedCall;
		TraceStep fromServer = (TraceStep)context.getTransportableCallContext().get(CONTEXT_ATTRIBUTE_STEPBACKFROMSERVER);
		if (fromServer==null){
			log.warn("Warning, unexpected null TraceStep in "+currentTrace+" came back from server.");
			return InterceptorResponse.CONTINUE;
		}
		currentTrace.getCurrentStep().addChild(fromServer);
		long endTime = System.nanoTime();
		try{
			long startTime = (Long)iContext.getLocalStore().get(ICONTEXT_ATTRIBUTE_STARTTIME);
			currentTrace.getCurrentStep().setDuration(endTime-startTime);
		}catch(Exception e){
			log.warn("Couldn't detect duration of current call "+currentTrace.getCurrentStep() +" in "+currentTrace, e);
		}
		currentTrace.endStep();
		return InterceptorResponse.CONTINUE;
	}

	//for unittesting

	/**
	 * Returns name of the variable for unittest.
	 * @return
	 */
	static String testGetCONTEXT_ATTRIBUTE_TRACE_FLAGname(){
		return CONTEXT_ATTRIBUTE_TRACE_FLAG;
	}

	/**
	 * Returns name of the variable for unittest.
	 * @return
	 */
	static String testGetCONTEXT_ATTRIBUTE_STEPBACKFROMSERVERName(){
		return CONTEXT_ATTRIBUTE_STEPBACKFROMSERVER;
	}
}
