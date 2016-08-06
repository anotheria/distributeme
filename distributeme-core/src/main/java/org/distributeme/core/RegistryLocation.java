package org.distributeme.core;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.ConfigureMe;
import org.distributeme.core.conventions.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configurable location object. This class is configured from distributeme.json.
 * @author lrosenberg
 *
 */
@ConfigureMe(allfields=true, name="distributeme")
public final class RegistryLocation implements Location {
	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(RegistryLocation.class);
	/**
	 * Configured host.
	 */
	private String registryContainerHost;

	/**
	 * Protocol, defaults to http.
	 */
	private String registryContainerProtocol = "http";

	/**
	 * Context, defaults to distributeme.
	 */
	private String registryContainerContext = "distributeme";

	/**
	 * Configured port.
	 */
	private int registryContainerPort;
	/**
	 * Left side of the port range for the local rmi registry.
	 */
	private int rmiRegistryMinPort = 9250;

	/**
	 * Right side of the port range for the local rmi registry.
	 */
	private int rmiRegistryMaxPort = 9299;
	
	public int getRegistryContainerPort() {
		return registryContainerPort;
	}
	public void setRegistryContainerPort(int registryContainerPort) {
		this.registryContainerPort = registryContainerPort; 
	}
	public String getRegistryContainerHost() {
		return registryContainerHost;
	}
	public void setRegistryContainerHost(String registryContainerHost) {
		this.registryContainerHost = registryContainerHost;
	}
	
	public int getRmiRegistryMinPort() {
		return rmiRegistryMinPort;
	}
	public void setRmiRegistryMinPort(int rmiRegistryMinPort) {
		this.rmiRegistryMinPort = rmiRegistryMinPort;
	}

	public int getRmiRegistryMaxPort() {
		return rmiRegistryMaxPort;
	}
	public void setRmiRegistryMaxPort(int rmiRegistryMaxPort) {
		this.rmiRegistryMaxPort = rmiRegistryMaxPort;
	}

	public String getRegistryContainerProtocol() {
		return registryContainerProtocol;
	}

	public void setRegistryContainerProtocol(String registryContainerProtocol) {
		this.registryContainerProtocol = registryContainerProtocol;
	}

	public String getRegistryContainerContext() {
		return registryContainerContext;
	}

	public void setRegistryContainerContext(String registryContainerContext) {
		this.registryContainerContext = registryContainerContext;
	}

	@Override public String toString(){
		return "DistributeMeRegistry " + getRegistryContainerProtocol()+"://"+getRegistryContainerHost() + ":" +getRegistryContainerPort()+"/"+getRegistryContainerContext()+", local range: ["+rmiRegistryMinPort+" .. "+rmiRegistryMaxPort+"]";
	}

	/**
	 * Creates a new configured registry location.
	 * @return
	 */
	public static RegistryLocation create(){
		RegistryLocation location = new RegistryLocation();
		if (location.configureFromSystemPropertiesAndReturnTrueIfConfigured()){
			return location;
		}
		try {
			ConfigurationManager.INSTANCE.configure(location);
		} catch (IllegalArgumentException e) {
			log.warn("Ignore this, if your instance is a registry server: "+e.getMessage());
		}
		return location;
	}
	
	private boolean configureFromSystemPropertiesAndReturnTrueIfConfigured(){
		registryContainerHost = SystemProperties.CENTRAL_REGISTRY_HOST.get();
		registryContainerPort = SystemProperties.CENTRAL_REGISTRY_PORT.getAsInt();
		registryContainerProtocol = SystemProperties.CENTRAL_REGISTRY_PROTOCOL.get();
		registryContainerContext  = SystemProperties.CENTRAL_REGISTRY_CONTEXT.get();
		rmiRegistryMinPort = SystemProperties.LOCAL_RMI_REGISTRY_MIN_PORT.getAsInt();
		rmiRegistryMaxPort = SystemProperties.LOCAL_RMI_REGISTRY_MAX_PORT.getAsInt();
		return registryContainerHost!=null && registryContainerHost.length()>0;
	}
	
	/**
	 * Private constructor.
	 */
	private RegistryLocation(){}

	/**
	 * Returns the host.
	 */
	public String getHost(){
		return registryContainerHost;
	}
	
	/**
	 * Returns the port.
	 */
	public int getPort() {
		return registryContainerPort;
	}

	public String getProtocol(){
		return registryContainerProtocol;
	}

	public String getContext(){
		return registryContainerContext;
	}


}
