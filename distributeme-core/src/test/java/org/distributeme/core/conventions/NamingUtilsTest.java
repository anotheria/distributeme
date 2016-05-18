package org.distributeme.core.conventions;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NamingUtilsTest {
	@Test public void testStubFactoryName(){
		String stubName = SharedNamingUtils.getStubFactoryFullClassName(MyTestService.class);
		assertTrue(stubName.indexOf("RemoteMyTestServiceFactory")>0);
	}
	@Test public void testAsynchFactoryName(){
		String stubName = SharedNamingUtils.getAsynchFactoryFullClassName(MyTestService.class);
		assertTrue(stubName.indexOf("AsynchMyTestServiceFactory")>0);
	}
}
