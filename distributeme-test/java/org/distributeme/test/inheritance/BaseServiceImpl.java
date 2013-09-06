package org.distributeme.test.inheritance;

public class BaseServiceImpl implements BaseService{

	@Override
	public long echo(long value) {
		return value/2;
	}

}
