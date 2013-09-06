package org.distributeme.test.eecho;

import net.anotheria.anoprise.metafactory.ServiceFactory;

public class EEchoServiceFactory implements ServiceFactory<EEchoService>{

	@Override
	public EEchoService create() {
		return new EEchoServiceImpl();
	}

}
