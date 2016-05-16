package org.distributeme.test.aggregation.a;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;

@DistributeMe
public interface AService extends Service{
	void aMethod();
}
