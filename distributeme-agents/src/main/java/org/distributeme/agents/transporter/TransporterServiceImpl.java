package org.distributeme.agents.transporter;

import org.distributeme.agents.AgencyException;
import org.distributeme.agents.AgencyImpl;
import org.distributeme.agents.AgentPackage;

public class TransporterServiceImpl implements TransporterService{

	@Override
	public void receiveAndAwakeAgent(AgentPackage agent)
			throws TransporterServiceException {
		try{
			AgencyImpl.INSTANCE.receiveAndAwakeAgent(agent);
		}catch(AgencyException e){
			throw new TransporterServiceException("Target's agency exception, transport aborted", e);
		}
		
	}

}
