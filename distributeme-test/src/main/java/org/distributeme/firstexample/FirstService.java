package org.distributeme.firstexample;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;

/**
 * An example service.
 */
@DistributeMe
public interface FirstService extends Service{
	/**
	 * Sends a greeting to the other side.
	 * @param message
	 * @return
	 */
	String greet(String message);
}
