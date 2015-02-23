package org.distributeme.firstexample;

import org.distributeme.annotation.DistributeMe;

import net.anotheria.anoprise.metafactory.Service;

@DistributeMe
public interface FirstService extends Service{
	String greet(String message);
}
