package org.distributeme.test.blacklisting;

import org.distributeme.core.ServiceLocator;


public class BlacklistingTestServiceClient {

	public static void main(String[] args) {
		int counter = 0;
		BlacklistingTestService blacklistingTestService = ServiceLocator.getRemote(BlacklistingTestService.class);

		while (true) {
			counter++;

			try {
				blacklistingTestService.doSomeThing(counter);
				System.err.println("success ");
			} catch (Exception exception) {
				System.err.println("exception ");

			}

		}
	}
}
