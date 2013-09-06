package org.distributeme.test.combined;

import org.distributeme.annotation.CombinedService;
import org.distributeme.annotation.DistributeMe;

@DistributeMe
@CombinedService(services={AdminService.class, BusinessService.class})
public interface BusinessAndAdminService{
	//this interface is empty, its only used as a control of the generator.
}
