package org.distributeme.firstexample;

public class FirstServiceImpl implements FirstService{
	public String greet(String message){
		System.out.println("server: I've got message "+message);
		return "Greet you stranger, who said \""+message+ '"';
	}
}
