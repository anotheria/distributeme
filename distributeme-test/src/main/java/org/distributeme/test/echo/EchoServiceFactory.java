package org.distributeme.test.echo;

import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * Factory to create new EchoService instances.
 */
public class EchoServiceFactory implements ServiceFactory<EchoService>{
	@Override
	public EchoService create(){
		return new EchoServiceImpl();
	}
}
