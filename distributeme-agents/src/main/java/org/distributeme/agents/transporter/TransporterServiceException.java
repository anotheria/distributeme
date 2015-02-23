package org.distributeme.agents.transporter;

public class TransporterServiceException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TransporterServiceException(String message){
		super(message);
	}
	public TransporterServiceException(String message, Exception cause){
		super(message, cause);
	}
}
