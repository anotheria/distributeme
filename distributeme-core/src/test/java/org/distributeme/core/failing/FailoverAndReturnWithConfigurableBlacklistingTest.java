package org.distributeme.core.failing;

import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import org.distributeme.core.routing.GenericRouterConfiguration;
import org.distributeme.core.routing.blacklisting.ErrorsPerIntervalBlacklistingStrategy;
import org.distributeme.core.routing.blacklisting.NoOpBlacklistingStrategy;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by hpemoeller on 4/10/17.
 */

public class FailoverAndReturnWithConfigurableBlacklistingTest {

    private final String serviceId = "FailOverService";
    private final GenericRouterConfiguration configuration = new GenericRouterConfiguration();
    private final String configurationName = "blacklisting-routing-test-regular";
    private FailoverAndReturnWithConfigurableBlacklisting router = new FailoverAndReturnWithConfigurableBlacklisting();

    @Before
    public void setUp() throws Exception {
        ProducerRegistryFactory.getProducerRegistryInstance().cleanup();
        router.setConfiguration(configuration);
    }

    @Test
    public void setServiceIdShouldRegisterAMoskitoProducer() throws Exception {
        router.setConfigurationName (serviceId, configurationName);

        Collection<IStatsProducer> producers = ProducerRegistryFactory.getProducerRegistryInstance().getProducers();
        assertThat(producers.size(), is(1));
        assertThat(producers.iterator().next().getProducerId(), is("_" +serviceId + "-Router"));
    }

    @Test
    public void setConfigurationNameShouldConfigureTheGenericRouterConfig() throws Exception {
        router.setConfigurationName(serviceId, configurationName);

        assertThat(configuration.getBlacklistTime(), is(20000l));
    }

    @Test(expected = IllegalStateException.class)
    public void setConfigurationNameShouldThrowExceptionIfConfigurationFileDoesNotExist() throws Exception {
        router.setConfigurationName(serviceId, "NoExistingFileName");
    }

    @Test
    public void getFailbackTimeoutShouldReturnConfiguredValue() {
        router.setConfigurationName(serviceId, configurationName);
        assertThat(router.getFailbackTimeout(), is(20000l));
    }

    @Test
    public void routerConfigurationChangeShouldReplaceTheExsistionConfiguration() throws Exception {
        GenericRouterConfiguration configuration = new GenericRouterConfiguration();
        configuration.setBlacklistTime(159l);
        router.routerConfigurationChange(configuration);

        assertThat(router.getFailbackTimeout(), is(159l));
    }

    @Test
    public void theBlackListingStrategyShouldChangeOnAfterInitialConfiguration() throws Exception {
        // given
        router.setConfigurationName (serviceId, configurationName);
        assertThat(router.getBlacklistingStrategy(), instanceOf(NoOpBlacklistingStrategy.class));

        // when
        router.getConfiguration().setBlacklistStrategyClazz("org.distributeme.core.routing.blacklisting.ErrorsPerIntervalBlacklistingStrategy");
        router.getConfiguration().afterConfiguration();

        // then
        assertThat(router.getBlacklistingStrategy(), instanceOf(ErrorsPerIntervalBlacklistingStrategy.class));
    }

    @Test
    public void theBlackListingStrategyShouldChangeOnObserverCall() throws Exception {
        // given
        router.setConfigurationName (serviceId, configurationName);
        assertThat(router.getBlacklistingStrategy(), instanceOf(NoOpBlacklistingStrategy.class));

        // when
        router.getConfiguration().setBlacklistStrategyClazz("org.distributeme.core.routing.blacklisting.ErrorsPerIntervalBlacklistingStrategy");
        router.getConfiguration().afterReConfiguration();

        // then
        assertThat(router.getBlacklistingStrategy(), instanceOf(ErrorsPerIntervalBlacklistingStrategy.class));
    }
}