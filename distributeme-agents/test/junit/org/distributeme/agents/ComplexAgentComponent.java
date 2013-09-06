package org.distributeme.agents;

import java.io.Serializable;
import java.util.Random;

public class ComplexAgentComponent implements Serializable{
	private String code;

	private static final long id = new Random(System.nanoTime()).nextLong();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String toString(){
		return super.toString()+" "+getCode();
	}
	
	public static final long getId(){
		return id;
	}
}
