package org.distributeme.test.echoplusevent;

import net.anotheria.anoprise.metafactory.ServiceFactory;

public class EchoPlusEventServiceFactory implements ServiceFactory<EchoPlusEventService>{
	public EchoPlusEventService create(){
		return new EchoPlusEventServiceImpl();
	}
}
