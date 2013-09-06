package org.distributeme.agents;

import java.util.Random;

public class PlainAgent implements Agent, TestAgent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	
	private static final long id = new Random(System.nanoTime()).nextLong();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String toString(){
		return "(agent "+code+")";
	}
	
	public long getId(){
		return id;
	}
	
	public long getSubId(){
		return id;
	}

	@Override
	public void prepareForTransport() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void awake() {
		// TODO Auto-generated method stub
		
	}
}
