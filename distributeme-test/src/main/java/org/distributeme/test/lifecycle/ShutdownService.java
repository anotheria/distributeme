package org.distributeme.test.lifecycle;

import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.lifecycle.LifecycleComponent;
import org.distributeme.support.lifecycle.generated.LifecycleSupportServiceConstants;
import org.distributeme.support.lifecycle.generated.RemoteLifecycleSupportServiceStub;

public class ShutdownService {
	public static void main(String[] args) {
		if (args.length==0){
			System.out.println("Use ./start.sh "+ShutdownAll.class.getName()+" <servicedescriptor>");
			System.out.println("for example ./start.sh "+ShutdownAll.class.getName()+" rmi://org_distributeme_test_echo_EchoService.aacpkpybrd@192.168.200.101:9250");
			System.exit(1);
		}
		
		ServiceDescriptor sd = ServiceDescriptor.fromSystemWideUniqueId(args[0]);
		System.out.println("Parsed ServiceDescriptor: "+sd);
		ServiceDescriptor target = sd.changeServiceId(LifecycleSupportServiceConstants.getServiceId());
		LifecycleComponent service = new RemoteLifecycleSupportServiceStub(target);
		service.shutdown("Killed by "+ShutdownService.class);
		
	}
}
