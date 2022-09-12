package org.distributeme.test.fail;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.annotation.Route;
import org.distributeme.core.failing.*;
import org.distributeme.core.failing.FailoverAndReturnInTenSeconds;

/**
 * This interface shows different types of failing strategies supported by distribute me.
 */
@DistributeMe
@FailBy(strategyClass=FailCall.class)
public interface FailableService extends Service{

	/**
	 * This strategy will do nothing if the method fails, it will simply fail.
	 */
	@FailBy(strategyClass= FailCall.class)
	void failableMethod();

	/**
	 * The call will be retried indefinitely.
	 */
	@FailBy(strategyClass= RetryCall.class)
	void retryMethod();

	/**
	 * The call will be retried exactly once. This is very useful if the service is not asked often and is restarted in background.
	 */
	@FailBy(strategyClass= RetryCallOnce.class)
	void retryOnceMethod();

	/**
	 * A method without custom strategy, this will use the class wide strategy.
	 */
	void defaultMethod();

	@FailBy(strategyClass=FailCall.class)
	long failableEcho(long value);

	/**
	 * This method will wait one second before retry.
	 * @param value
	 * @return
	 */
	@FailBy(strategyClass= WaitOneSecondAndRetry.class)
	long retryEcho(long value);
	
	@FailBy(strategyClass=RetryCallOnce.class)
	long retryOnceEcho(long value);

	/**
	 * Will failover to backup instance if failed.
	 * @param value
	 * @return
	 */
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
 