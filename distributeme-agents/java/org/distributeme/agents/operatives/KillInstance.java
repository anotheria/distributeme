package org.distributeme.agents.operatives;

import org.distributeme.agents.AgencyImpl;
import org.distributeme.agents.Agent;
import org.distributeme.core.ServiceDescriptor;

public class KillInstance implements Agent{

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
		System.out.println("Going to kill the locals, MUAHAHA... ");
		System.exit(0);
	}

	
	public static void main(String[] args) throws Exception{
		System.out.println("Creating agent");
		String target = "rmi://org_distributeme_test_echo_EchoService.lfxyotkpcy@192.168.200.101:9250@20110817012752";
		if (args.length!=1){
			//return later
		}else{
			target = args[0];
		}
		
		System.out.println("Trying to send agent to "+target);
		KillInstance agent = new KillInstance();
		AgencyImpl.INSTANCE.sendAgent(agent, ServiceDescriptor.fromSystemWideUniqueId(target));
		System.out.println("done");
	}
}
