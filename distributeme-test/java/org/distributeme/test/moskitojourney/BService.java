package org.distributeme.test.moskitojourney;

import org.distributeme.annotation.DistributeMe;
import net.anotheria.anoprise.metafactory.Service;
@DistributeMe
public interface BService extends Service{
	public String bMethod(String string) throws BServiceException;

}
