package org.distributeme.core.routing;

import org.distributeme.core.ClientSideCallContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This router sends each call to another instance. It is useful if you want to cluster a service
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class RoundRobinRouter extends AbstractRouter implements Router{
	/**
	 * Max mod parameter.
	 */
	private int MAX = 0;
	/**
	 * Call counter.
	 */
	private AtomicInteger callCounter = new AtomicInteger(0);
	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(RoundRobinRouter.class);
	
	/** {@inheritDoc} */
	@Override
	public void customize(String parameter) {
		try{
			MAX = Integer.parseInt(parameter);
		}catch(NumberFormatException e){
			log.error("Can't set customization parameter "+parameter+", send all traffic to default instance");
		}
	}

	/** {@inheritDoc} */
	@Override
	public String getServiceIdForCall(ClientSideCallContext callContext) {
		if (MAX==0)
			return callContext.getServiceId();
		int fromCounter = callCounter.incrementAndGet();
		if (fromCounter>=MAX){
			int oldCounter = fromCounter;
			fromCounter = 0;
			callCounter.compareAndSet(oldCounter, 0);
		}
		return callContext.getServiceId()+"_"+fromCounter;
	}

}
