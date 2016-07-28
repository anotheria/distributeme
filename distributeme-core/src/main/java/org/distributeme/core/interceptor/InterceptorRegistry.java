package org.distributeme.core.interceptor;

import net.anotheria.util.BasicComparable;
import net.anotheria.util.sorter.DummySortType;
import net.anotheria.util.sorter.IComparable;
import net.anotheria.util.sorter.StaticQuickSorter;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.SetIf;
import org.configureme.annotations.SetIf.SetIfCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The registry for the interceptors.
 * @author lrosenberg
 *
 */
public final class InterceptorRegistry {
	/**
	 * The singleton instances.
	 */
	private static final InterceptorRegistry instance ;
	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(InterceptorRegistry.class);
	
	/**
	 * Configured clientSideInterceptors.
	 */
	private volatile List<ClientSideRequestInterceptor> clientSideInterceptors = new ArrayList<>();
	/**
	 * Configured serverSideInterceptors.
	 */
	private volatile List<ServerSideRequestInterceptor> serverSideInterceptors = new ArrayList<>();
	
	/**
	 * Returns the singleton instances.
	 * @return
	 */
	public static final InterceptorRegistry getInstance(){ return instance; }
	
	static{
		instance = new InterceptorRegistry();
		Config config = new Config();
		try{
			ConfigurationManager.INSTANCE.configure(config);
		}catch(IllegalArgumentException e){
			LoggerFactory.getLogger(InterceptorRegistry.class).warn("No DistributeMe config (distributeme.json), interceptors aren't configured either");
		}
	}
	
	private InterceptorRegistry(){
	}
	
	public List<ClientSideRequestInterceptor> getClientSideRequestInterceptors(){
		return clientSideInterceptors;
	}
	
	public List<ServerSideRequestInterceptor> getServerSideRequestInterceptors(){
		return serverSideInterceptors;
	}
	
	static void buildInterceptors(Iterable<InterceptorEntry> entries, String[] interceptorNames){
		List<ClientSideRequestInterceptor> clientSide = new ArrayList<>();
		List<ServerSideRequestInterceptor> serverSide = new ArrayList<>();
		for (InterceptorEntry entry : entries){
			try {
				Object interceptorInstance = Class.forName(entry.clazzName).newInstance();
				if (interceptorInstance instanceof ClientSideRequestInterceptor){
					clientSide.add((ClientSideRequestInterceptor)interceptorInstance);
				}
				if (interceptorInstance instanceof ServerSideRequestInterceptor){
					serverSide.add((ServerSideRequestInterceptor)interceptorInstance);
				}
			} catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
				log.error("buildInterceptors(... "+entry.clazzName+ ')', e);
			}
        }
		if (interceptorNames!=null){
			for (String interceptorName : interceptorNames){
				try {
					Object interceptorInstance = Class.forName(interceptorName).newInstance();
					if (interceptorInstance instanceof ClientSideRequestInterceptor){
						clientSide.add((ClientSideRequestInterceptor)interceptorInstance);
					}
					if (interceptorInstance instanceof ServerSideRequestInterceptor){
						serverSide.add((ServerSideRequestInterceptor)interceptorInstance);
					}
				} catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
					log.error("buildInterceptors(... "+interceptorName+ ')', e);
				}
            }
		}
		instance.clientSideInterceptors = clientSide;
		instance.serverSideInterceptors = serverSide;
	}
	
	/**
	 * Internal configuration holder class.
	 * @author lrosenberg
	 *
	 */
	@ConfigureMe(name="distributeme")
	public static class Config{
		/**
		 * Old style interceptor config.
		 */
		@Configure private String[] interceptors;
		/**
		 * Interceptor entries.
		 */
		private List<InterceptorEntry> interceptorEntries = new ArrayList<>();
		
		@AfterConfiguration public void reconfigure(){
			buildInterceptors(StaticQuickSorter.sort(interceptorEntries, new DummySortType()), interceptors);
		}
		
		public void setInterceptors(String[] someInterceptors){
			interceptors = someInterceptors;
		}
		
		@SetIf(condition=SetIfCondition.startsWith, value="interceptor.")
		public void addInterceptor(String interceptorKey, String clazzName){
			int indexOfDot = interceptorKey.indexOf('.');
			int interceptorNumber = Integer.parseInt(interceptorKey.substring(indexOfDot+1));
			interceptorEntries.add(new InterceptorEntry(interceptorNumber, clazzName));
		}
	}
	
	/**
	 * Helper class for configuration purposes.
	 * @author lrosenberg
	 *
	 */
	static class InterceptorEntry implements IComparable<InterceptorEntry>{
		/**
		 * Number is used to sort interceptors.
		 */
		private int number;
		/**
		 * 
		 */
		private String clazzName;
		InterceptorEntry(int aNumber, String aClazzName){
			number = aNumber;
			clazzName = aClazzName;
		}
		@Override
		public int compareTo(IComparable<? extends InterceptorEntry> anotherEntry,
				int arg1) {
			return BasicComparable.compareInt(number, ((InterceptorEntry)anotherEntry).number);
		}
		
		
	}
		
}
