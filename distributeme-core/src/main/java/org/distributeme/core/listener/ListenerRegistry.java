package org.distributeme.core.listener;

import net.anotheria.util.BasicComparable;
import net.anotheria.util.sorter.DummySortType;
import net.anotheria.util.sorter.IComparable;
import net.anotheria.util.sorter.StaticQuickSorter;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.SetIf;
import org.configureme.annotations.SetIf.SetIfCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The registry for the ServerLifecycleListener instances..
 * @author lrosenberg
 *
 */
public final class ListenerRegistry {
	/**
	 * The singleton instances.
	 */
	private static final ListenerRegistry instance ;
	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(ListenerRegistry.class);
	
	/**
	 * Configured listeners.
	 */
	private volatile List<ServerLifecycleListener> serverLifecycleListeners = new ArrayList<ServerLifecycleListener>();
	
	/**
	 * Returns the singleton instances.
	 * @return
	 */
	public static final ListenerRegistry getInstance(){ return instance; }
	
	static{
		instance = new ListenerRegistry();
		Config config = new Config();
		try{
			ConfigurationManager.INSTANCE.configure(config);
		}catch(IllegalArgumentException e){
			LoggerFactory.getLogger(ListenerRegistry.class).error("No distributeme config?!", e);
		}
	}
	
	private ListenerRegistry(){
	}
	
	public List<ServerLifecycleListener> getServerLifecycleListeners(){
		return serverLifecycleListeners;
	}
	
	static void buildListeners(List<ListenerEntry> entries){
		List<ServerLifecycleListener> newServerLifecycleListeners = new ArrayList<ServerLifecycleListener>();
		for (ListenerEntry entry : entries){
			try {
				Object listenerInstance = Class.forName(entry.clazzName).newInstance();
				if (listenerInstance instanceof ServerLifecycleListener){
					newServerLifecycleListeners.add((ServerLifecycleListener)listenerInstance);
				}
				
				//ADD FUTURE listeners here
			} catch (InstantiationException e) {
				log.error("buildInterceptors(... "+entry.clazzName+")", e);
			} catch (IllegalAccessException e) {
				log.error("buildInterceptors(... "+entry.clazzName+")", e);
			} catch (ClassNotFoundException e) {
				log.error("buildInterceptors(... "+entry.clazzName+")", e);
			}
		}
		instance.serverLifecycleListeners = newServerLifecycleListeners;
	}
	
	/**
	 * Internal configuration holder class.
	 * @author lrosenberg
	 *
	 */
	@ConfigureMe(name="distributeme")
	public static class Config{
		/**
		 * Interceptor entries.
		 */
		private List<ListenerEntry> listenerEntries = new ArrayList<ListenerEntry>();
		
		@AfterConfiguration public void reconfigure(){
			buildListeners(StaticQuickSorter.sort(listenerEntries, new DummySortType()));
		}
		
		@SetIf(condition=SetIfCondition.startsWith, value="listener.")
		public void addInterceptor(String listenerKey, String clazzName){
			int indexOfDot = listenerKey.indexOf('.');
			int listenerNumber = Integer.parseInt(listenerKey.substring(indexOfDot+1));
			listenerEntries.add(new ListenerEntry(listenerNumber, clazzName));
		}
	}
	
	/**
	 * Helper class for configuration purposes.
	 * @author lrosenberg
	 *
	 */
	static class ListenerEntry implements IComparable<ListenerEntry>{
		/**
		 * Number is used to sort listeners.
		 */
		private int number;
		/**
		 * 
		 */
		private String clazzName;
		ListenerEntry(int aNumber, String aClazzName){
			number = aNumber;
			clazzName = aClazzName;
		}
		@Override
		public int compareTo(IComparable<? extends ListenerEntry> anotherEntry,
				int arg1) {
			return BasicComparable.compareInt(number, ((ListenerEntry)anotherEntry).number);
		}
		
		@Override public String toString(){
			return number+" "+clazzName;
		}
	}
		
}
