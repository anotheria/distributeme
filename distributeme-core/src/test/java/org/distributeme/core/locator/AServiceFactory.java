package org.distributeme.core.locator;

import net.anotheria.anoprise.metafactory.ServiceFactory;

public class AServiceFactory implements ServiceFactory<AService>{

	@Override
	public AService create() {
		return new AServiceImplWithWrongName();
	}

}
