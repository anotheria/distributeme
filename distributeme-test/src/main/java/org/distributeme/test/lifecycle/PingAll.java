package org.distributeme.test.lifecycle;

import java.util.List;

import org.distributeme.core.RegistryUtil;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.support.lifecycle.LifecycleSupportService;
import org.distributeme.support.lifecycle.generated.LifecycleSupportServiceConstants;
import org.distributeme.support.lifecycle.generated.RemoteLifecycleSupportServiceStub;

public class PingAll extends AbstractLifecycleTest{
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
				
				List<String> publicServices = service.getPublicServices();
				System.out.println("Remote VM offers following publicly accessable services: "+publicServices);
				for (String sid : publicServices){
					System.out.println("service "+sid+" info "+service.getServiceInfo(sid));
				}
				
			}catch(Exception e){
				System.out.println("Failed!");
				e.printStackTrace();
			}
			System.out.println("===========");
		}
		
	}
	
}
