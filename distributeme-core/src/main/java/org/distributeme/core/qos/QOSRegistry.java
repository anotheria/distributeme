package org.distributeme.core.qos;

import org.configureme.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Quality of Service registry contains references to all active outgoing calls and blacklisted service ids.
 *
 * @author lrosenberg
 * @since 20.02.15 17:29
 * @version $Id: $Id
 */
public final class QOSRegistry {
	/**
	 * Log.
	 */
	private static final Logger log = LoggerFactory.getLogger(QOSRegistry.class);

	/**
	 * Singleton instances.
	 */
	private static final QOSRegistry instance = new QOSRegistry();

	/**
	 * Currently running requests. They are inspected in regular intervals.
	 */
	private ConcurrentHashMap<String, QOSEntry> entries = new ConcurrentHashMap<String, QOSEntry>();

	/**
	 * Currently blacklisted entries. Key is the service id.
	 */
	private ConcurrentHashMap<String,QOSEntry> blacklist = new ConcurrentHashMap<String, QOSEntry>();

	/**
	 * Configuration.
	 */
	private QOSConfiguration configuration = new QOSConfiguration();

	/**
	 * Creates new registry.
	 */
	private QOSRegistry(){
		QOSRegistryRunnable r = new QOSRegistryRunnable();
		Thread checker = new Thread(r);
		checker.setDaemon(true);
		checker.setName("QOS-Checker");
		checker.start();

		try{
			ConfigurationManager.INSTANCE.configure(configuration);
		}catch(IllegalArgumentException e){
			log.warn("Can't configure QOSRegistry", e);

		}
	}

	/**
	 * Returns the singleton instance.
	 *
	 * @return a {@link org.distributeme.core.qos.QOSRegistry} object.
	 */
	public static final QOSRegistry getInstance(){
		return instance;
	}

	/**
	 * Called before a call is executed. Returns if the call may be executed.
	 *
	 * @param serviceId a {@link java.lang.String} object.
	 * @param callId a {@link java.lang.String} object.
	 * @return true if the call should be executed, false if the target service is blacklisted.
	 */
	public boolean callStarted(String serviceId, String callId) {
		if (blacklist.containsKey(serviceId))
			return false;
		QOSEntry entry = new QOSEntry(serviceId, callId);
		entries.put(entry.getKey(), entry);
		return true;
	}

	/**
	 * Called when a call has returned.
	 *
	 * @param serviceId a {@link java.lang.String} object.
	 * @param callId a {@link java.lang.String} object.
	 */
	public void callFinished(String serviceId, String callId){
		entries.remove(QOSEntry.getKey(serviceId, callId));
	}

	/**
	 * Regular checker.
	 */
	class QOSRegistryRunnable implements  Runnable{
		@Override
		public void run() {
			while (true) {

				//at the beginning we copy all old blacklist entries. If after the run something remains in this list, it means that the service went back to good and should be removed from blacklist.
				HashMap<String, QOSEntry> oldBlackList = new HashMap<String, QOSEntry>();
				oldBlackList.putAll(blacklist);

				Collection<QOSEntry> allEntries = entries.values();
				//System.out.println("QOS has to check " + allEntries.size() + " --- timeout: " + configuration.getTimeoutBeforeBlackList());
				for (QOSEntry entry : allEntries) {
					//System.out.println("Currently running " + entry);
					if (entry.getAge() > configuration.getTimeoutBeforeBlackList()) {
						entry.setBlacklistedUntil(System.currentTimeMillis() + configuration.getBlacklistDuration());
						//blacklist entry
						log.info(entry.getServiceId()+" is blacklisted");
						oldBlackList.remove(entry.getServiceId());
						blacklist.put(entry.getServiceId(), entry);
					}
				}

				//now check if we have something left in oldblacklist
				for (QOSEntry blacklistedEntry : oldBlackList.values()){
					if (blacklistedEntry.isBlacklistExpired()) {
						log.info(blacklistedEntry.getServiceId() + " recovered");
						blacklist.remove(blacklistedEntry.getServiceId());
					}
				}
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
