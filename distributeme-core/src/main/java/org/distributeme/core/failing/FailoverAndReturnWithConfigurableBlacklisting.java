package org.distributeme.core.failing;

import org.configureme.ConfigurationManager;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.exception.ServiceUnavailableException;
import org.distributeme.core.routing.ConfigurableRouter;
import org.distributeme.core.routing.Constants;
import org.distributeme.core.routing.GenericRouterConfiguration;
import org.distributeme.core.routing.RouterConfigurationObserver;
import org.distributeme.core.routing.blacklisting.BlacklistingStrategy;
import org.distributeme.core.routing.blacklisting.DefaultBlacklistingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hpemoeller on 4/10/17.
 */
public class FailoverAndReturnWithConfigurableBlacklisting extends FailoverAndReturn implements ConfigurableRouter, RouterConfigurationObserver {

    private static final Logger LOG = LoggerFactory.getLogger(Constants.ROUTING_LOGGER_NAME);

    private GenericRouterConfiguration configuration = new GenericRouterConfiguration();

    private BlacklistingStrategy blacklistingStrategy = new DefaultBlacklistingStrategy();

    @Override
    protected long getFailbackTimeout() {
        return configuration.getBlacklistTime();
    }

    @Override
    public void setConfigurationName(String serviceId, String configurationName) {
        setServiceId(serviceId);
        configuration.addRouterConfigurationObserver(this);
        try{
            ConfigurationManager.INSTANCE.configureAs(configuration, configurationName);
        }catch(IllegalArgumentException e){
            throw new IllegalStateException("Can't configure router and this leaves us in undefined state, probably configuration not found: "+configurationName, e);
        }
    }

    @Override
    public void routerConfigurationInitialChange(GenericRouterConfiguration configuration) {
        //not needed
    }

    @Override
    public void routerConfigurationFollowupChange(GenericRouterConfiguration configuration) {
        updateRouterConfiguration(configuration);
    }

    @Override
    public void routerConfigurationChange(GenericRouterConfiguration configuration) {
        updateRouterConfiguration(configuration);
    }

    @Override
    public FailDecision callFailed(ClientSideCallContext context) {
        blacklistingStrategy.notifyCallFailed(context);
        getRoutingStats(context.getServiceId()).addFailedCall();

        if (nextNodeIsBlacklisted(context) && !isOverrideBlacklistIfAllBlacklisted()){
            getRoutingStats(context.getServiceId()).addFailedCall();
            throw new ServiceUnavailableException(context.getServiceId() + " and " + createServiceIdForNextServiceCall(context) + " are Blacklisted!");
        }
        return retryOneceOnService(createServiceIdForNextServiceCall(context));
    }

    private boolean isOverrideBlacklistIfAllBlacklisted() {
        return getConfiguration().isOverrideBlacklistIfAllBlacklisted();
    }

    private boolean nextNodeIsBlacklisted(ClientSideCallContext context) {
        return blacklistingStrategy.isBlacklisted(createServiceIdForNextServiceCall(context));
    }

    private String createServiceIdForNextServiceCall(ClientSideCallContext context) {
        String targetService = context.getServiceId() + getSuffix();
        if (context.getServiceId().contains(getSuffix())){
            targetService = context.getServiceId().replace(getSuffix(),"");
        }
        getRoutingStats(targetService).addRequestRoutedTo();
        return targetService;
    }

    private FailDecision retryOneceOnService(String targetService) {
        getRoutingStats(targetService).addRequestRoutedTo();
        FailDecision ret = FailDecision.retryOnce();
        ret.setTargetService(targetService);
        return ret;
    }

    @Override
    public String getServiceIdForCall(ClientSideCallContext callContext) {
        if (blacklistingStrategy.isBlacklisted(callContext.getServiceId())){
            getRoutingStats(callContext.getServiceId()).addBlacklisted();
            return createServiceIdForNextServiceCall(callContext);
        }
        getRoutingStats(callContext.getServiceId()).addRequestRoutedTo();
        return callContext.getServiceId();
    }

    @Override
    protected String getSuffix() {
        return "_failover";
    }

    private void updateRouterConfiguration(GenericRouterConfiguration configuration) {
        this.configuration = configuration;
        if(getConfiguration().getBlacklistStrategyClazz() != null) {
            try {
                blacklistingStrategy = (BlacklistingStrategy)Class.forName(getConfiguration().getBlacklistStrategyClazz()).newInstance();
            } catch (Exception e) {
                LOG.error("Could not initialize black listing strategy " + getConfiguration().getBlacklistStrategyClazz() + " using: " + blacklistingStrategy.getClass().getName(), e);
            }
        }
        blacklistingStrategy.setConfiguration(getConfiguration());
    }

    GenericRouterConfiguration getConfiguration() {
        return configuration;
    }

    BlacklistingStrategy getBlacklistingStrategy() {
        return blacklistingStrategy;
    }

    void setBlacklistingStrategy(BlacklistingStrategy blacklistingStrategy) {
        this.blacklistingStrategy = blacklistingStrategy;
    }

    void setConfiguration(GenericRouterConfiguration configuration) {
        this.configuration = configuration;
    }
}