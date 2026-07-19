package org.distributeme.agents;

import net.anotheria.util.IdCodeGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PackAndUnpackTest {
	@Test public void testPlainAgent(){
		PlainAgent agent = new PlainAgent();
		System.out.println("Created agent "+agent);
		String code = IdCodeGenerator.generateCode(20);
		agent.setCode(code);
		System.out.println("Agent "+agent+" will be packed now.");
		
		AgentPackage pack = AgentPackageUtility.pack(agent);
		
		System.out.println("PACK: "+pack);
		System.out.println("==================");
		System.out.println("UNPACKING");
		
		//....
		
		Agent agent2 = AgentPackageUtility.unpack(pack);
		System.out.println("Unpacked agent2: "+agent2);
		assertEquals(agent.getCode(), ((TestAgent)agent2).getCode());
		assertFalse(agent.getId()==((TestAgent)agent2).getId(), "Same class ");
		assertFalse(agent.getSubId()==((TestAgent)agent2).getSubId(), "Same class ");
		
		System.out.println("==================");
	}


	@Test public void testComplexAgent(){
		ComplexAgent agent = new ComplexAgent();
		System.out.println("Created agent "+agent);
		String code = IdCodeGenerator.generateCode(20);
		agent.setCode(code);
		System.out.println("Agent "+agent+" will be packed now.");
		
		AgentPackage pack = AgentPackageUtility.pack(agent);
		
		System.out.println("PACK: "+pack);
		System.out.println("==================");
		System.out.println("UNPACKING");
		
		//....
		
		Agent agent2 = AgentPackageUtility.unpack(pack);
		System.out.println("Unpacked agent2: "+agent2);
		assertEquals(agent.getCode(), ((TestAgent)agent2).getCode());
		assertFalse(agent.getId()==((TestAgent)agent2).getId(), "Same class ");
		assertFalse(agent.getSubId()==((TestAgent)agent2).getSubId(), "Same sub class ");
	}
	
	@Test public void testScanClasses(){
		PlainAgent agent1 = new PlainAgent();
		List<Class<?>> list1 = AgentPackageUtility.scanForCustomClasses(agent1);
		assertNotNull(list1, "The class list shouldn't be null");
		assertTrue(list1.size()>0, "The class list shouldn't be empty");
		assertEquals(agent1.getClass(), list1.get(0));
		//System.out.println(list1);

	
		ComplexAgent agent2 = new ComplexAgent();
		List<Class<?>> list2 = AgentPackageUtility.scanForCustomClasses(agent2);
		assertNotNull(list2, "The class list shouldn't be null");
		assertTrue(list2.size()>0, "The class list shouldn't be empty");
		assertEquals(agent2.getClass(), list2.get(0));
		assertEquals(ComplexAgentComponent.class, list2.get(1));
		//System.out.println(list2);
		
		ClassScanAgent agent3 = new ClassScanAgent();
		List<Class<?>> list3 = AgentPackageUtility.scanForCustomClasses(agent3);
		assertNotNull(list3, "The class list shouldn't be null");
		assertTrue(list3.size()>0, "The class list shouldn't be empty");
		assertEquals(agent3.getClass(), list3.get(0));
		assertEquals(ClassScanAgentComponent.class, list3.get(1));
		assertEquals(PlainAgent.class, list3.get(2));
		assertEquals(ComplexAgent.class, list3.get(3));
		assertEquals(ComplexAgentComponent.class, list3.get(4));
		//System.out.println(list3);
		
	}
}
