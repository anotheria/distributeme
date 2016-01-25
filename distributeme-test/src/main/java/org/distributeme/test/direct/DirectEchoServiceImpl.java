package org.distributeme.test.direct;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 25.01.16 00:34
 */
public class DirectEchoServiceImpl implements DirectEchoService {
	@Override
	public int echo(int value) throws DirectServiceException {
		System.out.println("Request: "+value);
		return value;
	}
}
