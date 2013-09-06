package org.distributeme.registry.metaregistry;

import java.util.ArrayList;
import java.util.List;

import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.ServiceDescriptor.Protocol;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListenerTest {
	
	@Before public void reset(){
		resetRegistry();
	}
	
	@AfterClass public static void resetRegistry(){
		((MetaRegistryImpl)MetaRegistryImpl.getInstance()).reset();
	}
	
	@Test public void testBind(){
		ServiceDescriptor sd = new ServiceDescriptor(Protocol.RMI, "foo");
		MetaRegistry registry = MetaRegistryImpl.getInstance();
		TestListener listener = new TestListener();
		
		for (int i=0; i<10; i++){
			registry.bind(sd);
		}
		
		assertEquals(0, listener.binds.size());
		assertEquals(0, listener.unbinds.size());
		
		registry.addListener(listener);

		for (int i=0; i<10; i++){
			registry.bind(sd);
		}
		
		assertEquals(10, listener.binds.size());
		assertEquals(0, listener.unbinds.size());
	}
	
	@Test public void testUnbind(){
		ServiceDescriptor sd = new ServiceDescriptor(Protocol.RMI, "foo");
		MetaRegistry registry = MetaRegistryImpl.getInstance();
		TestListener listener = new TestListener();
		
		for (int i=0; i<10; i++){
			registry.unbind(sd);
		}
		
		assertEquals(0, listener.binds.size());
		assertEquals(0, listener.unbinds.size());
		
		registry.addListener(listener);

		for (int i=0; i<10; i++){
			registry.unbind(sd);
		}
		
		assertEquals(0, listener.binds.size());
		assertEquals(0, listener.unbinds.size());

		//now give something to unbind
		
		for (int i=0; i<10; i++){
			registry.bind(sd);
			registry.unbind(sd);
		}
		
		assertEquals(10, listener.binds.size());
		assertEquals(10, listener.unbinds.size());
}

	public static class TestListener implements MetaRegistryListener{

		List<ServiceDescriptor> binds = new ArrayList<ServiceDescriptor>();
		List<ServiceDescriptor> unbinds = new ArrayList<ServiceDescriptor>();
		
		@Override
		public void onBind(ServiceDescriptor service) {
			binds.add(service);
		}

		@Override
		public void onUnbind(ServiceDescriptor service) {
			unbinds.add(service);
		}
		
	}
}
