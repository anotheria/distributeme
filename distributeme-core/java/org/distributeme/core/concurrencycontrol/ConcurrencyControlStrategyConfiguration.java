package org.distributeme.core.concurrencycontrol;

import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.ConfigureMe;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 20.02.15 15:31
 */

@ConfigureMe(allfields = true)
public class ConcurrencyControlStrategyConfiguration {
	private int clientSideLimit = Integer.MAX_VALUE;
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
		return "Client: "+getClientSideLimit()+", Server: "+getServerSideLimit();
	}

	@AfterConfiguration public void checkForNullValues(){
		if (clientSideLimit<1)
			clientSideLimit = Integer.MAX_VALUE;
		if (serverSideLimit<1)
			serverSideLimit = Integer.MAX_VALUE;
	}
}
