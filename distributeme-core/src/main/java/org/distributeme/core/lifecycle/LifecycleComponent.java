package org.distributeme.core.lifecycle;

import java.util.List;
import java.util.Map;

/**
 * Defines an internal component that controls lifecycle of services inside of distributeme distributed vms.
 * @author lrosenberg
 */
public interface LifecycleComponent {
	/**
	 * Returns true if the service is online. Actually this method always returns true since all services are active for now, however you can call
	 * this method from a remote stub to detect whether the service replies at all.
	 * @return
	 */
	boolean isOnline();
	/**
	 * Prints a standard message to system out. 
	 */
	void printStatusToSystemOut();
	/**
	 * Logs a standard message to info. 
	 */
	void printStatusToLogInfo();
	
	/**
	 * Returns a list of all publicly accessable services. Publicly accessable in this context means that the service is accessible via the remote interface. It doesn't include 
	 * support services like event service bridge or lifecycle service itself, which are meant to have another usage.
	 * @return
	 */
	List<String> getPublicServices();
	
	/**
	 * Registers a service locally running service instance.
	 * @param descriptor
	 * @param instance
	 */
	void registerPublicService(String serviceId, ServiceAdapter instance);

	/**
	 * Collects and returns the info about the specified service.  
	 * @param serviceId the target service id.
	 * @return
	 */
	ServiceInfo getServiceInfo(String serviceId);
	
	/**
	 * Shutdowns the current VM and logs the message.
	 * @param message
	 */
	void shutdown(String message);
	/**
	 * Returns the health status of a given service.
	 * @param serviceId
	 * @return
	 */
	HealthStatus getHealthStatus(String serviceId);
	/**
	 * Returns map with health status objects for all contained public services (usually one!).
	 * @return
	 */
	Map<String, HealthStatus> getHealthStatuses();
}
