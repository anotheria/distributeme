package org.distributeme.test.aggregation.b;

import net.anotheria.anoprise.metafactory.Service;

import org.distributeme.annotation.DistributeMe;

@DistributeMe
public interface BService extends Service{
	void bMethod();
}
