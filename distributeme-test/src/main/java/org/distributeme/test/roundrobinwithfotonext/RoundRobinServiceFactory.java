package org.distributeme.test.roundrobinwithfotonext;


import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * Service factory.
 */
public class RoundRobinServiceFactory implements ServiceFactory<RoundRobinService>{

	@Override
	public RoundRobinService create() {
		return new RoundRobinServiceImpl();
	}

}
