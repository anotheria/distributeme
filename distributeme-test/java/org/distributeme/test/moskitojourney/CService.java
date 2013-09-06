package org.distributeme.test.moskitojourney;

import org.distributeme.annotation.DistributeMe;
import net.anotheria.anoprise.metafactory.Service;
@DistributeMe
public interface CService extends Service{
	public String cMethod(String string) throws CServiceException;
}
