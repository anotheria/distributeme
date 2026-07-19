package org.distributeme.consulintegration;

import java.util.Arrays;

import com.pszymczyk.consul.ConsulProcess;
import com.pszymczyk.consul.ConsulStarterBuilder;
import org.distributeme.core.RegistryLocation;
import org.distributeme.core.ServiceDescriptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Created by rboehling on 2/28/17.
 */
@Disabled
public class ConsulRegistryConnectorIntegrationTest {

	private static final String SERVICE_ID = "org_distributeme_test_blacklisting_BlacklistingTestService_0";
	private static final String INSTANCE_ID = "anInstanceId";
	private ConsulRegistryConnector connector = new ConsulRegistryConnector();
	private ConsulProcess consul;

	@BeforeEach
	public void setup() {
		consul = ConsulStarterBuilder.consulStarter().withHttpPort(33507).build().start();
	}

	@AfterEach
	public void cleanup() {
		consul.close();
	}

	@Test
	public void bindsToConsulRegistry() {
		ServiceDescriptor serviceDescriptor = new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, SERVICE_ID, INSTANCE_ID,"aHost", 9559, 1L);

		connector.setCustomTagProviderClassList(Arrays.asList("org.distributeme.consulintegration.DistributeMeCustomTagTestClassA" ,
				"org.distributeme.consulintegration.DistributeMeCustomTagTestClassB"));
		boolean bind = connector.bind(serviceDescriptor);

		assertTrue(bind, "Service should have been bind");

		ServiceDescriptor serviceDescriptorToResolve = new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, SERVICE_ID);
		ServiceDescriptor resolvedDescriptor = connector.resolve(serviceDescriptorToResolve, RegistryLocation.create());

		assertThat(resolvedDescriptor, is(notNullValue()));
		assertThat(resolvedDescriptor.getInstanceId(), is(serviceDescriptor.getInstanceId()));
	}

	@Test
	public void notRegisteredServiceIsNotResolved() {
		ServiceDescriptor serviceDescriptor = new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, SERVICE_ID, INSTANCE_ID,"aHost", 9559, 1L);

		boolean bind = connector.bind(serviceDescriptor);
		assertTrue(bind, "Service should have been bind");

		ServiceDescriptor serviceDescriptorToResolve = new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, "otherServiceID");
		ServiceDescriptor resolvedDescriptor = connector.resolve(serviceDescriptorToResolve, RegistryLocation.create());

		assertThat(resolvedDescriptor, is(nullValue()));
	}

	@Test
	public void unbindsService() {
		ServiceDescriptor serviceDescriptor = new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, SERVICE_ID, INSTANCE_ID,"aHost", 9559, 1L);

		connector.setCustomTagProviderClassList(Arrays.asList("org.distributeme.consulintegration.DistributeMeCustomTagTestClassA" ,
				"org.distributeme.consulintegration.DistributeMeCustomTagTestClassB"));
		boolean bind = connector.bind(serviceDescriptor);

		assertTrue(bind, "Service should have been bind");

		ServiceDescriptor serviceDescriptorToResolve = new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, SERVICE_ID);
		ServiceDescriptor resolvedDescriptor = connector.resolve(serviceDescriptorToResolve, RegistryLocation.create());

		assertThat(resolvedDescriptor, is(notNullValue()));
		assertThat(resolvedDescriptor.getInstanceId(), is(serviceDescriptor.getInstanceId()));

		boolean unbind = connector.unbind(serviceDescriptor);

		assertTrue(unbind, "Service should have been unbind");

		serviceDescriptorToResolve = new ServiceDescriptor(ServiceDescriptor.Protocol.RMI, SERVICE_ID);
		resolvedDescriptor = connector.resolve(serviceDescriptorToResolve, RegistryLocation.create());

		assertThat(resolvedDescriptor, is(nullValue()));
	}


}