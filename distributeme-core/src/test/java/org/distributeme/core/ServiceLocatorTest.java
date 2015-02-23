package org.distributeme.core;

import org.distributeme.core.locator.AService;
import org.distributeme.core.locator.BService;
import org.distributeme.core.locator.CService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ServiceLocatorTest {
	
	@Test public void testDynamicFactoryInstantiation(){
		AService service = ServiceLocator.getLocal(AService.class);
		assertEquals(1, service.returnOne());
	}

	@Test public void testDynamicImplInstantiation(){
		BService service = ServiceLocator.getLocal(BService.class);
		assertEquals(1, service.returnOne());
	}
	
	@Test public void testExceptionForNotFoundImpl(){
		try{
			CService service = ServiceLocator.getLocal(CService.class);
			fail("exception expected");
		}catch(IllegalArgumentException e){
			
		}
		
	}
}
