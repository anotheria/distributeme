package org.distributeme.registry.metaregistry;

import net.anotheria.moskito.aop.annotation.CountByParameter;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 18.02.18 17:18
 */
public class BindUnbindResolveCounter {
	@CountByParameter(producerId = "Operation.Bind", subsystem = "distributeme", category="operation")
	public void bind(String serviceId){}

	@CountByParameter(producerId = "Operation.Unbind", subsystem = "distributeme", category="operation")
	public void unbind(String serviceId){}

	@CountByParameter(producerId = "Operation.Resolve", subsystem = "distributeme", category="operation")
	public void resolve(String serviceId){}
}
