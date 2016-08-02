package org.distributeme.core;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.ConfigureMe;

/**
 * Configuration holder class for some output options.
 * @author lrosenberg
 *
 */
@ConfigureMe(allfields=true,name="distributeme")
public class Verbosity {
	/**
	 * Singleton instance.
	 */
	private static Verbosity instance = new Verbosity();
	
	static{
		try{
			ConfigurationManager.INSTANCE.configure(instance);
		}catch(IllegalArgumentException ignored){
			;//ignored
		}
	}
	/**
	 * If true server side exceptions will be logged. Default is false.
	 */
	private boolean logServerSideExceptions = false;
	/**
	 * If true client side exceptions will be logged. Default is false;
	 */
	private boolean logClientSideExceptions = false;
	
	public void setLogServerSideExceptions(boolean aValue){
		logServerSideExceptions = aValue;
	}
	
	public void setLogClientSideExceptions(boolean aValue){
		logClientSideExceptions = aValue;
	}

	public static boolean logServerSideExceptions(){
		return instance.logServerSideExceptions;
	}
	
	public static boolean logClientSideExceptions(){
		return instance.logClientSideExceptions;
	}
	
	@Override public String toString(){
		return "( logServerSideExceptions: "+logServerSideExceptions+
			" logClientSideExceptions: "+logClientSideExceptions+
                ')';
	}
}
