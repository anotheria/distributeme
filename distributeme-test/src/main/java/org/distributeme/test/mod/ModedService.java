package org.distributeme.test.mod;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.DontRoute;
import org.distributeme.annotation.Route;
import org.distributeme.annotation.RouteMe;
import org.distributeme.core.routing.ParameterBasedModRouter;
import org.distributeme.core.routing.PropertyBasedRegistrationNameProvider;
import org.distributeme.core.routing.SysOutRouter;

@DistributeMe(
		initcode={"MetaFactory.addFactoryClass(org.distributeme.test.mod.ModedService.class, Extension.LOCAL, org.distributeme.test.mod.ModedServiceFactory.class);"}
)
@RouteMe(providerClass=PropertyBasedRegistrationNameProvider.class, providerParameter="mod")
@Route(routerClass=ParameterBasedModRouter.class, routerParameter="2,0") 
public interface ModedService extends Service{
	/**
	 * This parameter is used to demonstrate the moding behaviour.
	 * @param parameter 
	 * @return
	 * @throws ModedServiceException
	 */
	long modEcho(long parameter) throws ModedServiceException;
	
	/**
	 * This method will not be moded.
	 * @param parameter
	 * @return
	 * @throws ModedServiceException
	 */
	@DontRoute long unmodEcho(long parameter) throws ModedServiceException;
	
	@Route(routerClass=ParameterBasedModRouter.class, routerParameter="2,1") 
	boolean modEcho(String dummyParameter, boolean parameter) throws ModedServiceException;

	/**
	 * This method demonstrates that a router can be misused for other purposes too.
	 * @param param
	 * @throws ModedServiceException
	 */
	@Route(routerClass=SysOutRouter.class)
	void printString(String param) throws ModedServiceException;
}
