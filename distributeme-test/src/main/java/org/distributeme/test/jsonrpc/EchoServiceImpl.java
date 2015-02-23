package org.distributeme.test.jsonrpc;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: vitaly
 * Date: 2/5/11
 * Time: 9:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class EchoServiceImpl implements EchoService {
    public EchoServiceImpl() {
    }

    @Override
	public long echo(long aValue) {
		System.out.println("Incoming call: " + aValue);
		return aValue;
	}

	@Override
	public Object echoObjectParam1(Object aValue) throws EchoServiceException {
		return aValue;
	}

    @Override
	public A echoObjectParam(A aValue) throws EchoServiceException {
        System.out.println(aValue);
		return null;
	}

    @Override
    public A echoManyParams(final int ind, final String strNum, final Long incremt) throws EchoServiceException, IOException {
        return new A((int) (ind + Integer.valueOf(strNum) + incremt));
    }
}
