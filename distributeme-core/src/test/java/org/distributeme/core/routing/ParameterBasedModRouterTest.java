package org.distributeme.core.routing;

import net.anotheria.util.IdCodeGenerator;
import org.distributeme.core.ClientSideCallContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ParameterBasedModRouterTest {
	
	@Test public void testInteger(){
		ParameterBasedModRouter router = new ParameterBasedModRouter();
		router.customize("2,0");
		
		Random rnd = new Random(System.currentTimeMillis());
		for (int i = 0; i<50; i++){
			int numberOfParameter = rnd.nextInt(5)+1;
			ArrayList parameters = new ArrayList(numberOfParameter);
			for (int t=0; t<numberOfParameter; t++){
				parameters.add(new Integer(rnd.nextInt(100)));
			}
			int modParameter = (Integer)parameters.get(0);
			String serviceId = IdCodeGenerator.generateCode(10);
			String sidReply = router.getServiceIdForCall(new ClientSideCallContext(serviceId, null, parameters));
			assertEquals(sidReply, serviceId+"_"+(modParameter%2));
		}
	}
	
	@Test public void testLong(){
		ParameterBasedModRouter router = new ParameterBasedModRouter();
		router.customize("2,0");
		
		Random rnd = new Random(System.currentTimeMillis());
		ArrayList parameters = new ArrayList();
		parameters.add(new Long(rnd.nextInt(100)));
		parameters.add(new Long(rnd.nextInt(100)));
		parameters.add(new Long(rnd.nextInt(100)));
 
		ClientSideCallContext context = new ClientSideCallContext("bla");
		context.setServiceId("foo");
		context.setParameters(parameters);
		
		parameters.set(0, 1L);
		assertEquals("foo_1", router.getServiceIdForCall(context));

		parameters.set(0, 2L);
		assertEquals("foo_0", router.getServiceIdForCall(context));
		
		parameters.set(0, 3L);
		assertEquals("foo_1", router.getServiceIdForCall(context));

		parameters.set(0, 4L);
		assertEquals("foo_0", router.getServiceIdForCall(context));

		parameters.set(0, 0L);
		assertEquals("foo_0", router.getServiceIdForCall(context));
	}
	
	@Test public void testBoolean(){
		ParameterBasedModRouter router = new ParameterBasedModRouter();
		router.customize("2,0");
		
		ArrayList parameters = new ArrayList();
 
		String serviceId = "foo";
		parameters.add(Boolean.TRUE);
		assertEquals("foo_0", router.getServiceIdForCall(new ClientSideCallContext(serviceId, "bla", parameters)));

		parameters.set(0, Boolean.FALSE);
		assertEquals("foo_1", router.getServiceIdForCall(new ClientSideCallContext(serviceId, "bla", parameters)));
	}
	
	@Test public void testForInsufficentParameters(){
		ParameterBasedModRouter router = new ParameterBasedModRouter();
		router.customize("2,3");
		
		ArrayList p = new ArrayList();

		try{
			router.getServiceIdForCall(new ClientSideCallContext("foo", "bar", p));
			fail("AssertionError expected");
		}catch(AssertionError e){
			
		}
		
	}
	
	@Test public void testForErrorsInCustomize(){
		ParameterBasedModRouter router = new ParameterBasedModRouter();

		try{
			router.customize(null);
			fail("Exception expected");
		}catch(AssertionError e){}

		try{
			router.customize("");
			fail("Exception expected");
		}catch(AssertionError e){}

		try{
			router.customize("abc");
			fail("Exception expected");
		}catch(AssertionError e){}

		try{
			router.customize("a,b");
			fail("Exception expected");
		}catch(AssertionError e){}

		try{
			router.customize("1,b");
			fail("Exception expected");
		}catch(AssertionError e){}
		
		try{
			router.customize("b,1");
			fail("Exception expected");
		}catch(AssertionError e){}

		//this one should work
		router.customize("1,1");

	}
	
	@Test public void testForErrorsIngetServiceIdForCall(){
		ParameterBasedModRouter router = new ParameterBasedModRouter();
		router.customize("2,3");
		

		try{
			ArrayList p = new ArrayList();
			p.add(null);
			router.getServiceIdForCall(new ClientSideCallContext("foo", "bar", p));
			fail("AssertionError expected");
		}catch(AssertionError e){
			
		}
		
		try{
			ArrayList p = new ArrayList();
			p.add(new String("foo"));
			router.getServiceIdForCall(new ClientSideCallContext("foo", "bar", p));
			fail("AssertionError expected");
		}catch(AssertionError e){
			
		}
	}
	
	@Test public void testForNull(){
		ParameterBasedModRouter router = new ParameterBasedModRouter();
		router.customize("2,0");
		
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(null);
		
		try{
			router.getServiceIdForCall(new ClientSideCallContext("foo", "bar", list));
			fail("Exception expected");
		}catch(AssertionError e){
			
		}

	}
	@Test public void testForUnsupported(){
		ParameterBasedModRouter router = new ParameterBasedModRouter();
		router.customize("2,0");
		
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(new ArrayList<Object>());//arraylist itself is not a supported argument type
		
		try{
			router.getServiceIdForCall(new ClientSideCallContext("foo", "bar", list));
			fail("Exception expected");
		}catch(AssertionError e){
			
		}

	}
}
