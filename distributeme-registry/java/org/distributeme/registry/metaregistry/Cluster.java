package org.distributeme.registry.metaregistry;

import net.anotheria.util.IdCodeGenerator;
import net.anotheria.util.StringUtils;
import net.anotheria.util.queue.IQueueWorker;
import net.anotheria.util.queue.QueuedProcessor;
import net.anotheria.util.queue.UnrecoverableQueueOverflowException;
import org.configureme.ConfigurationManager;
import org.distributeme.core.RegistryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This class supports operations that are needed for registry clustering.
 * @author lrosenberg
 */
public enum Cluster {
	/**
	 * Singleton instance.
	 */
	INSTANCE;
	
	/**
	 * The id of the node is unique and generated on startup.
	 */
	private String id = IdCodeGenerator.generateCode(20);
	
	/**
	 * Entires of this cluster.
	 */
	private List<ClusterEntry> entries = new ArrayList<ClusterEntry>();
	
	/**
	 * QueuedProcessor for asynchronous process of commands.
	 */
	private QueuedProcessor<ClusterSyncCommand> syncCommandProcessor;
	
	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(Cluster.class);
	
	/**
	 * Configuration.
	 */
	private ClusterConfiguration configuration;

	/**
	 * Singleton.
	 */
	private Cluster(){
	}
	
	public void init(){
		configuration = new ClusterConfiguration();
		try{
			ConfigurationManager.INSTANCE.configure(configuration);
		}catch(IllegalArgumentException e){
			//ignore
		}
		MetaRegistryImpl.getInstance().addListener(new ClusterRegistryListener());
		
		syncCommandProcessor = new QueuedProcessor<ClusterSyncCommand>("ClusterSyncCommandProcessor", new ClusterWorker(), 5000, 50, log);
		syncCommandProcessor.start();
		
	}

	void reconfigure(String registryCluster){
		log.info("Configuring registry cluster as "+registryCluster);
		if (registryCluster==null || registryCluster.length()==0)
			return;
		String tokens[] = StringUtils.tokenize(registryCluster, ',');
		for (String t : tokens){
			String[] tokens2 = StringUtils.tokenize(t, ':');
			ClusterEntry e = new ClusterEntry(tokens2[0], Integer.parseInt(tokens2[1]));
			//System.out.println("Parsed cluster entry "+e);
			entries.add(e);
		}
		log.info("Configured registry cluster with "+entries.size()+" entries: "+entries);
	}
	
	public List<ClusterEntry> entries(){
		return entries;
	}
	
	public boolean isClusterActive(){
		return entries!=null && entries.size()>0;
	}
	
	public void addSyncCommand(ClusterSyncCommand command){
		try{
			syncCommandProcessor.addToQueue(command);
		}catch(UnrecoverableQueueOverflowException e){
			log.error("Couldn't schedule command due to queue overflow, cluster is probably NOT de-synched.");
		}
	}
	
	/**
	 * The worker processes cluster sync commands in the update queue.
	 * @author lrosenberg
	 *
	 */
	class ClusterWorker implements IQueueWorker<ClusterSyncCommand>{

		@Override
		public void doWork(ClusterSyncCommand command) throws Exception {
			if (log.isDebugEnabled())
				log.debug("new sync command "+command);

			for (ClusterEntry entry : entries){
				if (entry.isMe())
					continue;
				boolean result;
				switch(command.getOperation()){
				case UNBIND:
					result = RegistryUtil.notifyUnbind(entry, command.getDescriptor());
					break;
				case BIND:
					result = RegistryUtil.notifyBind(entry, command.getDescriptor());
					break;
				default:
					throw new IllegalArgumentException("Unknown operation of the sync command "+command);
				}
				if (log.isDebugEnabled()){
					log.debug("Transmitted "+command+" on "+entry+" with success: "+result);
				}
				if (!result)
					log.warn("Couldn't transmit "+command+" to "+entry);
			}
		}
		
	}

	/**
	 * Returns the id of this node of the cluster.
	 * @return
	 */
	public String getId(){
		return id;
	}
	
	public ClusterConfiguration getConfiguration(){
		return configuration;
	}
}
