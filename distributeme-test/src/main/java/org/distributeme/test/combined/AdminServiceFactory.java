package org.distributeme.test.combined;

import net.anotheria.anoprise.metafactory.ServiceFactory;

public class AdminServiceFactory implements ServiceFactory<AdminService>{

	@Override
	public AdminService create() {
		return CombinedServiceImpl.INSTANCE;
	}

}
