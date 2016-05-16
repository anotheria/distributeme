package org.distributeme.test.lifecycle;

import org.distributeme.core.RegistryUtil;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.lifecycle.HealthStatus;
import org.distributeme.support.lifecycle.LifecycleSupportService;
import org.distributeme.support.lifecycle.generated.LifecycleSupportServiceConstants;
import org.distributeme.support.lifecycle.generated.RemoteLifecycleSupportServiceStub;

import java.util.List;
import java.util.Map;

public class CheckHealthStatus extends AbstractLifecycleTest{
	public static void main(String[] args) throws Exception{
		String serviceListAsXML = RegistryUtil.getXMLServiceList();
		System.out.println("Servicelist: "+serviceListAsXML);
		
		//create list of service descriptors:
		List<ServiceDescriptor> descriptors = parse(serviceListAsXML);
		System.out.println("parsed list "+descriptors);
		
		// ... //
		for (ServiceDescriptor descriptor : descriptors){
			System.out.println("Pinging "+descriptor.getSystemWideUniqueId());
			
			ServiceDescriptor lifeCycleDescriptor = descriptor.changeServiceId(LifecycleSupportServiceConstants.getServiceId());
			try{
				LifecycleSupportService service = new RemoteLifecycleSupportServiceStub(lifeCycleDescriptor);
			
				System.out.println(" Service online: "+service.isOnline());
				service.printStatusToSystemOut();
				
				//now check status.
				Map<String,HealthStatus> statuses = service.getHealthStatuses();
				System.out.println("Statuses: "+statuses);
			}catch(Exception e){
				System.out.println("Failed!");
				e.printStackTrace();
			}
			System.out.println("===========");
		}
		
	}
	
}
