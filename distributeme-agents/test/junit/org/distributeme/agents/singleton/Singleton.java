package org.distributeme.agents.singleton;

import java.io.Serializable;

public class Singleton implements Serializable{
	private int code = 10;
	private static int staticCode = 10;
	
	static final Singleton INSTANCE = new Singleton();
	
	private Singleton(){
		
	}
	
	public void setCode(int aCode){
		code = aCode;
		staticCode = aCode;
	}
	
	public String toString(){
		return "singleton code: "+code+", staticcode: "+staticCode+" hashcode: "+getClass().hashCode();
	}
	
	public static void reset(){
		INSTANCE.code = 10;
		staticCode = 10;
	}
	
	public String toSuperString(){
		return super.toString();
	}
	
}
