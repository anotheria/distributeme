package org.distributeme.test.inheritance;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;

@DistributeMe
public interface BaseService extends Service{
	long echo(long value);
}
