package org.distributeme.firstexample;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;

@DistributeMe
public interface FirstService extends Service{
	String greet(String message);
}
