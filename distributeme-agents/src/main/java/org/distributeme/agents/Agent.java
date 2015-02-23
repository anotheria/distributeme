package org.distributeme.agents;

import java.io.Serializable;

public interface Agent extends Serializable{
	/**
	 * Called by the transport utilities immediately prior to serialization of the agent. Allows the agent to compress its internal data if needed.
	 */
	void prepareForTransport();
	/**
	 * Called by the transport utilities to notify that the transport is finished. Whatever function the agent should execute it should be started in this call.
	 * DistributeMe will call the method in a separate execution environment (thread or executor), hence no own threads are needed to be started here.  
	 */
	void awake();
}
