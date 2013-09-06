package org.distributeme.test.roundrobin;

import net.anotheria.anoprise.metafactory.Service;

import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.Route;
import org.distributeme.annotation.RouteMe;
import org.distributeme.core.routing.PropertyBasedRegistrationNameProvider;
import org.distributeme.core.routing.RoundRobinRouter;

@DistributeMe(
		initcode={"MetaFactory.addFactoryClass(org.distributeme.test.roundrobin.RoundRobinService.class, Extension.LOCAL, org.distributeme.test.roundrobin.RoundRobinServiceFactory.class);"}
)
@RouteMe(providerClass=PropertyBasedRegistrationNameProvider.class, providerParameter="instanceId")
@Route(routerClass=RoundRobinRouter.class, routerParameter="3") 
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
}
