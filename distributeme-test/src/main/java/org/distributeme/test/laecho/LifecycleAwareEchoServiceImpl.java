package org.distributeme.test.laecho;

import org.distributeme.core.lifecycle.Health;
import org.distributeme.core.lifecycle.HealthStatus;


public class LifecycleAwareEchoServiceImpl implements LifecycleAwareEchoService{


	@Override
	public long echo(long parameter) {
		return parameter;
	}

	@Override
	public void printHello() {
		System.out.println("Hello World!");
	}

	@Override
	public HealthStatus getHealthStatus() {
		HealthStatus ret = new HealthStatus(Health.YELLOW, "Yellow just for test");
		return ret;
	}

	
}
