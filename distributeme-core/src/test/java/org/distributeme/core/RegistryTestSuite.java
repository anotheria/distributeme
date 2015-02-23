package org.distributeme.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value=Suite.class)
@SuiteClasses(value={RegistryUtilTest.class, ServiceDescriptorTest.class} )
public class RegistryTestSuite {}
