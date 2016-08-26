package org.distributeme.core;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.ConfigureMe;

/**
 * Configuration holder class for some output options.
 *
 * @author lrosenberg
 * @version $Id: $Id
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
	
	/**
	 * <p>Setter for the field <code>logServerSideExceptions</code>.</p>
	 *
	 * @param aValue a boolean.
	 */
	public void setLogServerSideExceptions(boolean aValue){
		logServerSideExceptions = aValue;
	}
	
	/**
	 * <p>Setter for the field <code>logClientSideExceptions</code>.</p>
	 *
	 * @param aValue a boolean.
	 */
	public void setLogClientSideExceptions(boolean aValue){
		logClientSideExceptions = aValue;
	}

	/**
	 * <p>logServerSideExceptions.</p>
	 *
	 * @return a boolean.
	 */
	public static boolean logServerSideExceptions(){
		return instance.logServerSideExceptions;
	}
	
	/**
	 * <p>logClientSideExceptions.</p>
	 *
	 * @return a boolean.
	 */
	public static boolean logClientSideExceptions(){
		return instance.logClientSideExceptions;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return "( logServerSideExceptions: "+logServerSideExceptions+
			" logClientSideExceptions: "+logClientSideExceptions+
			")";
	}
}
