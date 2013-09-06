package org.distributeme.agents.transporter;

import net.anotheria.anoprise.metafactory.ServiceFactory;

public class TransporterServiceFactory implements ServiceFactory<TransporterService>{

	@Override
	public TransporterService create() {
		return new TransporterServiceImpl();
	}

}
