package org.distributeme.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A shutdown hook that un-registers a service upon jvm exit.
 *
 * @author another
 * @version $Id: $Id
 */
public class ServerShutdownHook extends Thread{
	
	/**
	 * Log.
	 */
	private static Logger log = LoggerFactory.getLogger(ServerShutdownHook.class);
	
	/**
	 * Associated descriptor.
	 */
	private ServiceDescriptor targetDescriptor;
	
	/**
	 * <p>Constructor for ServerShutdownHook.</p>
	 *
	 * @param aDescriptor a {@link org.distributeme.core.ServiceDescriptor} object.
	 */
	public ServerShutdownHook(ServiceDescriptor aDescriptor){
		targetDescriptor = aDescriptor;
	}
	
	/** {@inheritDoc} */
	@Override public void run(){
		log.info("Unregister "+targetDescriptor);
		try{
			boolean result = RegistryUtil.unbind(targetDescriptor);
			log.info(result?  "\tsuccess" : "\terror");
		}catch(Exception e){
			log.error("run()", e);
		}
	}
	
}
