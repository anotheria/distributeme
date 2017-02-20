package org.distributeme.core.concurrencycontrol;

import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.ConfigureMe;

/**
 * Configuration for concurrency control strategy.
 *
 * @author lrosenberg
 * @since 20.02.15 15:31
 * @version $Id: $Id
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

	/**
	 * <p>Getter for the field <code>clientSideLimit</code>.</p>
	 *
	 * @return a int.
	 */
	public int getClientSideLimit() {
		return clientSideLimit;
	}

	/**
	 * <p>Setter for the field <code>clientSideLimit</code>.</p>
	 *
	 * @param clientSideLimit a int.
	 */
	public void setClientSideLimit(int clientSideLimit) {
		this.clientSideLimit = clientSideLimit;
	}

	/**
	 * <p>Getter for the field <code>serverSideLimit</code>.</p>
	 *
	 * @return a int.
	 */
	public int getServerSideLimit() {
		return serverSideLimit;
	}

	/**
	 * <p>Setter for the field <code>serverSideLimit</code>.</p>
	 *
	 * @param serverSideLimit a int.
	 */
	public void setServerSideLimit(int serverSideLimit) {
		this.serverSideLimit = serverSideLimit;
	}

	/** {@inheritDoc} */
	@Override public String toString(){
		return "Client: "+getClientSideLimit()+", Server: "+getServerSideLimit();
	}

	/**
	 * <p>checkForNullValues.</p>
	 */
	@AfterConfiguration public void checkForNullValues(){
		if (clientSideLimit<1)
			clientSideLimit = Integer.MAX_VALUE;
		if (serverSideLimit<1)
			serverSideLimit = Integer.MAX_VALUE;
	}
}
