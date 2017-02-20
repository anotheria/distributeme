package org.distributeme.test.blacklisting;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.annotation.Route;
import org.distributeme.annotation.RouteMe;
import org.distributeme.core.routing.PropertyBasedRegistrationNameProvider;


@DistributeMe
@RouteMe(providerClass=PropertyBasedRegistrationNameProvider.class, providerParameter="instanceId")
@Route(routerClass=BlacklistingRouter.class, routerParameter="", configurationName = "blacklist-router-config")
@FailBy(strategyClass=BlacklistingRouter.class, reuseRouter = true)
public interface BlacklistingTestService extends Service{
	public void doSomeThing(int mod);

}
