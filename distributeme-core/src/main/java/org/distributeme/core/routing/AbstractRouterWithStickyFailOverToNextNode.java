package org.distributeme.core.routing;

import net.anotheria.util.StringUtils;
import org.configureme.ConfigurationManager;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.exception.DistributemeRuntimeException;
import org.distributeme.core.failing.FailDecision;
import org.distributeme.core.failing.FailingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract implementation of {@link org.distributeme.core.routing.Router} which supports {@link org.distributeme.core.failing.FailingStrategy}.
 * <p/>
 * By methods overriding/implementing router can be configured to support :
 * - Mod or RoundRobin strategy (override properly getStrategy() method  - <p>NOTE : should not return null</p>);
 * - different amounts of service instances, can be configured via annotation ar simply changed by getServerAmount() method override;
 * - support or not support call failing (failingSupported() should return true fro support, false otherwise ).
 * <p/>
 * In case when Mod routing strategy selected for some router, register all MOD - routed methods directly in router-constructor  using next calls:
 * - addModRoutedMethod (name, position) - which will add mod support for method with selected [name], and incoming argument with selected [position] will be used
 * as modable value;
 * - addModRoutedMethod(name) - will add mod support for method with selected [name], and parameter with 0 position will be used as modable (common case).
 * <p/>
 * IMPORTANT : If MOD routing can't be performed for some call (method does not have any incoming params, or incoming params does not matches for modable calculations, or simply we does not need to
 * route some method by MOD ) - RoundRobin will be performed  instead!. For this - just don't call addModRoutedMethod for method which should not be routed by MOD.
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * By implementing getModableValue(<?>) method - You can simply extract some long from incoming parameter, for further calculations.
 *
 * @author h3llka,dvayanu
 */
public abstract class AbstractRouterWithStickyFailOverToNextNode implements ConfigurableRouter, FailingStrategy {

	/**
	 * Services parameter.
	 */
	public static final String PARAMETER_KEY_SERVICES = "services";
	/**
	 * Timeout parameter.
	 */
	public static final String PARAMETER_KEY_TIMEOUT  = "timeout";

	/**
	 * Attribute for call context where we store instances that we already tried.
	 */
	public static final String ATTR_TRIED_INSTANCES = AbstractRouterWithStickyFailOverToNextNode.class.getName()+".instance";

	/**
	 * Router configuration.
	 */
	private GenericRouterConfiguration configuration = new GenericRouterConfiguration();

	/**
	 * Random for selection of next instance.
	 */
	private Random random = new Random(System.nanoTime());

	/**
	 * AbstractRouterImplementation 'modRouteMethodRegistry'. Contains information about
	 * methods which should be routed using MOD strategy.
	 * Additionally maps method name to modable parameter position.
	 */
	private final Map<String, Integer> modRouteMethodRegistry;
	/**
	 * Under line constant.
	 */
	private static final String UNDER_LINE = "_";

	/**
	 * AbstractRouterImplementation logger.
	 */
	private final Logger log;

	/**
	 * AbstractRouterImplementation delegation counter.
	 */
	private AtomicInteger delegateCallCounter;

	/**
	 * Map with timestamps for last server failures.
	 */
	private ConcurrentMap<String, Long> serverFailureTimestamps = new ConcurrentHashMap<String, Long>();

	/**
	 * Constructor.
	 * Throws {@link AssertionError} in case when getStrategy() implementation is wrong ( result NULL ).
	 */
	protected AbstractRouterWithStickyFailOverToNextNode() {
		log = LoggerFactory.getLogger(this.getClass());
		delegateCallCounter = new AtomicInteger(0);
		if (getStrategy() == null)
			throw new AssertionError("getStrategy() method should not return NULL. Please check " + this.getClass() + " implementation");
		modRouteMethodRegistry = new HashMap<String, Integer>();
	}


	@Override
	public FailDecision callFailed(final ClientSideCallContext clientSideCallContext) {
		serverFailureTimestamps.put(clientSideCallContext.getServiceId(), System.currentTimeMillis());
		if (!failingSupported())
			return FailDecision.fail();

		if (clientSideCallContext.getCallCount() < getServiceAmount() - 1)
			return FailDecision.retry();

		return FailDecision.fail();
	}


	@Override
	public String getServiceIdForCall(final ClientSideCallContext clientSideCallContext) {

		if (log.isDebugEnabled())
			log.debug("Incoming call " + clientSideCallContext);

		if (getServiceAmount() == 0)
			return clientSideCallContext.getServiceId();

		if (failingSupported() && !clientSideCallContext.isFirstCall())
			return getServiceIdForFailing(clientSideCallContext);


		String selectedServiceId = null;

		switch (getStrategy()) {
			case MOD_ROUTER:
				selectedServiceId = getModBasedServiceId(clientSideCallContext);
				break;
			case RR_ROUTER:
				selectedServiceId = getRRBasedServiceId(clientSideCallContext);
				break;
			default:
				throw new AssertionError(" Routing Strategy " + getStrategy() + " not supported in current implementation.");
		}

		Long lastFailed = serverFailureTimestamps.get(selectedServiceId);
		boolean blacklisted = lastFailed != null && (System.currentTimeMillis() - lastFailed) < configuration.getBlacklistTime();
		/*if (lastFailed!=null)
			System.out.println("blacklisted "+lastFailed+", "+System.currentTimeMillis()+" " + (lastFailed - System.currentTimeMillis()));
			*/

		//the service id we picked up is blacklisted due to previous failing.
		if (blacklisted) {
			clientSideCallContext.setServiceId(selectedServiceId);
			return getServiceIdForFailing(clientSideCallContext);
		}

		return selectedServiceId;
	}

	/**
	 * Return serviceId based on Mod routing strategy.
	 * NOTE : it's native that not all methods supports such kind of routing strategy [MOD].
	 * So, if some method does not supports MOD - strategy - then ROUND-ROBIN strategy  will be used for such call.
	 *
	 * @param context {@link org.distributeme.core.ClientSideCallContext}
	 * @return serviceId string
	 */
	private String getModBasedServiceId(ClientSideCallContext context) {
		if (modRouteMethodRegistry.containsKey(context.getMethodName())) {
			List<?> parameters = context.getParameters();
			if (parameters == null)
				throw new AssertionError("Method parameters can't be NULL for MOD-Based routing strategy");

			int parameterPosition = modRouteMethodRegistry.get(context.getMethodName());
			if (parameters.size() < parameterPosition + 1)
				throw new AssertionError("Not properly configured router, parameter count is less than expected - actual: " + parameters.size() + ", expected: " + parameterPosition);
			Object parameter = parameters.get(parameterPosition);
			long parameterValue = getModableValue(parameter);

			String result = context.getServiceId() + UNDER_LINE + (parameterValue % getServiceAmount());

			if (log.isDebugEnabled())
				log.debug("Returning mod based result : " + result + " for " + context + " where : serversAmount[" + getServiceAmount() + "], modableValue[" + parameterValue + "]");
			return result;
		}
		if (log.isDebugEnabled())
			log.debug("Call to method " + context.getMethodName() + " can't be routed using MOD strategy. Building RR strategy based serviceId.");

		return getRRBasedServiceId(context);
	}


	/**
	 * Returns serviceId based on RoundRobin strategy.
	 *
	 * @param context {@link org.distributeme.core.ClientSideCallContext}
	 * @return serviceId string
	 */
	private String getRRBasedServiceId(ClientSideCallContext context) {
		final int callCounter = delegateCallCounter.get();
		if (delegateCallCounter.incrementAndGet() >= getServiceAmount())
			delegateCallCounter.set(0);

		String result = context.getServiceId() + UNDER_LINE + callCounter;

		if (log.isDebugEnabled())
			log.debug("Returning roundRobin based result : " + result + " for " + context + " where : serversAmount[" + getServiceAmount() + "]");

		return result;
	}

	/**
	 * Return serviceId for failing call.
	 *
	 * @param context {@link org.distributeme.core.ClientSideCallContext}
	 * @return serviceId string
	 */
	private String getServiceIdForFailing(final ClientSideCallContext context) {
		if (log.isDebugEnabled())
			log.debug("Calculating serviceIdForFailing call. ClientSideCallContext[" + context + "]");

		String originalServiceId = context.getServiceId();
		//System.out.println("Calculating failing for orig service id "+originalServiceId);
		HashSet<String> instancesThatIAlreadyTried = (HashSet<String>)context.getTransportableCallContext().get(ATTR_TRIED_INSTANCES);
		if (instancesThatIAlreadyTried==null){
			instancesThatIAlreadyTried = new HashSet<String>();
			context.getTransportableCallContext().put(ATTR_TRIED_INSTANCES, instancesThatIAlreadyTried);
		}

		int lastUnderscore = originalServiceId.lastIndexOf(UNDER_LINE);
		String idSubstring = originalServiceId.substring(lastUnderscore + 1);
		instancesThatIAlreadyTried.add(idSubstring);

		//System.out.println("instancesThat I already Tried: " + instancesThatIAlreadyTried+", "+configuration.getNumberOfInstances());

		//now pick next candidate.
		if (instancesThatIAlreadyTried.size()==configuration.getNumberOfInstances()){
			//we tried everything, it won't work
			throw new DistributemeRuntimeException("No instance available, we tried all already.");
		}

		String result = null;

		if (instancesThatIAlreadyTried.size() == configuration.getNumberOfInstances() -1){
			//only one instance left, it's easier to find it.
			for (int candidate = 0; candidate<configuration.getNumberOfInstances(); candidate++){
				if (!instancesThatIAlreadyTried.contains(""+candidate)){
					//found untried yet
					result = originalServiceId.substring(0, lastUnderscore + 1) + candidate;
				}
			}
		}

		if (result==null){
			//if we are here we have more than one possible candidate.
			int[] candidates = new int[configuration.getNumberOfInstances()-instancesThatIAlreadyTried.size()];
			int i=0;
			for (int candidate = 0; candidate<configuration.getNumberOfInstances(); candidate++) {
				if (!instancesThatIAlreadyTried.contains("" + candidate)) {
			 		candidates[i++] = candidate;
				}
			}
			//now pick a candidate
			int candidate = candidates[random.nextInt(candidates.length)];
			result = originalServiceId.substring(0, lastUnderscore + 1) + candidate;
		}

		if (log.isDebugEnabled())
			log.debug("serviceIdForFailing result[" + result + "]. ClientSideCallContext[" + context + "]");

		return result;
	}


	@Override
	public void setConfigurationName(String configurationName) {
		try{
			ConfigurationManager.INSTANCE.configureAs(configuration, configurationName);
		}catch(IllegalArgumentException e){
			throw new IllegalStateException("Can't configure router and this leaves us in undefined state, probably configuration not found: "+configurationName, e);
		}
	}


	@Override
	public void customize(String s) {
		String tokens[] = StringUtils.tokenize(s, ',');
		for (String t : tokens) {
			String key_value[] = StringUtils.tokenize(t, '=');
			String key = key_value[0];
			String value = key_value[1];
			key = key.toLowerCase();
			if (key.equals(PARAMETER_KEY_SERVICES)) {
				try {
					configuration.setNumberOfInstances(Integer.parseInt(value));
				} catch (NumberFormatException e) {
					log.error("Can't set customization parameter " +key+ " to " + value + ", send all traffic to default instance");
				}
			}
			if (key.equals(PARAMETER_KEY_TIMEOUT)) {
				try {
					configuration.setBlacklistTime(Long.parseLong(value));
				} catch (NumberFormatException e) {
					log.error("Can't set customization parameter " +key+ " to " + value + ", send all traffic to default instance");
				}
			}
		}
		if (configuration.getNumberOfInstances() < 0)
			throw new AssertionError("Customization Error! " + s + " Should be positive value, or at least 0");
	}


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


	/**
	 * Simply return configured {@link org.slf4j.Logger} instance.
	 *
	 * @return {@link org.slf4j.Logger}
	 */
	protected Logger getLog() {
		return log;
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
	 * Return amount of services for which routing should be performed.
	 * Current method should not return less then (int) 2 result, cause in that case
	 * router usage makes no sense,  value validation will be performed in constructor.
	 *
	 * @return int value
	 */
	protected int getServiceAmount() {
		return configuration.getNumberOfInstances();
	}

	/**
	 * Return long value for mod calculation.
	 *
	 * @param parameter some method incoming parameter
	 * @return long value
	 */
	protected abstract long getModableValue(Object parameter);


	/**
	 * Returns type  of strategy - on which current router works.
	 */
	protected static enum RouterStrategy {
		/**
		 * Router based on Mod policy/strategy.
		 */
		MOD_ROUTER,
		/**
		 * Router based on RoundRobin policy/strategy.
		 */
		RR_ROUTER
	}
}
