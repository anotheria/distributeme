package org.distributeme.core.concurrencycontrol;

import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.ConfigureMe;

/**
 * Configuration for concurrency control strategy.
 *
 * @author lrosenberg
 * @since 20.02.15 15:31
 */

@ConfigureMe(allfields = true)
public class ConcurrencyControlStrategyConfiguration {
	/**
	 * Client side limit.
	 */
	private int clientSideLimit = Integer.MAX_VALUE;
	/**
	 * Server side limit.
	 */
	private int serverSideLimit = Integer.MAX_VALUE;

	public int getClientSideLimit() {
		return clientSideLimit;
	}

	public void setClientSideLimit(int clientSideLimit) {
		this.clientSideLimit = clientSideLimit;
	}

	public int getServerSideLimit() {
		return serverSideLimit;
	}

	public void setServerSideLimit(int serverSideLimit) {
		this.serverSideLimit = serverSideLimit;
	}

	@Override public String toString(){
        return "Client: "+ clientSideLimit +", Server: "+ serverSideLimit;
	}

	@AfterConfiguration public void checkForNullValues(){
		if (clientSideLimit<1)
			clientSideLimit = Integer.MAX_VALUE;
		if (serverSideLimit<1)
			serverSideLimit = Integer.MAX_VALUE;
	}
}
