package org.distributeme.test.aggregation.c;

import org.distributeme.annotation.CombinedService;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.test.aggregation.a.AService;
import org.distributeme.test.aggregation.b.BService;


@DistributeMe
@CombinedService(services={AService.class, BService.class})
public interface CService {
	
}
