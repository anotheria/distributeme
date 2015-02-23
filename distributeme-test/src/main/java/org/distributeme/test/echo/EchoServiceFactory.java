package org.distributeme.test.echo;

import net.anotheria.anoprise.metafactory.ServiceFactory;

public class EchoServiceFactory implements ServiceFactory<EchoService>{
	public EchoService create(){
		return new EchoServiceImpl();
	}
}
