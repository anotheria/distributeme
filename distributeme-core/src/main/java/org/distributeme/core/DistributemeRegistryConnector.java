package org.distributeme.core;

import java.nio.charset.Charset;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.distributeme.core.util.BaseRegistryUtil;

import static org.distributeme.core.RegistryUtil.createRegistryOperationUrl;
import static org.distributeme.core.util.BaseRegistryUtil.getUrlContent;


/**
 * Created by rboehling on 2/28/17.
 */
public class DistributemeRegistryConnector implements RegistryConnector {

	/**
	 * Value for the parameter name for the id-param.
	 */
	public static final String PARAM_ID = "id";
	/**
	 * Name of the registry's web-application.
	 */
	public static final String APP = "registry";

	/**
	 * Returns a string representing current state of the registry.
	 *
	 * @return a {@link String} object.
	 */
	@Override
	public String describeRegistry(){
		return BaseRegistryUtil.getRegistryLocation().toString();
	}

	/**
	 * Binds a service.
	 *
	 * @param service a {@link org.distributeme.core.ServiceDescriptor} object.
	 * @return true if sucessful, false otherwise.
	 */
	@Override
	public boolean bind(ServiceDescriptor service){
		String url = createRegistryOperationUrl("bind", PARAM_ID+"="+ BaseRegistryUtil.encode(service.getRegistrationString()));
		return getSuccessOrError(url);
	}

	/**
	 * <p>notifyBind.</p>
	 *
	 * @param location a {@link org.distributeme.core.Location} object.
	 * @param descriptor a {@link org.distributeme.core.ServiceDescriptor} object.
	 * @return a boolean.
	 */
	@Override
	public boolean notifyBind(Location location, ServiceDescriptor descriptor){
		String url = createRegistryOperationUrl(location, "nbind", DistributemeRegistryConnector.PARAM_ID+"="+ BaseRegistryUtil.encode(descriptor.getRegistrationString()));
		return DistributemeRegistryConnector.getSuccessOrError(url);
	}

	/**
	 * <p>notifyUnbind.</p>
	 *
	 * @param location a {@link org.distributeme.core.Location} object.
	 * @param descriptor a {@link org.distributeme.core.ServiceDescriptor} object.
	 * @return a boolean.
	 */
	@Override
	public boolean notifyUnbind(Location location, ServiceDescriptor descriptor){
		String url = createRegistryOperationUrl(location, "nunbind", DistributemeRegistryConnector.PARAM_ID+"="+ BaseRegistryUtil.encode(descriptor.getRegistrationString()));
		return DistributemeRegistryConnector.getSuccessOrError(url);
	}


	/**
	 * Unbinds a service.
	 *
	 * @param service a {@link org.distributeme.core.ServiceDescriptor} object.
	 * @return a boolean.
	 */
	@Override
	public boolean unbind(ServiceDescriptor service){
		String url = createRegistryOperationUrl("unbind", DistributemeRegistryConnector.PARAM_ID+"="+ BaseRegistryUtil.encode(service.getRegistrationString()));
		return DistributemeRegistryConnector.getSuccessOrError(url);
	}

	/**
	 * Resolves a service descriptor at a specified location.
	 *
	 * @param toResolve a {@link org.distributeme.core.ServiceDescriptor} object.
	 * @param loc a {@link org.distributeme.core.Location} object.
	 * @return a {@link org.distributeme.core.ServiceDescriptor} object.
	 */
	@Override
	public ServiceDescriptor resolve(ServiceDescriptor toResolve, Location loc){
		String url = createRegistryOperationUrl(loc, "resolve", DistributemeRegistryConnector.PARAM_ID+"="+ BaseRegistryUtil.encode(toResolve.getLookupString()));
		byte data[] = BaseRegistryUtil.getUrlContent(url);
		if (data == null )
			return null;
		String reply = new String(data, Charset.defaultCharset());
		return "ERROR".equals(reply) ? null : ServiceDescriptor.fromRegistrationString(reply);
	}

	/**
	 * Helper method to determine whether the reply was an error.
	 * @param url
	 * @return
	 */
	@SuppressFBWarnings("DM_DEFAULT_ENCODING")
	static boolean getSuccessOrError(String url){
		byte[] data = getUrlContent(url);
		return data != null && new String(data).equals("SUCCESS");
	}
}
