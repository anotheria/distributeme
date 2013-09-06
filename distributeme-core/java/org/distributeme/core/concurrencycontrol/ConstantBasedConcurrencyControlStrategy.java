package org.distributeme.core.concurrencycontrol;

import java.util.concurrent.atomic.AtomicLong;

import net.anotheria.util.StringUtils;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.ServerSideCallContext;

/**
 * This implementation of a concurrencycontrolstrategy is based on constants which are annotated to the target interface.
 * @author lrosenberg
 *
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

	@Override
	public void notifyClientSideCallStarted(ClientSideCallContext context) {
		if (clientSideRequestCount.incrementAndGet()>clientSideLimit){
			clientSideRequestCount.decrementAndGet();
			throw new OutgoingRequestRefusedException();
		}
	}

	@Override
	public void notifyClientSideCallFinished(ClientSideCallContext context) {
		clientSideRequestCount.decrementAndGet();
	}

	@Override
	public void notifyServerSideCallStarted(ServerSideCallContext context) {
		if (serverSideRequestCount.incrementAndGet()>serverSideLimit){
			serverSideRequestCount.decrementAndGet();
			throw new ServerRefusedRequestException();
		}
	}

	@Override
	public void notifyServerSideCallFinished(ServerSideCallContext context) {
		serverSideRequestCount.decrementAndGet();
	}
	
	@Override public String toString(){
		return getClass().getSimpleName()+" with limits ("+clientSideLimit+", "+serverSideLimit+")";
	}
}
