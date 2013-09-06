package org.distributeme.test.mod;

public class ModedServiceImpl implements ModedService{



	@Override
	public long modEcho(long parameter) throws ModedServiceException {
		return echo(parameter);
	}

	@Override
	public long unmodEcho(long parameter) throws ModedServiceException {
		return echo(parameter);
	}
	
	private long echo(long parameter){
		System.out.println("Echo called "+parameter);
		return parameter;
	}

	private long boolean2long(boolean p){
		return p ? 1 : 0;
	}
	
	private boolean long2boolean(long l){
		return l==1;
	}

	@Override
	public boolean modEcho(String dummy, boolean parameter) {
		return long2boolean(echo(boolean2long(parameter)));
	}
	
	@Override public void printString(String param) throws ModedServiceException{
		System.out.println("param: "+param);
	}
}
