package org.distributeme.support.lifecycle;

import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * Factory for LifecycleSupportService.
 */
public class LifecycleSupportServiceFactory implements ServiceFactory<LifecycleSupportService>{

	@Override
	public LifecycleSupportService create() {
		return new LifecycleSupportServiceImpl();
	}
	
}
