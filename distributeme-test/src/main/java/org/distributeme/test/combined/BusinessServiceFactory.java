package org.distributeme.test.combined;

import net.anotheria.anoprise.metafactory.ServiceFactory;

public class BusinessServiceFactory implements ServiceFactory<BusinessService>{

	@Override
	public BusinessService create() {
		return CombinedServiceImpl.INSTANCE;
	}

}
