package org.distributeme.test.moskitojourney;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

@DistributeMe
@FailBy(strategyClass = RetryCallOnce.class)
public interface CService extends Service{
	public String cMethod(String string) throws CServiceException;
}
