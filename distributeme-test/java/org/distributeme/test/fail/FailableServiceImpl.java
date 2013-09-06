package org.distributeme.test.fail;

public class FailableServiceImpl implements FailableService{

	@Override
	public void failableMethod() {
		System.out.println("Called failableMethod");
	}

	@Override
	public void retryMethod() {
		System.out.println("Called retryMethod");
	}

	@Override
	public void retryOnceMethod() {
		System.out.println("Called retryOnceMethod");
	}

	@Override
	public void defaultMethod() {
		System.out.println("Called defaultMethod");
	}

	@Override
	public long failableEcho(long value) {
		return value;
	}

	@Override
	public long retryEcho(long value) {
		return value;
	}

	@Override
	public long retryOnceEcho(long value) {
		return value;
	}

	@Override
	public long failoverEcho(long value) {
		return value;
	}

	@Override
	public void failoverPrint(String message) {
		print(message);
	}

	@Override
	public void failoverPrintAndStay(String message) {
		print(message);
	}
	
	@Override
	public void failoverPrintAndStayFoTenSeconds(String message) {
		print(message);
	}

	private void print(String message){
		System.out.println("FailoverPrintMessage: "+message);
	}
	
	
}
