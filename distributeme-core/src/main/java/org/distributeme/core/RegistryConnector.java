package org.distributeme.core;

import java.util.List;
import java.util.Map;


/**
 * Created by rboehling on 2/28/17.
 */
public interface RegistryConnector {

	String describeRegistry();

	boolean bind(ServiceDescriptor service);

	boolean notifyBind(Location location, ServiceDescriptor descriptor);

	boolean notifyUnbind(Location location, ServiceDescriptor descriptor);

	boolean unbind(ServiceDescriptor service);

	ServiceDescriptor resolve(ServiceDescriptor toResolve, Location loc);

	void setTagableSystemProperties(Map<String, String> tagableSystemProperties);

	void setCustomTagProviderClassList(List<String> customTagProviderClassList);
}
