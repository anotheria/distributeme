package org.distributeme.test.echoplusevent;

import org.distributeme.annotation.DistributeMe;
import org.distributeme.test.echo.EchoService;

@DistributeMe(factoryClazz=EchoPlusEventServiceFactory.class)
public interface EchoPlusEventService extends EchoService{

}
