package org.distributeme.core.lifecycle;

import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of the LifecycleComponent. This one is a singleton.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public enum LifecycleComponentImpl implements LifecycleComponent{
	/**
	 * The singleton instance.
	 */
	INSTANCE;

	/**
	 * Internal map with publicly available services.
	 */
	private ConcurrentMap<String, ServiceAdapter> publicServices;
	/**
	 * Singleton constructor.
	 */
	private LifecycleComponentImpl(){
		publicServices = new ConcurrentHashMap<String, ServiceAdapter>();
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isOnline() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void printStatusToSystemOut() {
		System.out.println(LifecycleComponent.class+" is online.");
	}

	/** {@inheritDoc} */
	@Override
	public void printStatusToLogInfo() {
		LoggerFactory.getLogger(LifecycleComponent.class).info(LifecycleComponent.class+" is online.");
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getPublicServices() {
		ArrayList<String> ret = new ArrayList<String>();
		ret.addAll(publicServices.keySet());
		return ret;
	}

	/** {@inheritDoc} */
	@Override
	public void registerPublicService(String descriptor,
			ServiceAdapter instance) {
		publicServices.put(descriptor, instance);
	}

	/** {@inheritDoc} */
	@Override
	public ServiceInfo getServiceInfo(String serviceId) {
		ServiceAdapter adapter = publicServices.get(serviceId);
		ServiceInfo info = new ServiceInfo();
		info.setServiceId(serviceId);
		try{
			info.setLastAccessTimestamp(adapter.getLastAccessTimestamp());
			info.setCreationTimestamp(adapter.getCreationTimestamp());
		}catch(RemoteException willNeverHappen){
			// the exception will never be thrown here, because its only an excuse for rmi semantic errors.
			throw new AssertionError("The moon is falling into the sea!");
		}
		return info;
	}
	
	/** {@inheritDoc} */
	@Override public void shutdown(final String message){
		new Thread(){
			public void run(){
				try{
					Thread.sleep(500);
				}catch(InterruptedException e){//ignored
					
				}
				System.out.println("Remote shutdown initiated with message: "+message);
				System.exit(0);
			}
		}.start();
	}

	/** {@inheritDoc} */
	@Override
	public HealthStatus getHealthStatus(String serviceId) {
		ServiceAdapter adapter = publicServices.get(serviceId);
		if (adapter instanceof LifecycleAware){
			return ((LifecycleAware)adapter).getHealthStatus();
		}else{
			return HealthStatus.OK();
		}
	}

	/** {@inheritDoc} */
	@Override
	public Map<String, HealthStatus> getHealthStatuses() {
		HashMap<String, HealthStatus> ret = new HashMap<String, HealthStatus>();
		for (String id : publicServices.keySet())
			ret.put(id, getHealthStatus(id));
		return ret;
	}

}
