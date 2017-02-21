package org.distributeme.core.routing;

import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.exception.DistributemeRuntimeException;
import org.distributeme.core.failing.FailingStrategy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

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
 * @author h3llka
 * @version $Id: $Id
 */
public abstract class AbstractRouterWithFailOverToNextNode extends AbstractRouterWithFailover implements ConfigurableRouter, FailingStrategy {

	/**
	 * Attribute for call context where we store instances that we already tried.
	 */
	public static final String ATTR_TRIED_INSTANCES = AbstractRouterWithFailOverToNextNode.class.getName()+".instance";


	/**
	 * Random for selection of next instance.
	 */
	private Random random = new Random(System.nanoTime());



	/** {@inheritDoc} */
	@Override
	public String getServiceIdForCall(final ClientSideCallContext clientSideCallContext) {
		if (getLog().isDebugEnabled())
			getLog().debug("Incoming call " + clientSideCallContext);

		if (getServiceAmount() == 0)
			return clientSideCallContext.getServiceId();

		if (failingSupported() && !clientSideCallContext.isFirstCall())
			return getServiceIdForFailing(clientSideCallContext);

		switch (getStrategy()) {
			case MOD_ROUTER:
				return getModBasedServiceId(clientSideCallContext);
			case RR_ROUTER:
				return getRRBasedServiceId(clientSideCallContext);
			default:
				throw new AssertionError(" Routing Strategy " + getStrategy() + " not supported in current implementation.");
		}
	}

	/**
	 * Return serviceId for failing call.
	 *
	 * @param context {@link ClientSideCallContext}
	 * @return serviceId string
	 */
	private String getServiceIdForFailing(final ClientSideCallContext context) {
		if (getLog().isDebugEnabled())
			getLog().debug("Calculating serviceIdForFailing call. ClientSideCallContext[" + context + "]");

		String originalServiceId = context.getServiceId();

		HashSet<String> instancesThatIAlreadyTried = (HashSet<String>)context.getTransportableCallContext().get(ATTR_TRIED_INSTANCES);
		if (instancesThatIAlreadyTried==null){
			instancesThatIAlreadyTried = new HashSet<String>();
			context.getTransportableCallContext().put(ATTR_TRIED_INSTANCES, instancesThatIAlreadyTried);
		}

		int lastUnderscore = originalServiceId.lastIndexOf(UNDER_LINE);
		String idSubstring = originalServiceId.substring(lastUnderscore + 1);

		//check if idSubstring is numeric, if its not we have nowhere to fail.
		try{
			Integer.parseInt(idSubstring);
		}catch(NumberFormatException e){
			return originalServiceId;
		}


		instancesThatIAlreadyTried.add(idSubstring);

		//now pick next candidate.
		if (instancesThatIAlreadyTried.size()==getConfiguration().getNumberOfInstances()){
			//we tried everything, it won't work
			throw new DistributemeRuntimeException("No instance available, we tried all already.");
		}

		String result = null;

		if (instancesThatIAlreadyTried.size() == getConfiguration().getNumberOfInstances() -1){
			//only one instance left, it's easier to find it.
			for (int candidate = 0; candidate<getConfiguration().getNumberOfInstances(); candidate++){
				if (!instancesThatIAlreadyTried.contains(""+candidate)){
					//found untried yet
					result = originalServiceId.substring(0, lastUnderscore + 1) + candidate;
				}
			}
		}

		if (result == null){
			//if we are here we have more than one possible candidate.
			int[] candidates = new int[getConfiguration().getNumberOfInstances()-instancesThatIAlreadyTried.size()];
			int i=0;
			for (int candidate = 0; candidate<getConfiguration().getNumberOfInstances(); candidate++) {
				if (!instancesThatIAlreadyTried.contains("" + candidate)) {
					try {
						candidates[i++] = candidate;
					}catch(ArrayIndexOutOfBoundsException e){
						getLog().error("ERROR 20150306 Got ArrayIndexOutOfBoundsException in calculation of failover node index (increased by one) " + (i) + ", candidate: " + candidate + ", candidates: " + Arrays.toString(candidates) +
										"NumberOfInstances: " + getConfiguration().getNumberOfInstances() + ", AlreadyTried: " + instancesThatIAlreadyTried + ", Size: " + instancesThatIAlreadyTried.size() + ", ServiceId: " + originalServiceId
						);
					}
				}
			}
			//now pick a candidate
			int candidate = candidates[random.nextInt(candidates.length)];
			result = originalServiceId.substring(0, lastUnderscore + 1) + candidate;
		}

		if (getLog().isDebugEnabled())
			getLog().debug("serviceIdForFailing result[" + result + "]. ClientSideCallContext[" + context + "]");

		return result;
	}


	/** {@inheritDoc} */
	@Override
	public void customize(String s) {
		int serviceAmount = 0;
		try {
			serviceAmount = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			getLog().error("Can't set customization parameter " + s + ", send all traffic to default instance");
		}
		if (serviceAmount < 0)
			throw new AssertionError("Customization Error! " + s + " Should be positive value, or at least 0");

		getConfiguration().setNumberOfInstances(serviceAmount);
	}

}
