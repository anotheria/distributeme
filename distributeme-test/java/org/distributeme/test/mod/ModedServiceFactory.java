package org.distributeme.test.mod;

import net.anotheria.anoprise.metafactory.ServiceFactory;

public class ModedServiceFactory implements ServiceFactory<ModedService>{

	@Override
	public ModedService create() {
		return new ModedServiceImpl();
	}

}
