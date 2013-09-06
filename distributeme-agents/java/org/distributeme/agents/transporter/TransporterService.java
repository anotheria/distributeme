package org.distributeme.agents.transporter;

import net.anotheria.anoprise.metafactory.Service;

import org.distributeme.agents.AgentPackage;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.SupportService;

@DistributeMe(moskitoSupport=false, factoryClazz=TransporterServiceFactory.class)
@SupportService
public interface TransporterService extends Service{
	void receiveAndAwakeAgent(AgentPackage agent) throws TransporterServiceException;
}
