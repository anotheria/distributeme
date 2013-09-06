package org.distributeme.core.interceptor.availabilitytesting;

import java.util.ArrayList;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.exception.ServiceUnavailableException;
import org.distributeme.core.interceptor.ClientSideRequestInterceptor;
import org.distributeme.core.interceptor.InterceptionContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServiceUnavailableByConfigurationInterceptorTest {
	@Test public void testPositive(){
		ClientSideRequestInterceptor interceptor = new ServiceUnavailableByConfigurationInterceptor();
		try{
			interceptor.beforeServiceCall(constructCallContext("org_distributeme_test_junit_Service1"), new InterceptionContext());
			fail("Expected exception!");
		}catch(ServiceUnavailableException e){
			//expected
		}
		try{
			interceptor.beforeServiceCall(constructCallContext("org_distributeme_test_junit_Service2"), new InterceptionContext());
			fail("Expected exception!");
		}catch(ServiceUnavailableException e){
			//expected
		}
	}
	
	@Test public void testNegative(){
		ClientSideRequestInterceptor interceptor = new ServiceUnavailableByConfigurationInterceptor();
		try{
			interceptor.beforeServiceCall(constructCallContext("foo"), new InterceptionContext());
		}catch(ServiceUnavailableException e){
			fail("Expected no exception!");
		}
	}
	
	private ClientSideCallContext constructCallContext(String serviceId){
		return new ClientSideCallContext(serviceId, "foo", new ArrayList(0));
	}
}
