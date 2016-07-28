package org.distributeme.test.moskitojourney;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
@DistributeMe
public interface CService extends Service{
	String cMethod(String string) throws CServiceException;
}
