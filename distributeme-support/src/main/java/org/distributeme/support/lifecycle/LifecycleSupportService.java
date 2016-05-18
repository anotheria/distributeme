package org.distributeme.support.lifecycle;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.SupportService;
import org.distributeme.core.lifecycle.LifecycleComponent;

@DistributeMe(moskitoSupport=false, factoryClazz=LifecycleSupportServiceFactory.class)
@SupportService
public interface LifecycleSupportService extends LifecycleComponent,Service{

}
