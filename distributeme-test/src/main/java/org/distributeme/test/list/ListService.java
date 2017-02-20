package org.distributeme.test.list;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.annotation.Route;
import org.distributeme.annotation.RouteMe;
import org.distributeme.core.routing.PropertyBasedRegistrationNameProvider;

import java.util.Collection;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 22.09.15 00:57
 */
@DistributeMe
@RouteMe(providerClass=PropertyBasedRegistrationNameProvider.class, providerParameter="instanceId")
@Route(routerClass=ListRouter.class, routerParameter="", configurationName = "list-config")
@FailBy(strategyClass=ListRouter.class, reuseRouter = true)
public interface ListService extends Service {
	ListObject getListObject(ListObjectId id);

	Collection<ListObject> getListObjects();

	Collection<ListObject> getSomeListObjects(Collection<ListObjectId> ids) ;

	Collection<ListObject> getListObjectsSharded();

	Collection<ListObject> getSomeListObjectsSharded(Collection<ListObjectId> ids) ;
}
