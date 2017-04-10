package org.distributeme.core.failing;

import org.configureme.ConfigurationManager;
import org.distributeme.core.routing.ConfigurableRouter;
import org.distributeme.core.routing.GenericRouterConfiguration;

/**
 * Created by hpemoeller on 4/10/17.
 */
public class FailoverAndReturnWithConfigurableBlacklisting extends FailoverAndReturn implements ConfigurableRouter {


    private GenericRouterConfiguration configuration = new GenericRouterConfiguration();

    @Override
    protected long getFailbackTimeout() {
        return configuration.getBlacklistTime();
    }

    @Override
    public void setConfigurationName(String serviceId, String configurationName) {
        setServiceId(serviceId);
        try{
            ConfigurationManager.INSTANCE.configureAs(configuration, configurationName);
        }catch(IllegalArgumentException e){
            throw new IllegalStateException("Can't configure router and this leaves us in undefined state, probably configuration not found: "+configurationName, e);
        }
    }

    public void setConfiguration(GenericRouterConfiguration configuration) {
        this.configuration = configuration;
    }
}