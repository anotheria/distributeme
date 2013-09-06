package org.distributeme.test.jaxrs;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.core.ServiceDescriptor;

@DistributeMe (protocols = {ServiceDescriptor.Protocol.JAXRS})
public interface FirstJaxRsService extends Service{
	String greet(String message);

	long echo(long echo);
}
