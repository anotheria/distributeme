package org.distributeme.firstexample;

/**
 * Implementation of the FirstService that just prints out the message and returns similar message back.
 */
public class FirstServiceImpl implements FirstService{
	public String greet(String message){
		System.out.println("server: I've got message "+message);
		return "Greet you stranger, who said \""+message+"\"";
	}
}
