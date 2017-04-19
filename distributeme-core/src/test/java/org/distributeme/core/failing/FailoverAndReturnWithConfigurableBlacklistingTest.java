package org.distributeme.core.failing;

import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.exception.ServiceUnavailableException;
import org.distributeme.core.routing.GenericRouterConfiguration;
import org.distributeme.core.routing.blacklisting.BlacklistingStrategy;
import org.distributeme.core.routing.blacklisting.ErrorsPerIntervalBlacklistingStrategy;
import org.distributeme.core.routing.blacklisting.NoOpBlacklistingStrategy;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hpemoeller on 4/10/17.
 */

public class FailoverAndReturnWithConfigurableBlacklistingTest {

    public static final String FAILOVER_SUFFIX = "_failover";
    private final String serviceId = "ServiceId";
    private final String failoverServiceId = serviceId + FAILOVER_SUFFIX;
    private final GenericRouterConfiguration configuration = new GenericRouterConfiguration();
    private final String configurationName = "blacklisting-routing-test-regular";
    private final BlacklistingStrategy blacklistingStrategyMock = mock(BlacklistingStrategy.class);
    private FailoverAndReturnWithConfigurableBlacklisting router = new FailoverAndReturnWithConfigurableBlacklisting();
    private final ClientSideCallContext clientSideCallContext = new ClientSideCallContext("ServiceId", "context", null);

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

    @Test
    public void getSuffixShouldReturnTheRightSuffix(){
        assertThat(router.getSuffix(), is(FAILOVER_SUFFIX));
    }

    @Test
    public void callFailedShouldNotifyTheStrategy(){
        router.setBlacklistingStrategy(blacklistingStrategyMock);

        router.callFailed(clientSideCallContext);

        verify(blacklistingStrategyMock).notifyCallFailed(clientSideCallContext);
    }

    @Test
    public void callFailedShouldAskIfNextServiceIsBlacklisted(){
        router.setBlacklistingStrategy(blacklistingStrategyMock);

        router.callFailed(clientSideCallContext);

        verify(blacklistingStrategyMock).isBlacklisted(failoverServiceId);
    }

    @Test
    public void ifServiceIsBlacklistedRetryOneFailDecisionWithFailOverServiceIdShouldBeReturned() throws Exception {
        when(blacklistingStrategyMock.isBlacklisted(clientSideCallContext.getServiceId())).thenReturn(true);
        router.setBlacklistingStrategy(blacklistingStrategyMock);

        FailDecision result = router.callFailed(clientSideCallContext);

        assertThat(result.getReaction(), is(FailDecision.Reaction.RETRYONCE));
        assertThat(result.getTargetService(), is("ServiceId" + FAILOVER_SUFFIX));
    }

    @Test
    public void ifServiceIsNotBlacklistedRetryWithServiceIdShouldBeReturned(){
        router.setBlacklistingStrategy(blacklistingStrategyMock);
        when(blacklistingStrategyMock.isBlacklisted(clientSideCallContext.getServiceId())).thenReturn(false);

        FailDecision result = router.callFailed(clientSideCallContext);

        assertThat(result.getReaction(), is(FailDecision.Reaction.RETRYONCE));
        assertThat(result.getTargetService(), is(failoverServiceId));
    }

    @Test(expected = ServiceUnavailableException.class)
    public void ifBothServicesAreBlackListedAnExceptionShouldBeThrown(){
        router.setBlacklistingStrategy(blacklistingStrategyMock);
        when(blacklistingStrategyMock.isBlacklisted(clientSideCallContext.getServiceId())).thenReturn(true);
        when(blacklistingStrategyMock.isBlacklisted(clientSideCallContext.getServiceId() + FAILOVER_SUFFIX)).thenReturn(true);

        router.callFailed(clientSideCallContext);
    }

    @Test
    public void ifCallFailedGetsFailsWithFailOverInstanceItShouldFallBackToTheNormalInstance(){
        clientSideCallContext.setServiceId(failoverServiceId);
        router.setBlacklistingStrategy(blacklistingStrategyMock);
        when(blacklistingStrategyMock.isBlacklisted(failoverServiceId)).thenReturn(true);
        when(blacklistingStrategyMock.isBlacklisted(serviceId)).thenReturn(false);

        FailDecision result = router.callFailed(clientSideCallContext);

        assertThat(result.getReaction(), is(FailDecision.Reaction.RETRYONCE));
        assertThat(result.getTargetService(), is("ServiceId" ));
    }

    @Test(expected = ServiceUnavailableException.class)
    public void ifCallFailedGetsFailsWithFailOverInstanceAndTheNormalInstanceIsBlacklistedAnExceptionShouldBeThrown(){
        clientSideCallContext.setServiceId(failoverServiceId);
        router.setBlacklistingStrategy(blacklistingStrategyMock);
        when(blacklistingStrategyMock.isBlacklisted(failoverServiceId)).thenReturn(true);
        when(blacklistingStrategyMock.isBlacklisted(serviceId)).thenReturn(true);

        router.callFailed(clientSideCallContext);
    }


    @Test
    public void ifBothServicesAreBlackListedAnd_isOverrideBlacklistIfAllBlacklisted_isTrueTheOtherShouldBeCalled(){
        router.getConfiguration().setOverrideBlacklistIfAllBlacklisted(true);
        router.setBlacklistingStrategy(blacklistingStrategyMock);
        when(blacklistingStrategyMock.isBlacklisted(clientSideCallContext.getServiceId())).thenReturn(true);
        when(blacklistingStrategyMock.isBlacklisted(clientSideCallContext.getServiceId() + FAILOVER_SUFFIX)).thenReturn(true);

        FailDecision result = router.callFailed(clientSideCallContext);

        assertThat(result.getReaction(), is(FailDecision.Reaction.RETRYONCE));
        assertThat(result.getTargetService(), is(failoverServiceId ));
    }

    @Test
    public void getServiceIdForCallShouldReturnTheNormalInstance(){
        String result = router.getServiceIdForCall(clientSideCallContext);

        assertThat(result, is(serviceId));
    }

    @Test
    public void getServiceIdForCallShouldReturnTheFailoverInstanceIfServiceIsBlackListed(){
        when(blacklistingStrategyMock.isBlacklisted(serviceId)).thenReturn(true);
        router.setBlacklistingStrategy(blacklistingStrategyMock);

        String result = router.getServiceIdForCall(clientSideCallContext);

        assertThat(result, is(failoverServiceId));
    }
}