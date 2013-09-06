package org.distributeme.test.failingandrr;

import net.anotheria.anoprise.metafactory.Service;

import org.distributeme.annotation.ConcurrencyControlServerSideLimit;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.DontRoute;
import org.distributeme.annotation.FailBy;
import org.distributeme.annotation.Route;
import org.distributeme.annotation.RouteMe;
import org.distributeme.core.routing.PropertyBasedRegistrationNameProvider;
import org.distributeme.core.routing.RoundRobinRouter;
import org.distributeme.core.routing.RoundRobinRouterWithFailoverToNextNode;
/**
 * This is a testinterface for different round-robin-routing with failover test cases.
 * @author lrosenberg
 *
 */
@DistributeMe
@RouteMe(providerClass=PropertyBasedRegistrationNameProvider.class, providerParameter="instanceId")
@Route(routerClass=RoundRobinRouterWithFailoverToNextNode.class,routerParameter="2")
@FailBy(strategyClass=RoundRobinRouterWithFailoverToNextNode.class, reuseRouter=true)
@ConcurrencyControlServerSideLimit(100)
public interface TestService extends Service{
	@ConcurrencyControlServerSideLimit(200)
	long echo(long test);
	
	@DontRoute void dontroute();
	
	@Route(routerClass=RoundRobinRouter.class) void route();
	
	@FailBy(strategyClass=RoundRobinRouterWithFailoverToNextNode.class, reuseRouter=true)
	long routeEcho(long test);
}
