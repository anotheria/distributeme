package org.distributeme.tools;

import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.lifecycle.LifecycleComponent;
import org.distributeme.support.lifecycle.generated.LifecycleSupportServiceConstants;
import org.distributeme.support.lifecycle.generated.RemoteLifecycleSupportServiceStub;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 02.12.14 00:43
 */
public class Ping {
	public static void main(String a[]){
		if (a.length != 2){
			System.out.println("Use java ... "+Ping.class+" host port");
			System.exit(-1);
		}
		String host = a[0];
		int port = Integer.parseInt(a[1]);
		ServiceDescriptor lifeCycleDescriptor = new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, LifecycleSupportServiceConstants.getServiceId(), "any", host, port);
		System.out.println("Trying to connect to "+lifeCycleDescriptor);
		LifecycleComponent service = new RemoteLifecycleSupportServiceStub(lifeCycleDescriptor);

		System.out.println(" Service online: "+service.isOnline());
		System.out.println(" Services: "+service.getPublicServices());
		System.out.println(" Health: "+service.getHealthStatuses());


	}
}
