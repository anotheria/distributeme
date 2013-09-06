package org.distributeme.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A shutdown hook that unregisters a service upon jvm exit.
 * @author another
 *
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
	
	public ServerShutdownHook(ServiceDescriptor aDescriptor){
		targetDescriptor = aDescriptor;
	}
	
	@Override public void run(){
		log.info("Unregistering "+targetDescriptor);
		try{
			boolean result = RegistryUtil.unbind(targetDescriptor);
			log.info(result?  "\tsuccess" : "\terror");
		}catch(Exception e){
			log.error("run()", e);
		}
	}
	
}
