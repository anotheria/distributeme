package org.distributeme.test.inheritance;

public class BServiceImpl  implements BService{

	@Override
	public long echo(long value) {
		return -1*value;
	}

}
