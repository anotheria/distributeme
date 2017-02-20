package org.distributeme.core;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.ConfigureMe;
import org.distributeme.core.conventions.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configurable location object. This class is configured from distributeme.json.
 *
 * @author lrosenberg
 * @version $Id: $Id
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
	
	/**
	 * <p>Getter for the field <code>registryContainerPort</code>.</p>
	 *
	 * @return a int.
	 */
	public int getRegistryContainerPort() {
		return registryContainerPort;
	}
	/**
	 * <p>Setter for the field <code>registryContainerPort</code>.</p>
	 *
	 * @param registryContainerPort a int.
	 */
	public void setRegistryContainerPort(int registryContainerPort) {
		this.registryContainerPort = registryContainerPort; 
	}
	/**
	 * <p>Getter for the field <code>registryContainerHost</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getRegistryContainerHost() {
		return registryContainerHost;
	}
	/**
	 * <p>Setter for the field <code>registryContainerHost</code>.</p>
	 *
	 * @param registryContainerHost a {@link java.lang.String} object.
	 */
	public void setRegistryContainerHost(String registryContainerHost) {
		this.registryContainerHost = registryContainerHost;
	}
	
	/**
	 * <p>Getter for the field <code>rmiRegistryMinPort</code>.</p>
	 *
	 * @return a int.
	 */
	public int getRmiRegistryMinPort() {
		return rmiRegistryMinPort;
	}
	/**
	 * <p>Setter for the field <code>rmiRegistryMinPort</code>.</p>
	 *
	 * @param rmiRegistryMinPort a int.
	 */
	public void setRmiRegistryMinPort(int rmiRegistryMinPort) {
		this.rmiRegistryMinPort = rmiRegistryMinPort;
	}

	/**
	 * <p>Getter for the field <code>rmiRegistryMaxPort</code>.</p>
	 *
	 * @return a int.
	 */
	public int getRmiRegistryMaxPort() {
		return rmiRegistryMaxPort;
	}
	/**
	 * <p>Setter for the field <code>rmiRegistryMaxPort</code>.</p>
	 *
	 * @param rmiRegistryMaxPort a int.
	 */
	public void setRmiRegistryMaxPort(int rmiRegistryMaxPort) {
		this.rmiRegistryMaxPort = rmiRegistryMaxPort;
	}

	/**
	 * <p>Getter for the field <code>registryContainerProtocol</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getRegistryContainerProtocol() {
		return registryContainerProtocol;
	}

	/**
	 * <p>Setter for the field <code>registryContainerProtocol</code>.</p>
	 *
	 * @param registryContainerProtocol a {@link java.lang.String} object.
	 */
	public void setRegistryContainerProtocol(String registryContainerProtocol) {
		this.registryContainerProtocol = registryContainerProtocol;
	}

	/**
	 * <p>Getter for the field <code>registryContainerContext</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getRegistryContainerContext() {
		return registryContainerContext;
	}

	/**
	 * <p>Setter for the field <code>registryContainerContext</code>.</p>
	 *
	 * @param registryContainerContext a {@link java.lang.String} object.
	 */
	public void setRegistryContainerContext(String registryContainerContext) {
		this.registryContainerContext = registryContainerContext;
	}

	/** {@inheritDoc} */
	@Override public String toString(){
		return "DistributeMeRegistry " + getRegistryContainerProtocol()+"://"+getRegistryContainerHost() + ":" +getRegistryContainerPort()+"/"+getRegistryContainerContext()+", local range: ["+rmiRegistryMinPort+" .. "+rmiRegistryMaxPort+"]";
	}

	/**
	 * Creates a new configured registry location.
	 *
	 * @return a {@link org.distributeme.core.RegistryLocation} object.
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
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getHost(){
		return registryContainerHost;
	}
	
	/**
	 * Returns the port.
	 *
	 * @return a int.
	 */
	public int getPort() {
		return registryContainerPort;
	}

	/**
	 * <p>getProtocol.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getProtocol(){
		return registryContainerProtocol;
	}

	/**
	 * <p>getContext.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getContext(){
		return registryContainerContext;
	}


}
