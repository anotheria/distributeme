package org.distributeme.consulintegration;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


/**
 * Created by rboehling on 3/1/17.
 */
public class ServiceNameTranslatorTest {

	public static final String DISTRIBUTE_ME_SERVICE_ID = "org_distributeme_test_blacklisting_BlacklistingTestService_0";
	public static final String CONSUL_SERVICE_ID = "org-distributeme-test-blacklisting-BlacklistingTestService-0";

	@Test
	public void toConsulReplaceUnderscoresWithDashes() throws Exception {
		assertThat(ServiceNameTranslator.toConsul(DISTRIBUTE_ME_SERVICE_ID), is(CONSUL_SERVICE_ID));

	}

	@Test
	public void fromConsulReplaceDashesWithUnderscores() {
		assertThat(ServiceNameTranslator.fromConsul(CONSUL_SERVICE_ID), is(DISTRIBUTE_ME_SERVICE_ID));
	}
}