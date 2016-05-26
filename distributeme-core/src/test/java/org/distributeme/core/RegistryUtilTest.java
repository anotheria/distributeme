package org.distributeme.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class RegistryUtilTest {

	@Test public void testRegistryParentUtil() {
		String host = "test.host.uk";
		int port = 815;
		String baseUrl = RegistryUtil.getRegistryBaseUrl(host, port);
		assertEquals(baseUrl, "http://"+host+":"+port+"/distributeme/registry/");
	}
	
}
