package org.distributeme.agents;

import org.distributeme.agents.transporter.TransporterService;
import org.distributeme.agents.transporter.TransporterServiceException;
import org.distributeme.agents.transporter.generated.RemoteTransporterServiceStub;
import org.distributeme.agents.transporter.generated.TransporterServiceConstants;
import org.distributeme.core.ServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum AgencyImpl implements Agency{
	INSTANCE;
	
	private static Logger log = LoggerFactory.getLogger(AgencyImpl.class);

	@Override
	public void receiveAndAwakeAgent(AgentPackage agentpackage) throws AgencyException {
		if (log.isDebugEnabled())
			log.debug("Incoming agent "+agentpackage);
		final Agent agent = AgentPackageUtility.unpack(agentpackage);
		//System.out.println("unpacked agent "+agent);
		Thread agentThread = new Thread(new AgentRunnable(agent), "Agent "+agent.toString());
		agentThread.start();
	}
	
	
	
	
	class AgentRunnable implements Runnable{
		private Agent agent;
		public AgentRunnable(Agent toRun) {
			agent = toRun;
		}
		
		public void run(){
			agent.awake();
		}
	}




	@Override
	public void sendAgent(Agent agent, ServiceDescriptor destination)
			throws AgencyException {
		
		try{
			ServiceDescriptor target = destination.changeServiceId(TransporterServiceConstants.getServiceId());
			System.out.println("Sending agent "+agent+" to "+target);
			TransporterService transporter = new RemoteTransporterServiceStub(target);
			transporter.receiveAndAwakeAgent(AgentPackageUtility.pack(agent));
		}catch(TransporterServiceException e){
			throw new AgencyException("sendAgent("+agent+", "+destination+")", e);
		}
	}
	
}
