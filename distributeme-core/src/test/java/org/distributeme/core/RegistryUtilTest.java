package org.distributeme.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class RegistryUtilTest {

	@Test public void testRegistryParentUtil() {
		Location location = new Location() {
			@Override
			public String getHost() {
				return "test.host.uk";
			}

			@Override
			public int getPort() {
				return 815;
			}

			@Override
			public String getProtocol() {
				return "http";
			}

			@Override
			public String getContext() {
				return "distributeme";
			}
		};

		String baseUrl = RegistryUtil.getRegistryBaseUrl(location);
		assertEquals(baseUrl, "http://test.host.uk:815/distributeme/registry/");
	}

	@Test public void testRootLocation() {
		Location location = new Location() {
			@Override
			public String getHost() {
				return "bla.distributeme.org";
			}

			@Override
			public int getPort() {
				return 443;
			}

			@Override
			public String getProtocol() {
				return "https";
			}

			@Override
			public String getContext() {
				return null;
			}
		};

		String baseUrl = RegistryUtil.getRegistryBaseUrl(location);
		assertEquals(baseUrl, "https://bla.distributeme.org:443/registry/");
	}
}
