package org.distributeme.agents.operatives;

import org.distributeme.agents.AgencyImpl;
import org.distributeme.agents.Agent;
import org.distributeme.core.ServiceDescriptor;
import org.slf4j.LoggerFactory;

/**
 * This agent will move into given destination vm and print out a HelloWorld to SysOut.
 * @author lrosenberg
 *
 */
public class HelloWorldAgent2 implements Agent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void prepareForTransport() {
		System.out.println("Going to move now!");
	}

	@Override
	public void awake() {
		System.out.println("Good bye World");
	}

	
	public static void main(String[] args) throws Exception{
		LoggerFactory.getLogger(HelloWorldAgent2.class).info("TEST");
		System.out.println("Creating agent");
		String target = "rmi://org_distributeme_test_echo_EchoService.lfxyotkpcy@192.168.200.101:9250@20110817012752";
		if (args.length!=1){
			//return later
		}else{
			target = args[0];
		}
		
		System.out.println("Trying to send agent to "+target);
		Agent agent = new HelloWorldAgent2();
		AgencyImpl.INSTANCE.sendAgent(agent, ServiceDescriptor.fromSystemWideUniqueId(target));
		System.out.println("done");
	}
}
