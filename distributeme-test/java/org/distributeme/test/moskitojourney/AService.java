package org.distributeme.test.moskitojourney;

import org.distributeme.annotation.DistributeMe;
import net.anotheria.anoprise.metafactory.Service;

@DistributeMe()
public interface AService extends Service{
	String aMethod(String param) throws AServiceException;
}
