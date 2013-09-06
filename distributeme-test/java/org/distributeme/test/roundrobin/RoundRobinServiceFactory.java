package org.distributeme.test.roundrobin;

import net.anotheria.anoprise.metafactory.ServiceFactory;

public class RoundRobinServiceFactory implements ServiceFactory<RoundRobinService>{

	@Override
	public RoundRobinService create() {
		return new RoundRobinServiceImpl();
	}

}
