package org.distributeme.consulintegration;

import com.sun.jersey.api.client.ClientResponse;
import org.distributeme.core.ServiceDescriptor;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Created by rboehling on 3/30/17.
 */
public class ServiceDescriptorFactoryTest {

	private ClientResponse response = mock(ClientResponse.class);


	@Test
	public void returnsNullIfResponseFromConsulIsEmpty() {
		String emptyString = "";
		when(response.getEntity(String.class)).thenReturn(emptyString);

		ServiceDescriptor serviceDescriptor = ServiceDescriptorFactory.createFrom(response);

		assertThat(serviceDescriptor, is(nullValue()));
	}

	@Test
	public void returnsNullIfResponseFromConsulIsEmptyArray() {
		String emptyResponseArray = "[]";
		when(response.getEntity(String.class)).thenReturn(emptyResponseArray);

		ServiceDescriptor serviceDescriptor = ServiceDescriptorFactory.createFrom(response);

		assertThat(serviceDescriptor, is(nullValue()));
	}

	@Test
	public void returnsResponseWithValuesFromConsul() {
		String responseAsString = "[{\"Node\":\"localhost\",\"Address\":\"127.0.0.1\",\"TaggedAddresses\":{\"lan\":\"127.0.0.1\",\"wan\":\"127.0.0.1\"},\"ServiceID\":\"org-distributeme-test-blacklisting-BlacklistingTestService-0\",\"ServiceName\":\"org-distributeme-test-blacklisting-BlacklistingTestService-0\",\"ServiceTags\":[\"instanceId=anInstanceId\",\"my_custom_tag_b\",\"my_custom_tag_a\",\"protocol=rmi\",\"timestamp=1\"],\"ServiceAddress\":\"aHost\",\"ServicePort\":9559,\"ServiceEnableTagOverride\":false,\"CreateIndex\":6,\"ModifyIndex\":6}]";
		when(response.getEntity(String.class)).thenReturn(responseAsString);

		ServiceDescriptor serviceDescriptor = ServiceDescriptorFactory.createFrom(response);

		assertThat(serviceDescriptor, is(notNullValue()));
		assertThat(serviceDescriptor.getServiceId(), is("org_distributeme_test_blacklisting_BlacklistingTestService_0"));
		assertThat(serviceDescriptor.getTimestamp(), is(1L));
		assertThat(serviceDescriptor.getHost(), is("aHost"));
		assertThat(serviceDescriptor.getInstanceId(), is("anInstanceId"));
		assertThat(serviceDescriptor.getPort(), is(9559));
		assertThat(serviceDescriptor.getGlobalServiceId(), is("rmi://org_distributeme_test_blacklisting_BlacklistingTestService_0"));
	}

	@Test
	public void useLatestServiceInstanceIfMoreThenOneInstancesWithSameServiceIdAreRegisteredAtConsul() {
		Long oldestTimestamp = 1L;
		Long inBetweenTimestamp = 2L;
		Long latestTimestamp = 3L;
		String responseAsString = "["
				+ "{\"Node\":\"localhost\",\"Address\":\"127.0.0.1\",\"TaggedAddresses\":{\"lan\":\"127.0.0.1\",\"wan\":\"127.0.0.1\"},\"ServiceID\":\"org-distributeme-test-blacklisting-BlacklistingTestService-0\",\"ServiceName\":\"org-distributeme-test-blacklisting-BlacklistingTestService-0\",\"ServiceTags\":[\"instanceId=anInstanceId\",\"my_custom_tag_b\",\"my_custom_tag_a\",\"protocol=rmi\",\"timestamp="+ oldestTimestamp +"\"],\"ServiceAddress\":\"oldestHost\",\"ServicePort\":9559,\"ServiceEnableTagOverride\":false,\"CreateIndex\":6,\"ModifyIndex\":6},"
				+ "{\"Node\":\"localhost\",\"Address\":\"127.0.0.1\",\"TaggedAddresses\":{\"lan\":\"127.0.0.1\",\"wan\":\"127.0.0.1\"},\"ServiceID\":\"org-distributeme-test-blacklisting-BlacklistingTestService-0\",\"ServiceName\":\"org-distributeme-test-blacklisting-BlacklistingTestService-0\",\"ServiceTags\":[\"instanceId=anInstanceId\",\"my_custom_tag_b\",\"my_custom_tag_a\",\"protocol=rmi\",\"timestamp=" + latestTimestamp + "\"],\"ServiceAddress\":\"latestHost\",\"ServicePort\":9559,\"ServiceEnableTagOverride\":false,\"CreateIndex\":6,\"ModifyIndex\":6},"
				+ "{\"Node\":\"localhost\",\"Address\":\"127.0.0.1\",\"TaggedAddresses\":{\"lan\":\"127.0.0.1\",\"wan\":\"127.0.0.1\"},\"ServiceID\":\"org-distributeme-test-blacklisting-BlacklistingTestService-0\",\"ServiceName\":\"org-distributeme-test-blacklisting-BlacklistingTestService-0\",\"ServiceTags\":[\"instanceId=anInstanceId\",\"my_custom_tag_b\",\"my_custom_tag_a\",\"protocol=rmi\",\"timestamp="+ inBetweenTimestamp + "\"],\"ServiceAddress\":\"inBetweenHost\",\"ServicePort\":9559,\"ServiceEnableTagOverride\":false,\"CreateIndex\":6,\"ModifyIndex\":6}"
				+ "]";
		when(response.getEntity(String.class)).thenReturn(responseAsString);

		ServiceDescriptor serviceDescriptor = ServiceDescriptorFactory.createFrom(response);

		assertThat(serviceDescriptor, is(notNullValue()));
		assertThat(serviceDescriptor.getServiceId(), is("org_distributeme_test_blacklisting_BlacklistingTestService_0"));
		assertThat("Should return the service instance with latest timestamp", serviceDescriptor.getTimestamp(), is(latestTimestamp));
		assertThat("Should return the host with the lateste service timesamp ", serviceDescriptor.getHost(), is("latestHost"));
		assertThat(serviceDescriptor.getInstanceId(), is("anInstanceId"));
		assertThat(serviceDescriptor.getPort(), is(9559));
		assertThat(serviceDescriptor.getGlobalServiceId(), is("rmi://org_distributeme_test_blacklisting_BlacklistingTestService_0"));

	}

}