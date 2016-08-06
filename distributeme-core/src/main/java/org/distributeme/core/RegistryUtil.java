package org.distributeme.core;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.anotheria.util.StringUtils;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.Set;
import org.configureme.annotations.SetAll;
import org.distributeme.core.ServiceDescriptor.Protocol;
import org.distributeme.core.util.BaseRegistryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Utilities for communication with the registry over http protocol.
 * @author lrosenberg
 *
 */
public class RegistryUtil extends BaseRegistryUtil{
	

	/**
	 * Value for the parameter name for the id-param.
	 */
	public static final String PARAM_ID = "id";
	/**
	 * Name of the registry's web-application.
	 */
	public static final String APP = "registry";

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(RegistryUtil.class);
	/**
	 * Registry configuration.
	 */
	private static final Configurable configuration;
	static{
		configuration = new Configurable();
		try{
			ConfigurationManager.INSTANCE.configure(configuration);
		}catch(Exception ignored){
			//ignored
		}
	}
	
	/**
	 * Binds a service. 
	 * @param service
	 * @return true if sucessful, false otherwise.
	 */
	public static boolean bind(ServiceDescriptor service){
		String url = createRegistryOperationUrl("bind", PARAM_ID+"="+encode(service.getRegistrationString()));
		return getSuccessOrError(url);
	}
	
	/**
	 * Pings a location. This is part of the cluster management. 
	 * @param location location to ping.
	 * @return
	 */
	public static String ping(Location location){
		String url = createRegistryOperationUrl(location, "ping", "");
		byte data[] = getUrlContent(url, true);
		if (data == null )
			return null;
		String reply = new String(data, Charset.defaultCharset());
		return "ERROR".equals(reply) ? null : reply;		
	}
	
	public static boolean notifyBind(Location location, ServiceDescriptor descriptor){
		String url = createRegistryOperationUrl(location, "nbind", PARAM_ID+"="+encode(descriptor.getRegistrationString()));
		return getSuccessOrError(url);
	}
	
	public static boolean notifyUnbind(Location location, ServiceDescriptor descriptor){
		String url = createRegistryOperationUrl(location, "nunbind", PARAM_ID+"="+encode(descriptor.getRegistrationString()));
		return getSuccessOrError(url);
	}
	
	/**
	 * Unbinds a service.
	 * @param service
	 * @return
	 */
	public static boolean unbind(ServiceDescriptor service){
		String url = createRegistryOperationUrl("unbind", PARAM_ID+"="+encode(service.getRegistrationString()));
		return getSuccessOrError(url);
	}

	/**
	 * Resolves a service descriptor at a specified location.
	 * @param toResolve
	 * @param loc
	 * @return
	 */
	public static ServiceDescriptor resolve(ServiceDescriptor toResolve, Location loc){
		String url = createRegistryOperationUrl(loc, "resolve", PARAM_ID+"="+encode(toResolve.getLookupString()));
		byte data[] = getUrlContent(url);
		if (data == null )
			return null;
		String reply = new String(data, Charset.defaultCharset());
		return "ERROR".equals(reply) ? null : ServiceDescriptor.fromRegistrationString(reply);		
	}
	
	/**
	 * Helper methods that creates the url for given operation and parameters.
	 * @param operation the operation which should be executed. For example BIND,UNBIND etc.
	 * @param parameters the parameters to the operation.
	 * @return
	 */
	public static String createRegistryOperationUrl(String operation, String parameters){
		return getRegistryBaseUrl()+operation+"?"+parameters;
	}

	/**
	 * Helper methods that creates the url for given operation and parameters.
	 * @param loc location of the registry.
	 * @param operation the operation which should be executed. For example BIND,UNBIND etc.
	 * @param parameters the parameters to the operation.
	 * @return
	 */
	public static String createRegistryOperationUrl(Location loc, String operation, String parameters){
		return getRegistryBaseUrl(loc)+operation+"?"+parameters;
	}
	
	/**
	 * Returns the list of services as xml-string.
	 * @return
	 */
	public static String getXMLServiceList(){
		return getXMLServiceList(registryLocation);
	}

	/**
	 * Returns the list of services from a specified location.
	 * @return
	 */
	@SuppressFBWarnings("DM_DEFAULT_ENCODING")
	public static String getXMLServiceList(Location loc){
		String url = getRegistryBaseUrl(loc)+"list";
		byte data[] = getUrlContent(url);
		if (data == null )
			return null;
		String reply = new String(data);
		return reply;
	}

	/**
	 * Resolves a service request.
	 * @param toResolve parameter descriptor which contains the serviceid to be resolved.
	 * @return resolved service descriptor which points to a concrete service instance or null if no service has been found.
	 */
	public static ServiceDescriptor resolve(ServiceDescriptor toResolve){
		return resolve(toResolve, registryLocation);
	}
 
	/**
	 * Get registry url for internal use.
	 * @return
	 */
	protected static String getRegistryBaseUrl(){
		return getRegistryBaseUrl(APP);
	}
	
	/**
	 * Get registry url for internal use.
	 * @param location registry location.
	 * @return
	 */
	protected static String getRegistryBaseUrl(Location location){
		return getRegistryBaseUrl(APP, location.getHost(), location.getPort(), location.getProtocol(), location.getContext());
	}
	
	public static ServiceDescriptor createLocalServiceDescription(Protocol protocol, String serviceId, String instanceId, int port){
		return new ServiceDescriptor(protocol, serviceId, instanceId, getHostName(), port);
	}
	
	private static String getHostName(){
		try{
			InetAddress localhost = InetAddress.getLocalHost();
			String host = localhost.getHostAddress();
			HashMap<String, String> mappings = configuration.getMappings(); 
			String mappedHost = mappings.get(host);
			return mappedHost == null ? host : mappedHost;
		}catch(UnknownHostException e){
			return "unknown";
		}
	}
	
	/**
	 * Helper method to determine whether the reply was an error.
	 * @param url
	 * @return
	 */
	@SuppressFBWarnings("DM_DEFAULT_ENCODING")
	private static boolean getSuccessOrError(String url){
		byte[] data = getUrlContent(url);
		return data != null && new String(data).equals("SUCCESS");
	}
	

	/**
	 * Returns a string representing current state of the registry.
	 * @return
	 */
	public static final String describeRegistry(){
		return registryLocation.toString();
	}
	
	
	/**
	 * Inner configuration holder class.
	 * @author lrosenberg
	 */
	@ConfigureMe(name="distributeme",watch=true)
	public static class Configurable{
		/**
		 * IP Mappings.
		 */
		private volatile HashMap<String, String> mappings = new HashMap<String, String>();
		
		@Set("registrationIpMapping")
		public void setRegistrationIpMapping(String registrationIpMapping) {
			log.info("registrationIpMappingSet: "+registrationIpMapping);
			try{
				HashMap<String, String> newMappings = new HashMap<String, String>();
				String[] pairs = StringUtils.tokenize(registrationIpMapping, ',');
				for (String p : pairs){
					String ips[] = StringUtils.tokenize(p, ':');
					newMappings.put(ips[0], ips[1]);
				}
				mappings = newMappings;
			}catch(Exception e){
				log.warn("setRegistrationIpMapping("+registrationIpMapping+"), e");
			}
		}
		
		@SetAll
		public void debug(String key, String value){
			log.debug("Config "+key+"="+value);
		}
		
		public HashMap<String, String> getMappings(){
			return mappings;
		}
		
		@Override public String toString(){
			return "Configurable mappings: "+mappings;
		}
	}
	
}
