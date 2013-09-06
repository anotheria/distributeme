package org.distributeme.test.asynch;

import net.anotheria.anoprise.metafactory.ServiceFactory;

public class TestServiceFactory implements ServiceFactory<TestService> {

	@Override
	public TestService create() {
		return new TestServiceImpl();
	}

}
