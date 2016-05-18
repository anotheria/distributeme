package org.distributeme.test.combined;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.SupportService;

@DistributeMe
@SupportService
public interface AdminService extends Service{
	void adminMethod() throws AdminServiceException;
}
 