package org.distributeme.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.anotheria.util.StringUtils;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.Set;
import org.configureme.annotations.SetAll;
import org.distributeme.core.ServiceDescriptor.Protocol;
import org.distributeme.core.util.BaseRegistryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for communication with the registry over http protocol.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class RegistryUtil extends BaseRegistryUtil{

	private static RegistryConnector registryConnector = new DistributemeRegistryConnector();

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
		try {
			registryConnector = (RegistryConnector)Class.forName(configuration.getRegistryConnectorClazz()).newInstance();
		} catch (Exception e) {
			log.error("Could not initiate registry connector " + configuration.getRegistryConnectorClazz(), e);
		}
	}
	
	/**
	 * Binds a service.
	 *
	 * @param service a {@link org.distributeme.core.ServiceDescriptor} object.
	 * @return true if sucessful, false otherwise.
	 */
	public static boolean bind(ServiceDescriptor service){
		return registryConnector.bind(service);
	}
	
	/**
	 * Pings a location. This is part of the cluster management.
	 *
	 * @param location location to ping.
	 * @return a {@link java.lang.String} object.
	 */
	public static String ping(Location location){
		String url = createRegistryOperationUrl(location, "ping", "");
		byte data[] = getUrlContent(url, true);
		if (data == null )
			return null;
		String reply = new String(data, Charset.defaultCharset());
		return "ERROR".equals(reply) ? null : reply;		
	}
	
	/**
	 * <p>notifyBind.</p>
	 *
	 * @param location a {@link org.distributeme.core.Location} object.
	 * @param descriptor a {@link org.distributeme.core.ServiceDescriptor} object.
	 * @return a boolean.
	 */
	public static boolean notifyBind(Location location, ServiceDescriptor descriptor){
		return registryConnector.notifyBind(location, descriptor);
	}
	
	/**
	 * <p>notifyUnbind.</p>
	 *
	 * @param location a {@link org.distributeme.core.Location} object.
	 * @param descriptor a {@link org.distributeme.core.ServiceDescriptor} object.
	 * @return a boolean.
	 */
	public static boolean notifyUnbind(Location location, ServiceDescriptor descriptor){
		return registryConnector.notifyUnbind(location, descriptor);
	}
	
	/**
	 * Unbinds a service.
	 *
	 * @param service a {@link org.distributeme.core.ServiceDescriptor} object.
	 * @return a boolean.
	 */
	public static boolean unbind(ServiceDescriptor service){
		return registryConnector.unbind(service);
	}

	/**
	 * Resolves a service descriptor at a specified location.
	 *
	 * @param toResolve a {@link org.distributeme.core.ServiceDescriptor} object.
	 * @param loc a {@link org.distributeme.core.Location} object.
	 * @return a {@link org.distributeme.core.ServiceDescriptor} object.
	 */
	public static ServiceDescriptor resolve(ServiceDescriptor toResolve, Location loc){
		return registryConnector.resolve(toResolve, loc);
	}
	
	/**
	 * Helper methods that creates the url for given operation and parameters.
	 *
	 * @param operation the operation which should be executed. For example BIND,UNBIND etc.
	 * @param parameters the parameters to the operation.
	 * @return a {@link java.lang.String} object.
	 */
	public static String createRegistryOperationUrl(String operation, String parameters){
		return getRegistryBaseUrl()+operation+"?"+parameters;
	}

	/**
	 * Helper methods that creates the url for given operation and parameters.
	 *
	 * @param loc location of the registry.
	 * @param operation the operation which should be executed. For example BIND,UNBIND etc.
	 * @param parameters the parameters to the operation.
	 * @return a {@link java.lang.String} object.
	 */
	public static String createRegistryOperationUrl(Location loc, String operation, String parameters){
		return getRegistryBaseUrl(loc)+operation+"?"+parameters;
	}
	
	/**
	 * Returns the list of services as xml-string.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String getXMLServiceList(){
		return getXMLServiceList(registryLocation);
	}

	/**
	 * Returns the list of services from a specified location.
	 *
	 * @param loc a {@link org.distributeme.core.Location} object.
	 * @return a {@link java.lang.String} object.
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
	 *
	 * @param toResolve parameter descriptor which contains the serviceid to be resolved.
	 * @return resolved service descriptor which points to a concrete service instance or null if no service has been found.
	 */
	public static ServiceDescriptor resolve(ServiceDescriptor toResolve){
		return resolve(toResolve, registryLocation);
	}
 
	/**
	 * Get registry url for internal use.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getRegistryBaseUrl(){
		return getRegistryBaseUrl(DistributemeRegistryConnector.APP);
	}
	
	/**
	 * Get registry url for internal use.
	 *
	 * @param location registry location.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getRegistryBaseUrl(Location location){
		return getRegistryBaseUrl(DistributemeRegistryConnector.APP, location.getHost(), location.getPort(), location.getProtocol(), location.getContext());
	}
	
	/**
	 * <p>createLocalServiceDescription.</p>
	 *
	 * @param protocol a {@link org.distributeme.core.ServiceDescriptor.Protocol} object.
	 * @param serviceId a {@link java.lang.String} object.
	 * @param instanceId a {@link java.lang.String} object.
	 * @param port a int.
	 * @return a {@link org.distributeme.core.ServiceDescriptor} object.
	 */
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
	 * Returns a string representing current state of the registry.
	 *
	 * @return a {@link java.lang.String} object.
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

		@Configure
		private String registryConnectorClazz = DistributemeRegistryConnector.class.getName();
		
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

		public String getRegistryConnectorClazz() {
			return registryConnectorClazz;
		}

		public void setRegistryConnectorClazz(String registryConnectorClazz) {
			this.registryConnectorClazz = registryConnectorClazz;
		}
	}
	
}
