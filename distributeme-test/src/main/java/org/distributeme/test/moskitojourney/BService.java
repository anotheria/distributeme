package org.distributeme.test.moskitojourney;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
@DistributeMe
public interface BService extends Service{
	public String bMethod(String string) throws BServiceException;

}
