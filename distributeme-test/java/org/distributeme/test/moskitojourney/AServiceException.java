package org.distributeme.test.moskitojourney;

public class AServiceException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public AServiceException(String message){
		super(message);
	}
	
	public AServiceException(String message, Throwable cause){
		super(message,cause);
	}

}
