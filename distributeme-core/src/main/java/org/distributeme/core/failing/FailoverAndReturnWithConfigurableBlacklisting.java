package org.distributeme.core.failing;

import org.configureme.ConfigurationManager;
import org.distributeme.core.ClientSideCallContext;
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

    public void setConfiguration(GenericRouterConfiguration configuration) {
        this.configuration = configuration;
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

        if(blacklistingStrategy.isBlacklisted(context.getServiceId())) {
            FailDecision ret = FailDecision.retryOnce();
            ret.setTargetService(context.getServiceId()+getSuffix());
            return ret;
        }
        FailDecision retry = FailDecision.retry();
        retry.setTargetService(context.getServiceId());
        return retry;
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

    public GenericRouterConfiguration getConfiguration() {
        return configuration;
    }

    public BlacklistingStrategy getBlacklistingStrategy() {
        return blacklistingStrategy;
    }


}