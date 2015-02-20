package org.distributeme.test.roundrobinwithfotonext.client;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.ServiceFactory;

import org.distributeme.test.roundrobin.RoundRobinService;

public abstract class Client {
	static{
		try{
			MetaFactory.addFactoryClass(RoundRobinService.class, Extension.REMOTE, (Class<ServiceFactory<RoundRobinService>>)Class.forName("org.distributeme.test.roundrobin.generated.RemoteRoundRobinServiceFactory"));
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			throw new AssertionError("Not properly generated, factory is missing");
		}
	}
}
 