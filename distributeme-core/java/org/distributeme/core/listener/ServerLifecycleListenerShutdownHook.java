package org.distributeme.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A shutdown hook that unregisters a service upon jvm exit.
 * @author another
 *
 */
public class ServerLifecycleListenerShutdownHook extends Thread{
	
	/**
	 * Log.
	 */
	private static Logger log = LoggerFactory.getLogger(ServerLifecycleListenerShutdownHook.class);

	/**
	 * Listeners which have to perform operations prior to shut down.
	 * @see ServerLifecycleListener
	 */
	private List<ServerLifecycleListener> listeners;
	
	public ServerLifecycleListenerShutdownHook(List<ServerLifecycleListener> someListeners){
		listeners = someListeners;
	}

	@Override public void run(){
		//embedded listeners
		if (listeners!=null && listeners.size()>0){
			for (ServerLifecycleListener listener : listeners){
				try{
					listener.beforeShutdown();
				}catch(Exception e){
					log.warn("Exception in listener "+listener.getClass(),e);
				}
			}
		}
		
		//configured listeners
		List<ServerLifecycleListener> configuredListeners = ListenerRegistry.getInstance().getServerLifecycleListeners();
		if (configuredListeners!=null && configuredListeners.size()>0){
			for (ServerLifecycleListener listener : configuredListeners){
				try{
					listener.beforeShutdown();
				}catch(Exception e){
					log.warn("Exception in listener "+listener.getClass(),e);
				}
			}
		}
	}
	
	
}
