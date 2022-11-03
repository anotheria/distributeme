package org.distributeme.test.moskitojourney;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

@DistributeMe()
@FailBy(strategyClass = RetryCallOnce.class)
public interface AService extends Service{
	String aMethod(String param) throws AServiceException;
}
