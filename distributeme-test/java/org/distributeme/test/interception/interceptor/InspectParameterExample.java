package org.distributeme.test.interception.interceptor;

import org.distributeme.core.AbstractCallContext;
import org.distributeme.core.interceptor.InterceptionContext;
import org.distributeme.core.interceptor.InterceptionPhase;
import org.distributeme.core.interceptor.InterceptorResponse;
import org.distributeme.core.interceptor.SinglePhaseInterceptor;

public class InspectParameterExample extends SinglePhaseInterceptor{

	public static final int TOSEARCH = 42;
	
	public InspectParameterExample(){
		super(InterceptionPhase.BEFORE_SERVICE_CALL);
	}
	
	private boolean checkParameter(Object toCheck){
		if (toCheck instanceof Integer){
			if ( ((Integer)toCheck).intValue()==TOSEARCH)
				return true;
		}
		return false;
	}
	
	@Override
	protected InterceptorResponse processPhase(AbstractCallContext callContext,
			InterceptionContext iContext) {
		
		for (Object p : callContext.getParameters()){
			if (checkParameter(p)){
				System.out.println("DETECTED SearchedParameter "+callContext.getServiceId()+" in "+callContext.getMethodName()+" - "+callContext.getParameters());
			}
		}
		return InterceptorResponse.CONTINUE;
	}
	

}