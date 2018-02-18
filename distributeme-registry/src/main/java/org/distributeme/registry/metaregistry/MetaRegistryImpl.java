package org.distributeme.registry.metaregistry;

import net.anotheria.moskito.aop.annotation.Monitor;
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
@Monitor (producerId = "ServiceRegistry", category = "registry", subsystem = "distributeme")
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

	private BindUnbindResolveCounter counter = new BindUnbindResolveCounter();
	
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
		counter.bind(service.getGlobalServiceId());
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
		counter.resolve(serviceId);
		return bindings.get(serviceId);
	}

	@Override
	public boolean unbind(ServiceDescriptor service) {
		counter.unbind(service.getGlobalServiceId());
		//for https://github.com/anotheria/distributeme/issues/22
		//check old instance a) is there an old?
		ServiceDescriptor storedDescriptor = bindings.get(service.getGlobalServiceId());
		if (storedDescriptor==null)
			return false;
		//does the instance id match?
		if (!storedDescriptor.getInstanceId().equals(service.getInstanceId()))
			return false;
		bindings.remove(service.getGlobalServiceId());
		for (MetaRegistryListener listener : listeners){
			try{
				listener.onUnbind(service);
			}catch(Exception any){
				log.warn("Exception in listener on unbind, cught.", any);
			}
		}
		return true;
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
		ArrayList<ServiceDescriptor> ret = new ArrayList<ServiceDescriptor>();
		ret.addAll(bindings.values());
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
		bindings = new ConcurrentHashMap<String, ServiceDescriptor>();
		listeners = new CopyOnWriteArrayList<MetaRegistryListener>();		
	}
	 

}
