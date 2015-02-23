package org.distributeme.test.moskitojourney;

public class CServiceException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public CServiceException(String message){
		super(message);
	}
	
	public CServiceException(String message, Throwable cause){
		super(message,cause);
	}

}
