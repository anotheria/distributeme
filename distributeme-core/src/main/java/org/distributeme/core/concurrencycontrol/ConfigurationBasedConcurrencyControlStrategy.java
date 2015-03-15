package org.distributeme.core.concurrencycontrol;

import net.anotheria.util.StringUtils;
import org.configureme.ConfigurationManager;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.ServerSideCallContext;

import java.util.concurrent.atomic.AtomicLong;

/**
 * This implementation of a concurrencycontrolstrategy is based on constants which are annotated to the target interface.
 * @author lrosenberg
 *
 */
public class ConfigurationBasedConcurrencyControlStrategy implements ConcurrencyControlStrategy{

	/**
	 * Configuration instance.
	 */
	private ConcurrencyControlStrategyConfiguration configuration = new ConcurrencyControlStrategyConfiguration();

	/**
	 * Currently active client side requests.
	 */
	private AtomicLong clientSideRequestCount = new AtomicLong(0);
	/**
	 * Currently active server side requests.
	 */
	private AtomicLong serverSideRequestCount = new AtomicLong(0);
	
	@Override
	public void customize(String parameter) {
		//if configuration parameter contains a comma its old style configuration parameter.
		if (parameter.indexOf(',')!=-1)
			customizeFromAnnotationParameter(parameter);
		else
			customizeFromConfigurationName(parameter);
	}
	private void customizeFromConfigurationName(String parameter){
		try{
			ConfigurationManager.INSTANCE.configureAs(configuration, parameter);
		}catch(IllegalArgumentException e){
			throw new IllegalArgumentException("Can't find configuration "+parameter+" for concurrency config");
		}
	}

	private void customizeFromAnnotationParameter(String parameter){
		if (parameter==null || parameter.length()==0)
			throw new IllegalArgumentException("Empty or null parameter, expected clientlimit,serverlimit");
		String limits[] = StringUtils.tokenize(parameter, ',');
		int aClientSideLimit = Integer.parseInt(limits[0]);
		if (aClientSideLimit>0)
			configuration.setClientSideLimit(aClientSideLimit);
		int aServerSideLimit = 0;
		if (limits.length>1)
			aServerSideLimit = Integer.parseInt(limits[1]);
		if (aServerSideLimit>0)
			configuration.setServerSideLimit(aServerSideLimit);
	}

	@Override
	public void notifyClientSideCallStarted(ClientSideCallContext context) {
		if (clientSideRequestCount.incrementAndGet()>configuration.getClientSideLimit()){
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
		if (serverSideRequestCount.incrementAndGet()>configuration.getServerSideLimit()){
			serverSideRequestCount.decrementAndGet();
			throw new ServerRefusedRequestException();
		}
	}

	@Override
	public void notifyServerSideCallFinished(ServerSideCallContext context) {
		serverSideRequestCount.decrementAndGet();
	}
	
	@Override public String toString(){
		return getClass().getSimpleName()+" with limits configuration: "+configuration;
	}
}
