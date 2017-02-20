package org.distributeme.test.direct;

import org.distributeme.core.ServiceDescriptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 25.01.16 00:35
 */
public class RemoteDirectClient {

	public static void main(String a[]) throws Exception{

		Class<DirectEchoService> targetClazz = DirectEchoService.class;
		Class constantsClass = Class.forName(targetClazz.getPackage().getName()+".generated."+targetClazz.getSimpleName()+"Constants");
		Method m = constantsClass.getMethod("getServiceId");
		String serviceId = (String)m.invoke(null);
		System.out.println("Desired service id is "+serviceId);

		Class<? extends DirectEchoService> remoteStubClass =
			(Class<? extends DirectEchoService> )Class.forName(targetClazz.getPackage().getName()+".generated.Remote"+targetClazz.getSimpleName()+"Stub");
		ServiceDescriptor sd = new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, serviceId, "any", "localhost", 9401);
		Constructor<? extends DirectEchoService> c = remoteStubClass.getConstructor(ServiceDescriptor.class);
		DirectEchoService service = c.newInstance(sd);

		System.out.println("Created new Service: "+service);

		int value = 42;
		System.out.println("Sending echo "+value);
		int reply = service.echo(42);
		System.out.println("Got reply "+reply);




	}


}
