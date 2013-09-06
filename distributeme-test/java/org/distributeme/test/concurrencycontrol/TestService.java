package org.distributeme.test.concurrencycontrol;

import net.anotheria.anoprise.metafactory.Service;

import org.distributeme.annotation.ConcurrencyControlClientSideLimit;
import org.distributeme.annotation.ConcurrencyControlLimit;
import org.distributeme.annotation.ConcurrencyControlServerSideLimit;
import org.distributeme.annotation.DistributeMe;

@DistributeMe
@ConcurrencyControlServerSideLimit(5)
public interface TestService extends Service{
	/**
	 * This method can be called by max three threads from the same stub.
	 * @return
	 */
	@ConcurrencyControlClientSideLimit(3)
	long clientSideLimited(long parameter);
	
	/**
	 * This method can handle max 5 concurrent requests on the server side.
	 * @return
	 */
	@ConcurrencyControlServerSideLimit(5)
	long serverSideLimited(long parameter);
	
	@ConcurrencyControlLimit(client=4, server=5)
	long bothSideLimited(long parameter);

	long clazzLevelServerSideLimited(long parameter);

	/**
	 * This method is for debug and demonstration purposes.
	 */
	void printAndResetStats();

	/**
	 * This method is for debug and demonstration purposes.
	 */
	void printStats();
}
