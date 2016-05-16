package org.distributeme.test.fail;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.annotation.Route;
import org.distributeme.core.failing.*;
import org.distributeme.core.failing.FailoverAndReturnInTenSeconds;

@DistributeMe
@FailBy(strategyClass=FailCall.class)
public interface FailableService extends Service{

	@FailBy(strategyClass=FailCall.class)
	void failableMethod();
	
	@FailBy(strategyClass=RetryCall.class)
	void retryMethod();
	
	@FailBy(strategyClass=RetryCallOnce.class)
	void retryOnceMethod();
	
	void defaultMethod();

	@FailBy(strategyClass=FailCall.class)
	long failableEcho(long value);
	
	@FailBy(strategyClass=WaitOneSecondAndRetry.class)
	long retryEcho(long value);
	
	@FailBy(strategyClass=RetryCallOnce.class)
	long retryOnceEcho(long value);
	
	@FailBy(strategyClass=Failover.class)
	long failoverEcho(long value);

	@FailBy(strategyClass=Failover.class)
	void failoverPrint(String message);

	@Route(routerClass=Failover.class)
	@FailBy(strategyClass=Failover.class)
	void failoverPrintAndStay(String message);

	@Route(routerClass=FailoverAndReturnInTenSeconds.class)
	@FailBy(strategyClass=FailoverAndReturnInTenSeconds.class)
	void failoverPrintAndStayFoTenSeconds(String message);
}
 