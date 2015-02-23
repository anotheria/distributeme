package org.distributeme.test.inheritance;

import org.distributeme.annotation.DistributeMe;

import net.anotheria.anoprise.metafactory.Service;

@DistributeMe
public interface BaseService extends Service{
	long echo(long value);
}
