package org.distributeme.test.eecho;

import org.distributeme.annotation.DistributeMe;
import org.distributeme.test.echo.EchoService;

/**
 * This interface is a test for extension generation.
 * @author lrosenberg.
 *
 */
@DistributeMe()
public interface EEchoService extends EchoService{
	/**
	 * A method unique to the overriding interface.
	 * @param param
	 * @return
	 * @throws EEchoServiceException
	 */
	long eecho(long param) throws EEchoServiceException;
}
 