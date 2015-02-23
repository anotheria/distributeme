package org.distributeme.agents.singleton;

import org.distributeme.agents.Agent;
import org.distributeme.agents.AgentPackage;
import org.distributeme.agents.AgentPackageUtility;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingletonAgentTest {
	@Test public void testSingletonNormal() throws Exception{
		System.out.println("NOTATTACK");
		Singleton.reset(); //ensure tests are not influencing each other
		
		SingletonAgent agent = new SingletonAgent();
		agent.awake();
		
		
		Agent agent2 = agent;
		agent2.awake();
		assertTrue(agent.getClass().hashCode() == agent2.getClass().hashCode());
	}
	
	@Ignore @Test public void testSingletonAttack() throws Exception{
		System.out.println("ATTACK");
		Singleton.reset(); //ensure tests are not influencing each other
		
		SingletonAgent agent = new SingletonAgent();
		agent.awake();
		
		//now pack
		AgentPackage pack = AgentPackageUtility.pack(agent);
		
		Agent agent2 = AgentPackageUtility.unpack(pack);
		System.out.println(agent2.getClass());
		SingletonAgent test = (SingletonAgent)agent2;
		agent2.awake();
		assertFalse(agent.getClass().hashCode() == agent2.getClass().hashCode());
	}
	
	@Test public void multipleInstances(){
		SingletonAgent a1 = new SingletonAgent();
		SingletonAgent a2 = new SingletonAgent();
		
		System.out.println(a1.toSingletonString());
		assertEquals(a1.toSingletonString(), a2.toSingletonString());
		
		AgentPackage pack = AgentPackageUtility.pack(a1);
		Agent a3 = AgentPackageUtility.unpack(pack);
		
		System.out.println(((SingletonTestable)a3).toSingletonString());
		assertFalse(((SingletonTestable)a3).toSingletonString().equals(a2.toSingletonString()));
	}
}
