package org.distributeme.agents.singleton;

import org.distributeme.agents.Agent;

import java.io.Serializable;

public class SingletonAgent implements Agent, Serializable, SingletonTestable{

	private Singleton singleton = Singleton.INSTANCE;
	
	@Override
	public void prepareForTransport() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void awake() {
		System.out.println("Current singleton "+singleton);
		singleton.setCode(1000);
		System.out.println("Current singleton after"+singleton);
	}
	
	public String toSingletonString(){
		return singleton.toSuperString();
	}
	
}
