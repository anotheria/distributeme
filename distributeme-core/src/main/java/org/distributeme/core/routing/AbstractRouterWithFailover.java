package org.distributeme.core.routing;

import net.anotheria.util.StringUtils;
import org.configureme.ConfigurationManager;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.failing.FailDecision;
import org.distributeme.core.failing.FailingStrategy;
import org.distributeme.core.stats.RoutingStatsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.03.15 00:37
 */
abstract class AbstractRouterWithFailover extends AbstractRouter implements ConfigurableRouter, FailingStrategy {

	/**
	 * Under line constant.
	 */
	public static final String UNDER_LINE = "_";

	/**
	 * AbstractRouterImplementation logger.
	 */
	private final Logger log;

	/**
	 * Router configuration.
	 */
	private GenericRouterConfiguration configuration = new GenericRouterConfiguration();

	/**
	 * AbstractRouterImplementation delegation counter.
	 */
	private AtomicInteger delegateCallCounter;

	/**
	 * AbstractRouterImplementation 'modRouteMethodRegistry'. Contains information about
	 * methods which should be routed using MOD strategy.
	 * Additionally maps method name to modable parameter position.
	 */
	private final Map<String, Integer> modRouteMethodRegistry;


	public AbstractRouterWithFailover() {
		log = LoggerFactory.getLogger(Constants.ROUTING_LOGGER_NAME);
		delegateCallCounter = new AtomicInteger(0);
		if (getStrategy() == null)
			throw new AssertionError("getStrategy() method should not return NULL. Please check " + this.getClass() + " implementation");
		modRouteMethodRegistry = new HashMap<String, Integer>();
	}

	@Override
	public FailDecision callFailed(final ClientSideCallContext clientSideCallContext) {

		RoutingStatsCollector stats = getRoutingStats(clientSideCallContext.getServiceId());
		stats.addFailedCall();

		if (!failingSupported()) {
			stats.addFailDecision();
			return FailDecision.fail();
		}

		if (clientSideCallContext.getCallCount() < getServiceAmount() - 1) {
			stats.addRetryDecision();
			return FailDecision.retry();
		}

		stats.addFailDecision();
		return FailDecision.fail();
	}


	/**
	 * Returns serviceId based on RoundRobin strategy.
	 *
	 * @param context {@link org.distributeme.core.ClientSideCallContext}
	 * @return serviceId string
	 */
	protected String getRRBasedServiceId(ClientSideCallContext context) {
		if (configuration.getNumberOfInstances() == 0)
			return context.getServiceId();
		int fromCounter = delegateCallCounter.incrementAndGet();
		if (fromCounter >= configuration.getNumberOfInstances()){
			int oldCounter = fromCounter;
			fromCounter = 0;
			delegateCallCounter.compareAndSet(oldCounter, 0);
		}
		return context.getServiceId()+UNDER_LINE+fromCounter;
	}

	/**
	 * Return serviceId based on Mod routing strategy.
	 * NOTE : it's native that not all methods supports such kind of routing strategy [MOD].
	 * So, if some method does not supports MOD - strategy - then ROUND-ROBIN strategy  will be used for such call.
	 *
	 * @param context {@link org.distributeme.core.ClientSideCallContext}
	 * @return serviceId string
	 */
	protected String getModBasedServiceId(ClientSideCallContext context) {
		if (modRouteMethodRegistry.containsKey(context.getMethodName())) {
			List<?> parameters = context.getParameters();
			if (parameters == null)
				throw new AssertionError("Method parameters can't be NULL for MOD-Based routing strategy");

			int parameterPosition = modRouteMethodRegistry.get(context.getMethodName());
			if (parameters.size() < parameterPosition + 1)
				throw new AssertionError("Not properly configured router, parameter count is less than expected - actual: " + parameters.size() + ", expected: " + parameterPosition);
			Object parameter = parameters.get(parameterPosition);
			long parameterValue = getModableValue(parameter);
			if (parameterValue<0)
				parameterValue *= -1;

			String result = context.getServiceId() + UNDER_LINE + (parameterValue % getServiceAmount());

			if (getLog().isDebugEnabled())
				getLog().debug("Returning mod based result : " + result + " for " + context + " where : serversAmount[" + getServiceAmount() + "], modableValue[" + parameterValue + "]");
			return result;
		}
		if (getLog().isDebugEnabled())
			getLog().debug("Call to method " + context.getMethodName() + " can't be routed using MOD strategy. Building RR strategy based serviceId.");

		return getRRBasedServiceId(context);
	}




	/**
	 * Simply return configured {@link org.slf4j.Logger} instance.
	 *
	 * @return {@link org.slf4j.Logger}
	 */
	protected Logger getLog() {
		return log;
	}

	protected GenericRouterConfiguration getConfiguration(){
		return configuration;
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

	/**
	 * Return amount of services for which routing should be performed.
	 * Current method should not return less then (int) 2 result, cause in that case
	 * router usage makes no sense,  value validation will be performed in constructor.
	 *
	 * @return int value
	 */
	protected int getServiceAmount() {
		return getConfiguration().getNumberOfInstances();
	}

	/**
	 * Allow to turn on and off failing support.
	 * Actually can be enabled or disabled per some implementation.
	 *
	 * @return boolean value
	 */
	protected abstract boolean failingSupported();

	/**
	 * Return RouterStrategy - which should be used for current Router implementation.
	 * Current method should not return NULL, value validation will be performed in constructor.
	 *
	 * @return {@link RouterStrategy}
	 */
	protected abstract RouterStrategy getStrategy();

	/**
	 * Return long value for mod calculation.
	 *
	 * @param parameter some method incoming parameter
	 * @return long value
	 */
	protected abstract long getModableValue(Object parameter);



	/**
	 * Allow to add some custom method with some name and modable parameter position to mod method registry.
	 * Illegal argument exception will be thrown if any incoming parameter is not valid ( mName - is null or empty, modableParameterPosition is negative).
	 *
	 * @param mName					name of method which should be routed using MOD strategy
	 * @param modableParameterPosition position of method argument for mod calculations
	 */
	protected void addModRoutedMethod(String mName, int modableParameterPosition) {
		if (StringUtils.isEmpty(mName))
			throw new IllegalArgumentException("mName parameter can't be null || empty");
		if (modableParameterPosition < 0)
			throw new IllegalArgumentException("modableParameterPosition should be greater or equal to 0, no negative values supported");

		modRouteMethodRegistry.put(mName, modableParameterPosition);
	}

	/**
	 * Allow to add some custom method with some name using default 0 modable parameter position.
	 * Illegal argument exception will be thrown if  mName - is null or empty.
	 *
	 * @param mName name of method which should be routed using MOD strategy
	 */
	protected void addModRoutedMethod(String mName) {
		if (StringUtils.isEmpty(mName))
			throw new IllegalArgumentException("mName parameter can't be null || empty");
		modRouteMethodRegistry.put(mName, 0);
	}



}
