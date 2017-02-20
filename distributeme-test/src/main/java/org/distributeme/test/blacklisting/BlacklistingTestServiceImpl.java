package org.distributeme.test.blacklisting;

public class BlacklistingTestServiceImpl implements BlacklistingTestService {

	@Override
	public void doSomeThing(int mod) {
		System.out.println("mod " + mod);
	}
}
