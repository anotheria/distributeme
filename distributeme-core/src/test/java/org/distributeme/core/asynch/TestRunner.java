package org.distributeme.core.asynch;


class TestRunner implements Runnable{

	private Object resultObject;
	long sleepTime;
	CallType type;
	CallBackHandler handler;
	
	public TestRunner(CallBackHandler aHandler, Object aResultObject, CallType aType, long aSleepTime) {
		sleepTime = aSleepTime;
		resultObject = aResultObject;
		type = aType;
		handler = aHandler;
	}
	
	@Override
	public void run() {
		try{
			Thread.sleep(10+sleepTime);
		}catch(InterruptedException e){}
		if (type==CallType.SUC)
			handler.success(resultObject);
		else
			handler.error((Exception)resultObject);

	}
	
}