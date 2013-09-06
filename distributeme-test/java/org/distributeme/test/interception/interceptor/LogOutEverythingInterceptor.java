package org.distributeme.test.interception.interceptor;

import java.util.concurrent.atomic.AtomicLong;

import org.distributeme.core.AbstractCallContext;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptionPhase;
import org.distributeme.core.interceptor.InterceptorResponse;
import org.distributeme.core.interceptor.SinglePhaseInterceptor;

abstract class LogOutEverythingInterceptor extends SinglePhaseInterceptor{

	private AtomicLong callCounter = new AtomicLong();
	
	public LogOutEverythingInterceptor(){
		super(InterceptionPhase.BEFORE_SERVICE_CALL, InterceptionPhase.AFTER_SERVICE_CALL, InterceptionPhase.BEFORE_SERVANT_CALL, InterceptionPhase.AFTER_SERVANT_CALL);
	}
	
	@Override
	protected InterceptorResponse processPhase(AbstractCallContext callContext,
			InterceptionContext iContext) {
		
		StringBuilder output = new StringBuilder("-"+getMessage()+"- ").append(getCallNumber(iContext)).append(" ");
		output.append(iContext.getCurrentPhase()).append(" ").append(callContext.getMethodName()).append("(");
		StringBuilder params = new StringBuilder();
		for (Object p : callContext.getParameters()){
			if (params.length()!=0)
				params.append(", ");
			params.append(p);
		}
		output.append(params).append(")");
		if (iContext.getReturnValue()!=null){
			output.append(" RETURN: "+iContext.getReturnValue());
		}
		if (iContext.getException()!=null){
			output.append(" EXCEPTION: "+iContext.getException());
		}
		System.out.println(output.toString());
		return InterceptorResponse.CONTINUE;
	}
	
	
	
	private String getCallNumber(InterceptionContext iContext){
		String currentNumber = (String) iContext.getLocalStore().get(LogOutEverythingInterceptor.class);
		if (currentNumber==null){
			currentNumber = ""+callCounter.incrementAndGet();
			iContext.getLocalStore().put(LogOutEverythingInterceptor.class, currentNumber);
		}
		return currentNumber;
	}
	
	protected abstract String getMessage();

}
