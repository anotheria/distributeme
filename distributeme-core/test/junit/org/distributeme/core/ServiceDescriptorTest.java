package org.distributeme.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.ServiceDescriptor.Protocol;
import org.junit.Test;

public class ServiceDescriptorTest {
	@Test public void testForNullValues(){
		try{
			new ServiceDescriptor(null, "aaaa", "aaaa", "instance", 1);
			fail("Illegal constructor");
		}catch(IllegalArgumentException e){}

		try{
			new ServiceDescriptor(Protocol.CORBA, "", "aaaa", "instance", 1);
			fail("Illegal constructor");
		}catch(IllegalArgumentException e){}

		try{  
			new ServiceDescriptor(Protocol.CORBA, null, "aaaa", "instance", 1);
			fail("Illegal constructor");
		}catch(IllegalArgumentException e){}

		try{
			new ServiceDescriptor(Protocol.CORBA, "aaaa", "", "instance", 1);
			fail("Illegal constructor");
		}catch(IllegalArgumentException e){}

		try{
			new ServiceDescriptor(Protocol.CORBA, "aaaa", null, "instance", 1);
			fail("Illegal constructor");
		}catch(IllegalArgumentException e){}

		//this shouldn't throw an exception
		new ServiceDescriptor(Protocol.RMI, ServiceDescriptorTest.class.getName(), "instancexyz", "localhost", 1234);
		
	}
	
	@Test public void testParsingAndUnparsing(){
		String host = "localhost";
		int port = 9230;
		String serviceId = ServiceDescriptorTest.class.getName();
		Protocol protocol = Protocol.RMI;
		String instanceId = "instancexyz";
		
		ServiceDescriptor d1 = new ServiceDescriptor(protocol, serviceId, instanceId, host, port);
		//System.out.println(d1.getRegistrationString());
		assertEquals("rmi://"+serviceId+"."+instanceId+"@"+host+":"+port+"@"+ServiceDescriptor.getTimeString(d1.getTimestamp()), d1.getRegistrationString());
		
		String regString = d1.getRegistrationString();
		
		ServiceDescriptor d2 = ServiceDescriptor.fromRegistrationString(regString);
		
		assertEquals("toString must be equal", d1.toString(), d2.toString());
		assertEquals("equals must be equal", d1, d2);
		assertEquals("hashCode must be equal", d1.hashCode(), d2.hashCode());
		
	}
	
	@Test public void testCompatibility(){
		String reg105 = "rmi://org_distributeme_test_laecho_LifecycleAwareEchoService.sjopfazedr@192.168.200.101:9250@20110702120735";
		ServiceDescriptor sd105 = ServiceDescriptor.fromSystemWideUniqueId(reg105);
		String reg104 = "rmi://org_distributeme_test_laecho_LifecycleAwareEchoService.sjopfazedr@192.168.200.101:9250";
		ServiceDescriptor sd104 = ServiceDescriptor.fromSystemWideUniqueId(reg104);
		assertEquals(sd105,sd104);
	}
}
