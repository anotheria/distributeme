package org.distributeme.consulintegration;

import com.pszymczyk.consul.ConsulProcess;
import com.pszymczyk.consul.ConsulStarterBuilder;
import com.pszymczyk.consul.junit.ConsulResource;
import org.distributeme.core.Location;
import org.distributeme.core.RegistryLocation;
import org.distributeme.core.ServiceDescriptor;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;


/**
 * Created by rboehling on 2/28/17.
 */
public class ConsulRegistryConnectorIntegrationTest {

	public static final String SERVICE_ID = "org_distributeme_test_blacklisting_BlacklistingTestService_0";
	public static final String INSTANCE_ID = "anInstanceId";
	private ConsulRegistryConnector connector = new ConsulRegistryConnector();
	private ConsulProcess consul;

	@Before
	public void setup() {
		consul = ConsulStarterBuilder.consulStarter().withHttpPort(33507).build().start();
	}

	@After
	public void cleanup() throws Exception {
		consul.close();
	}

	@Test
	public void bindsToConsulRegistry() {
		ServiceDescriptor serviceDescriptor = new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, SERVICE_ID, INSTANCE_ID,"aHost", 9559, 1L);

		boolean bind = connector.bind(serviceDescriptor);
		assertTrue("Service should have been bind", bind);

		ServiceDescriptor serviceDescriptorToResolve = new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, SERVICE_ID);
		ServiceDescriptor resolvedDescriptor = connector.resolve(serviceDescriptorToResolve, RegistryLocation.create());

		assertThat(resolvedDescriptor, is(notNullValue()));
		assertThat(resolvedDescriptor.getInstanceId(), is(serviceDescriptor.getInstanceId()));
	}

	@Test
	public void notRegisteredServiceIsNotResolved() {
		ServiceDescriptor serviceDescriptor = new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, SERVICE_ID, INSTANCE_ID,"aHost", 9559, 1L);

		boolean bind = connector.bind(serviceDescriptor);
		assertTrue("Service should have been bind", bind);

		ServiceDescriptor serviceDescriptorToResolve = new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, "otherServiceID");
		ServiceDescriptor resolvedDescriptor = connector.resolve(serviceDescriptorToResolve, RegistryLocation.create());

		assertThat(resolvedDescriptor, is(nullValue()));
	}


}