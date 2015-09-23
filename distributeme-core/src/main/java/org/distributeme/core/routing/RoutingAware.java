package org.distributeme.core.routing;

/**
 * This interface is offering support for custom services. By implementing this interface your service will
 * a) mark himself as knowing about routing. b) allow DistributeMe to submit some routing/sharding relevant information.
 * IMPORTANT: This only works with class-level routers.
 *
 * @author lrosenberg
 * @since 23.09.15 22:52
 */
public interface RoutingAware {

	/**
	 * Called when the service id of the service instance is determined and the service is registered..
	 * @param definedServiceId
	 * @param registeredAsServiceId
	 * @param routerParameter value of the @Route annotation parameter.
	 * @param routerConfigurationName value of the router configuration name parameter. Allows you to actively watch the same config.
	 */
	void notifyServiceId(String definedServiceId, String registeredAsServiceId, String routerParameter, String routerConfigurationName);
}
