package org.distributeme.core.routing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(value=Suite.class)
@SuiteClasses(value={NoOpRouterTest.class, ParameterBasedModRouterTest.class, RoundRobinRouterTest.class} )
public class RouteTestSuite {

}
