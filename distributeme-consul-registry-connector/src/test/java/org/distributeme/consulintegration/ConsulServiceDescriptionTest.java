package org.distributeme.consulintegration;

import java.util.HashMap;
import java.util.Map;

import org.distributeme.core.ServiceDescriptor;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;


/**
 * Created by rboehling on 3/2/17.
 */
public class ConsulServiceDescriptionTest {

	private static final String SERVICE_ID = "serviceId";
	private static final String INSTANCE_ID = "instanceId";
	private static final String HOSTNAME = "hostname";
	private static final int PORT = 9000;
	private static final long TIMESTAMP = 1L;
	private static final ServiceDescriptor.Protocol RMI = ServiceDescriptor.Protocol.RMI;


	private ConsulServiceDescription consulServiceDescription;
	private ServiceDescriptor servicesDescriptor;
	private Map<String, String> tagableSystemProperties = new HashMap<>();

	@Test
	public void createConsulServiceDescription_withCorrectValues() {
		givenServiceDescriptor();

		whenConsulServiceDescriptionIsCreated();

		thenConsulServiceDescriptionContainsAllValuesFromServiceDescription();
	}

	private void thenConsulServiceDescriptionContainsAllValuesFromServiceDescription() {
		assertThat(consulServiceDescription.getId(), is(SERVICE_ID));
		assertThat(consulServiceDescription.getName(), is(SERVICE_ID));
		assertThat(consulServiceDescription.getAddress(), is(HOSTNAME));
		assertThat(consulServiceDescription.getPort(), is(PORT));
		assertThat(consulServiceDescription.getTags().size(), is(3));
		assertThat(consulServiceDescription.getTags(), hasItem("instanceId=" + INSTANCE_ID));
		assertThat(consulServiceDescription.getTags(), hasItem("protocol=" + RMI.toString().toLowerCase()));
		assertThat(consulServiceDescription.getTags(), hasItem("timestamp=" + TIMESTAMP));
	}



	private void whenConsulServiceDescriptionIsCreated() {
		consulServiceDescription = new ConsulServiceDescription(servicesDescriptor, tagableSystemProperties);
	}

	private void givenServiceDescriptor() {
		servicesDescriptor = new ServiceDescriptor(
				RMI,
				SERVICE_ID,
				INSTANCE_ID,
				HOSTNAME,
				PORT,
				TIMESTAMP);
	}


}