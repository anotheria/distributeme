package org.distributeme.agents;

import java.util.Random;

public class ComplexAgent implements Agent, TestAgent{
	private ComplexAgentComponent component = new ComplexAgentComponent();
	private static final long id = new Random(System.nanoTime()).nextLong();
	
	public void setCode(String code){
		component.setCode(code);
	}
	
	public String getCode(){
		return component.getCode();
	}
	
	public String toString(){
		return "(agent "+super.toString()+component+")";
	}
	
	@Override public long getId(){
		return id;
	}
	
	@Override public long getSubId(){
		return component.getId();
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
