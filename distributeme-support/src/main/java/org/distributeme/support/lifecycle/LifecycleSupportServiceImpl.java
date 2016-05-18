package org.distributeme.support.lifecycle;

import org.distributeme.core.lifecycle.*;

import java.util.List;
import java.util.Map;

public class LifecycleSupportServiceImpl implements LifecycleSupportService{
	
	private LifecycleComponent component = LifecycleComponentImpl.INSTANCE;

	@Override
	public boolean isOnline() {
		return component.isOnline();
	}

	@Override
	public void printStatusToSystemOut() {
		component.printStatusToSystemOut();
	}

	@Override
	public void printStatusToLogInfo() {
		component.printStatusToLogInfo();
	}

	@Override
	public List<String> getPublicServices() {
		return component.getPublicServices();
	}

	@Override
	public void registerPublicService(String serviceId,
			ServiceAdapter instance) {
		throw new AssertionError("This method can only be called locally");
	}

	@Override
	public ServiceInfo getServiceInfo(String serviceId) {
		return component.getServiceInfo(serviceId);
	}

	@Override
	public void shutdown(String message) {
		component.shutdown(message);
		
	}

	@Override
	public HealthStatus getHealthStatus(String serviceId) {
		return component.getHealthStatus(serviceId);
	}

	@Override
	public Map<String, HealthStatus> getHealthStatuses() {
		return component.getHealthStatuses();
	}
	
	

}
