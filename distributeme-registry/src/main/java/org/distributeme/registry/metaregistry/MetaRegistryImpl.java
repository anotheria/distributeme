package org.distributeme.registry.metaregistry;

import org.distributeme.core.ServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementation of the MetaRegistry.	
 * @author lrosenberg.
 *
 */
public final class MetaRegistryImpl implements MetaRegistry{
	
	/**
	 * Configured service bindings.
	 */
	private ConcurrentHashMap<String,ServiceDescriptor> bindings;
	
	/**
	 * Registry listeners.
	 */
	private List<MetaRegistryListener> listeners;
	
	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(MetaRegistryImpl.class);
	
	/**
	 * Constructor.
	 */
	private MetaRegistryImpl() {
		reset();
	}

	public static MetaRegistry getInstance() {
		return MetaRegistryImplHolder.registry;
	}

	@Override
	public boolean bind(ServiceDescriptor service) {
		bindings.put(service.getGlobalServiceId(), service);
		for (MetaRegistryListener listener : listeners){
			try{
				listener.onBind(service);
			}catch(Exception any){
				log.warn("Exception in listener on unbind, cught.", any);
			}
		}
		return true;
	}

	@Override
	public ServiceDescriptor resolve(String serviceId) {
		return bindings.get(serviceId);
	}

	@Override
	public boolean unbind(ServiceDescriptor service) {
		boolean ret = bindings.remove(service.getGlobalServiceId())!=null;
		if (ret){
			for (MetaRegistryListener listener : listeners){
				try{
					listener.onUnbind(service);
				}catch(Exception any){
					log.warn("Exception in listener on unbind, cught.", any);
				}
			}
		}
		return ret;
	}
	
	@Override
	public void remoteUnbind(ServiceDescriptor service) {
		bindings.remove(service.getGlobalServiceId());
	}

	@Override
	public void remoteBind(ServiceDescriptor service) {
		bindings.put(service.getGlobalServiceId(), service);
	}

	@Override public List<ServiceDescriptor> list(){
		List<ServiceDescriptor> ret = new ArrayList<>(bindings.values());
        return ret;
	}

	private static class MetaRegistryImplHolder {
		/**
		 * Singleton instance.
		 */
		private static MetaRegistry registry = new MetaRegistryImpl();
	}

	@Override
	public void addListener(MetaRegistryListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(MetaRegistryListener listener) {
		listeners.remove(listener);
	}
	
	void reset(){
		bindings = new ConcurrentHashMap<>();
		listeners = new CopyOnWriteArrayList<>();
	}
	 

}
