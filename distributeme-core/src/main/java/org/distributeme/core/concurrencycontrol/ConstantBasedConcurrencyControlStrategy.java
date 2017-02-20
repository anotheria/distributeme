package org.distributeme.core.concurrencycontrol;

import net.anotheria.util.StringUtils;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.ServerSideCallContext;

import java.util.concurrent.atomic.AtomicLong;

/**
 * This implementation of a concurrencycontrolstrategy is based on constants which are annotated to the target interface.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class ConstantBasedConcurrencyControlStrategy implements ConcurrencyControlStrategy{
	/**
	 * Max number of connections on client side.
	 */
	private int clientSideLimit = Integer.MAX_VALUE;
	/**
	 * Max number of connections on server side.
	 */
	private int serverSideLimit = Integer.MAX_VALUE;
	/**
	 * Currenty active client side requests.
	 */
	private AtomicLong clientSideRequestCount = new AtomicLong(0);
	/**
	 * Currently active server side requests.
	 */
	private AtomicLong serverSideRequestCount = new AtomicLong(0);
	
	/** {@inheritDoc} */
	@Override
	public void customize(String parameter) {
		if (parameter==null || parameter.length()==0)
			throw new IllegalArgumentException("Empty or null parameter, expected clientlimit,serverlimit");
		String limits[] = StringUtils.tokenize(parameter, ',');
		int aClientSideLimit = Integer.parseInt(limits[0]);
		if (aClientSideLimit>0)
			clientSideLimit = aClientSideLimit;
		int aServerSideLimit = 0;
		if (limits.length>1)
			aServerSideLimit = Integer.parseInt(limits[1]);
		if (aServerSideLimit>0)
			serverSideLimit = aServerSideLimit;
		
	}

	/** {@inheritDoc} */
	@Override
	public void notifyClientSideCallStarted(ClientSideCallContext context) {
		if (clientSideRequestCount.incrementAndGet()>clientSideLimit){
			clientSideRequestCount.decrementAndGet();
			throw new OutgoingRequestRefusedException();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void notifyClientSideCallFinished(ClientSideCallContext context) {
		clientSideRequestCount.decrementAndGet();
	}

	/** {@inheritDoc} */
	@Override
	public void notifyServerSideCallStarted(ServerSideCallContext context) {
		if (serverSideRequestCount.incrementAndGet()>serverSideLimit){
			serverSideRequestCount.decrementAndGet();
			throw new ServerRefusedRequestException();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void notifyServerSideCallFinished(ServerSideCallContext context) {
		serverSideRequestCount.decrementAndGet();
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return getClass().getSimpleName()+" with limits ("+clientSideLimit+", "+serverSideLimit+")";
	}
}
