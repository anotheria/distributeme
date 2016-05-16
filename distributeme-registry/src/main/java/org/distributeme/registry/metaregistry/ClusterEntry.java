package org.distributeme.registry.metaregistry;

import net.anotheria.util.NumberUtils;
import org.distributeme.core.Location;

/**
 * This class represents an entry in the cluser management. Each registry instance has a corresponding ClusterEntry.
 * @author lrosenberg
 *
 */
public class ClusterEntry implements Location{
	/**
	 * Host where the cluster entry runs.
	 */
	private String host;
	/**
	 * Port where the cluster entry runs.
	 */
	private int port;
	/**
	 * True if the registry represented by the entry is online.
	 */
	private boolean online = false;
	/**
	 * Entries identity.
	 */
	private String identity;
	/**
	 * When have we seen the registry last time.
	 */
	private long lastSeen;
	/**
	 * When did we checked the registry last time.
	 */
	private long lastChecked;
	/**
	 * When have we seen the registry first time.
	 */
	
	private long firstSeen;
	
	public ClusterEntry(String aHost, int aPort){
		host = aHost;
		port = aPort;
	}
	
	@Override public String toString(){
		return host+":"+port;
	}
	
	public boolean isMe(){
		return identity!=null && identity.equals(Cluster.INSTANCE.getId());
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isOnline() {
		return online;
	}
	
	public void setDiscoveredIdentity(String anIdentity){
		identity = anIdentity;
		firstSeen = System.currentTimeMillis();
		setOnline(true);
		
	}

	public void setOnline(boolean online) {
		this.online = online;
		lastChecked = System.currentTimeMillis();
		if (online)
			lastSeen = System.currentTimeMillis();
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public long getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(long lastSeen) {
		this.lastSeen = lastSeen;
	}
	
	public String getLastSeenString(){
		return getTimeString(lastSeen);
	}
	
	public String getFirstSeenString(){
		return getTimeString(firstSeen);
	}

	public String getLastCheckedString(){
		return getTimeString(lastChecked);
	}

	private String getTimeString(long when){
		return when == 0 ? "Never" : NumberUtils.makeISO8601TimestampString(when);
		
	}
}
