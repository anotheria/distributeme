package org.distributeme.test.laecho;

import net.anotheria.anoprise.metafactory.Service;

import org.distributeme.annotation.DistributeMe;
import org.distributeme.core.lifecycle.LifecycleAware;

@DistributeMe()
public interface LifecycleAwareEchoService extends Service,LifecycleAware {
	long echo(long parameter) throws LifecycleAwareEchoServiceException;

	void printHello() throws LifecycleAwareEchoServiceException;


}
