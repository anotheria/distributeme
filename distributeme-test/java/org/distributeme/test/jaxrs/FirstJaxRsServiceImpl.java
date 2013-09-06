package org.distributeme.test.jaxrs;

import org.distributeme.firstexample.FirstService;

public class FirstJaxRsServiceImpl implements FirstService{
	public String greet(String message){
		System.out.println("server: I've got message "+message);
		return "Greet you stranger, who said \""+message+"\"";
	}

	public long echo(long echo){
		return echo;
	}
}
