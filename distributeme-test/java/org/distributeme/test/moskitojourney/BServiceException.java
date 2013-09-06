package org.distributeme.test.moskitojourney;

public class BServiceException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public BServiceException(String message){
		super(message);
	}
	
	public BServiceException(String message, Throwable cause){
		super(message,cause);
	}

}
