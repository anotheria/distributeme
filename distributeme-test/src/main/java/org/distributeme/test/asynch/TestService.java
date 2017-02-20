package org.distributeme.test.asynch;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;

@DistributeMe(asynchSupport=true, asynchCallTimeout=2000)
public interface TestService extends Service {
	
	long ping(long param);
	
	long sleepAndReturnRandom(long sleepTime);
	
	void sleep(long sleepTime);
	
	void sleepAndThrowTypedException(long sleepTime) throws TestServiceException;
	void sleepAndThrowRuntimeException(long sleepTime);
}
