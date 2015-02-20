package org.distributeme.test.roundrobinwithfotonext;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.annotation.Route;
import org.distributeme.annotation.RouteMe;
import org.distributeme.core.routing.PropertyBasedRegistrationNameProvider;
import org.distributeme.core.routing.RoundRobinRouterWithStickyFailoverToNextNode;

@DistributeMe()
@RouteMe(providerClass=PropertyBasedRegistrationNameProvider.class, providerParameter="instanceId")
@Route(routerClass=RoundRobinRouterWithStickyFailoverToNextNode.class, routerParameter="services=3,timeout=5000", configurationName = "router-config")
@FailBy(strategyClass=RoundRobinRouterWithStickyFailoverToNextNode.class, reuseRouter = true)
public interface RoundRobinService extends Service{
	/**
	 * Just a method. This one will probably add the two submitted values.
	 * @param a
	 * @param b
	 * @return
	 */
	int add(int a, int b);
	/**
	 * Returns a randomly created id of an instance. The id is created at initialization and remains until restart.
	 * @return
	 */
	String getRandomId();
	
	void print(String parameter);

	void printResults();
}
