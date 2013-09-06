package org.distributeme.agents;

import org.distributeme.core.ServiceDescriptor;
/**
 * An agency is the internal structure in DistributeMe VM which handles agents. It uses Transporter service for agent transportation.
 * @author lrosenberg
 *
 */
public interface Agency {
	/**
	 * Called from outside to 
	 * @param agent
	 * @throws AgencyException
	 */
	void receiveAndAwakeAgent(AgentPackage agent) throws AgencyException;
	/**
	 * Sends an agent to given destination. The agent shouldn't perform any tasks after it has been passed as parameter to this method.
	 * @param agent
	 * @param destination
	 * @throws AgencyException
	 */
	void sendAgent(Agent agent, ServiceDescriptor destination) throws AgencyException;
}
