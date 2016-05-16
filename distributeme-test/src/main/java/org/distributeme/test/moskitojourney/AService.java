package org.distributeme.test.moskitojourney;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;

@DistributeMe()
public interface AService extends Service{
	String aMethod(String param) throws AServiceException;
}
