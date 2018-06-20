package org.distributeme.registry.metaregistry;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.ConfigureMe;
import org.distributeme.core.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * If this registry instance only contains a subset of entries, ask another instance ("parent"). 
 * @author Oliver Hoogvliet
 */
@ConfigureMe(allfields=true, name="registryconfig")
public final class MetaRegistryConfig implements Location {
	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(MetaRegistryConfig.class);
	
	/**
	 * Port of the parent registry.
	 */
	private int registryParentPort;
	/**
	 * Host of the parent registry.
	 */
	private String registryParentHost;

	/**
	 * Context (distributeme).
	 */
	private String registryParentContext = "distributeme";
	/**
	 * Protocol (defaults to http).
	 */
	private String registryParentProtocol = "http";

	/**
	 * If true the registry will lookup a service in a parent registry.
	 */
	private boolean registryParentLookup = false;

	private int registryPortMin;

	private int registryPortMax;

	private boolean monitorRequiredServiceAmount = false;

	private int requiredServiceAmount = 0;



	/**
	 * Returns true if parent registry lookup is enabled.
	 * @return
	 */
	public boolean isRegistryParentLookup() {
		return registryParentLookup;
	}

	public void setRegistryParentLookup(boolean registryParentLookup) {
		this.registryParentLookup = registryParentLookup;
	}

	/**
	 * Protect constructor for singleton.
	 */
	private MetaRegistryConfig() {}
		
	/**
	 * Creates a new config.
	 * @return
	 */
	public static MetaRegistryConfig create(){
		MetaRegistryConfig parent = new MetaRegistryConfig();
		try {
			ConfigurationManager.INSTANCE.configure(parent);
		} catch (IllegalArgumentException e) {
			log.warn("Ignore this, if your instance is a registry client: "+e.getMessage());
		}
		return parent;
	}
	
	public void setRegistryParentPort(int registryParentPort) {
		this.registryParentPort = registryParentPort;
	}
	
	public void setRegistryParentHost(String registryParentHost) {
		this.registryParentHost = registryParentHost;
	}
	
	public String getRegistryParentHost() {
		return registryParentHost;
	}
	
	public int getRegistryParentPort() {
		return registryParentPort;
	}

	public int getRegistryPortMin() {
		return registryPortMin;
	}

	public void setRegistryPortMin(int registryPortMin) {
		this.registryPortMin = registryPortMin;
	}

	public int getRegistryPortMax() {
		return registryPortMax;
	}

	public void setRegistryPortMax(int registryPortMax) {
		this.registryPortMax = registryPortMax;
	}

	@Override public String toString(){
		return registryParentHost+":"+registryParentPort;
	}

	@Override public String getHost() {
		return registryParentHost;
	}

	@Override public int getPort() {
		return registryParentPort;
	}

	@Override
	public String getProtocol() {
		return registryParentProtocol;
	}

	@Override
	public String getContext() {
		return registryParentContext;
	}

	public boolean isMonitorRequiredServiceAmount() {
		return monitorRequiredServiceAmount;
	}

	public void setMonitorRequiredServiceAmount(boolean monitorRequiredServiceAmount) {
		this.monitorRequiredServiceAmount = monitorRequiredServiceAmount;
	}

	public int getRequiredServiceAmount() {
		return requiredServiceAmount;
	}

	public void setRequiredServiceAmount(int requiredServiceAmount) {
		this.requiredServiceAmount = requiredServiceAmount;
	}
}
