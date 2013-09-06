package org.distributeme.registry.metaregistry;

import org.distributeme.core.RegistryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ClusterChecker extends  TimerTask{
	
	private static Logger log = LoggerFactory.getLogger(ClusterChecker.class);
	
	private static Timer timer;
	
	private ClusterChecker(){
	}
	
	public void run(){
		List<ClusterEntry> entries = Cluster.INSTANCE.entries();
		if (log.isDebugEnabled()){
			log.debug("Checking entries "+entries);
		}
		for (ClusterEntry entry : entries){
			log.debug("checking "+entry);
			if (entry.isMe()){
				log.debug("Skiped my reference "+entry);
				continue;
			}
			try{
				String pingreply = RegistryUtil.ping(entry);
				if (pingreply==null){
					entry.setOnline(false);
				}else{
					if (entry.getIdentity()==null){
						entry.setDiscoveredIdentity(pingreply);
					}else{
						if (pingreply.equals(entry.getIdentity())){
							entry.setOnline(true);
						}else{
							log.debug("unexpected reply from "+entry+" --> "+pingreply+" possible entry restart");
							entry.setDiscoveredIdentity(pingreply);
						}
					}
				}
			}catch(Exception e){
				System.out.println("aborted due to "+e);
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] a){
		start();
	}

	public static void start() {
		ClusterChecker checker = new ClusterChecker();
		timer = new Timer("ClusterChecker", true);
		timer.scheduleAtFixedRate(checker, 0L, Cluster.INSTANCE.getConfiguration().getClusterCheckPeriod());
	}
	
	public static void stop(){
		if (timer!=null)
			timer.cancel();
	}
}
