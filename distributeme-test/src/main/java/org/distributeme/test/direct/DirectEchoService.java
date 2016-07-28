package org.distributeme.test.direct;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;

/**
 * This service is used to test/demonstrate direct connection, without registry.
 *
 * @author lrosenberg
 * @since 25.01.16 00:32
 */
@DistributeMe
public interface DirectEchoService extends Service {
	int echo(int value) throws DirectServiceException;
}
