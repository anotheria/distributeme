package org.distributeme.core;

import net.anotheria.util.BasicComparable;

import java.io.Serializable;
import java.util.Calendar;

/**
 * This class represents a resolvable address of a service.
 * A server address is build up like following:
 * <protocol>://<serviceid>.<instanceid>@<host>:<port>
 * @author lrosenberg
 *
 */
public class ServiceDescriptor implements Serializable, Cloneable{
	
	/**
	 * serial version uid.
	 */
	private static final long serialVersionUID = 2854220693966853387L;
	/**
	 * Separator between protocol part of the descriptor and the rest of the descriptor.
	 */
	public static final String PROTOCOL_SEPARATOR = "://";
	/**
	 * Separator between host part and interface part in the string representation.
	 */
	public static final char HOST_SEPARATOR = '@';
	/**
	 * Separator between host and port.
	 */
	public static final char PORT_SEPARATOR = ':';
	/**
	 * Separator between serviceId and instanceId.
	 */
	public static final char INSTANCE_SEPARATOR = '.';
	/**
	 * Separator between port and startup timestamp.
	 */
	public static final char TIMESTAMP_SEPARATOR = '@';
	/**
	 * Supported protocols.
	 * @author lrosenberg
	 *
	 */
	public static enum Protocol{
		/**
		 * RMI
		 */
		RMI, 
		/**
		 * JAXWS aka WebService.
		 */
		JAXWS, 
		/**
		 * CORBA.
		 */
		CORBA,
		/**
		 * Jax Rest Service.
		 */
		JAXRS
	}
	
	/**
	 * The protocol this instance supports.
	 */
	private Protocol protocol;
	/**
	 * The id of the service - usually a string delivered from the interface and package name.
	 */
	private String serviceId;
	/**
	 * Host on which the service is running.
	 */
	private String host;
	/**
	 * Port on which the local rmi registry is running.
	 */
	private int port;
	/**
	 * A unique id (random generated) which is only valid for the live time of the one instance.
	 */
	private String instanceId;	
	/**
	 * Timestamp in milliseconds.
	 */
	private long timestamp;
	
	/**
	 * This constructor is used for registration.
	 * @param aProtocol
	 * @param aServiceId
	 */
	public ServiceDescriptor(Protocol aProtocol, String aServiceId, String anInstanceId, String aHost, int aPort){
		this(aProtocol, aServiceId, anInstanceId, aHost, aPort, 0);
	}

	public ServiceDescriptor(Protocol aProtocol, String aServiceId, String anInstanceId, String aHost, int aPort, long aTimestamp){
		if (aProtocol==null)
			throw new IllegalArgumentException("Null protocol is not allowed");
		if (aServiceId==null || aServiceId.isEmpty())
			throw new IllegalArgumentException("Null or empty serviceId is not allowed");
		if (anInstanceId==null || anInstanceId.isEmpty())
			throw new IllegalArgumentException("Null or empty instanceId is not allowed");
		if (aHost==null || aHost.isEmpty())
			throw new IllegalArgumentException("Null or empty host is not allowed");
		
		protocol = aProtocol;
		serviceId = aServiceId;
		instanceId = anInstanceId;
		host = aHost;
		port = aPort;		
		timestamp = (aTimestamp > 0) ? aTimestamp : System.currentTimeMillis();
	}

	/**
	 * This constructor is used for the lookup.
	 * @param aProtocol
	 * @param aServiceId
	 */
	public ServiceDescriptor(Protocol aProtocol, String aServiceId){
		if (aProtocol==null)
			throw new IllegalArgumentException("Null protocol is not allowed");
		if (aServiceId==null || aServiceId.isEmpty())
			throw new IllegalArgumentException("Null or empty serviceId is not allowed");
		protocol = aProtocol;
		serviceId = aServiceId;
	}

	public String getGlobalServiceId(){
		return protocol.toString().toLowerCase()+"://"+serviceId;
	}
	
	public String getRegistrationString(){
		return getGlobalServiceId()+INSTANCE_SEPARATOR+instanceId+HOST_SEPARATOR+host+PORT_SEPARATOR+port+TIMESTAMP_SEPARATOR+getTimeString(timestamp);
	}
	
	public String getLookupString(){
		return getGlobalServiceId();
	}
 
	@Override public String toString(){
		return getRegistrationString();
	}
	
	public final String getSystemWideUniqueId(){
		return getRegistrationString();
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Virtually the same as <b>fromRegistrationString</b> but better named ;-).
	 * @param systemWideUniqueId
	 * @return
	 */
	public static final ServiceDescriptor fromSystemWideUniqueId(String systemWideUniqueId){
		return fromRegistrationString(systemWideUniqueId);
	}
	
	/**
	 * Factory method to create a service descriptor from a registration string (used for bind in the registry).
	 * @param registrationString
	 * @return
	 */
	public static final ServiceDescriptor fromRegistrationString(String registrationString){
		int indexOfHost = registrationString.indexOf(HOST_SEPARATOR);
		
		long timestamp = 0;
		int indexOfTimestamp = registrationString.substring(indexOfHost+1).indexOf(TIMESTAMP_SEPARATOR);
		if(indexOfTimestamp >= 0) {
			indexOfTimestamp += indexOfHost + 1;
			timestamp = parseTimeString(registrationString.substring(indexOfTimestamp + 1));
		} else
			indexOfTimestamp = registrationString.length();
		
		String hostAndPort = registrationString.substring(indexOfHost+1, indexOfTimestamp);
		String protocolAndServiceId = registrationString.substring(0, indexOfHost);
		
		int portSeparatorPos = hostAndPort.lastIndexOf(PORT_SEPARATOR);
		String host = hostAndPort.substring(0, portSeparatorPos);
		String port = hostAndPort.substring(portSeparatorPos + 1, hostAndPort.length());
		
		String protocol = protocolAndServiceId.substring(0, protocolAndServiceId.indexOf(':'));
		String serviceAndInstanceId = protocolAndServiceId.substring(protocolAndServiceId.indexOf('/')+2);
		int indexOfInstanceIdSeparator = serviceAndInstanceId.lastIndexOf(INSTANCE_SEPARATOR);
		String serviceId = serviceAndInstanceId.substring(0, indexOfInstanceIdSeparator);
		String instanceId = serviceAndInstanceId.substring(indexOfInstanceIdSeparator+1);
		
		return new ServiceDescriptor(Protocol.valueOf(protocol.toUpperCase()), serviceId, instanceId, host, Integer.parseInt(port), timestamp);
	}
	
	/**
	 * Factory method to create a service descriptor from a resolve string (used for lookup in the registry). 
	 * @param resolveString
	 * @return
	 */
	public static final ServiceDescriptor fromResolveString(String resolveString){
		String protocol = resolveString.substring(0, resolveString.indexOf(':'));
		String serviceId = resolveString.substring(resolveString.indexOf('/')+2);
		
		return new ServiceDescriptor(Protocol.valueOf(protocol.toUpperCase()), serviceId);
	}

	/**
	 * Changes the service id in the descriptor to another id. This is useful if you want to access a not publicly registered service (like eventservice or lifecycle servce) 
	 * via the public service id from the registry.
	 * @param serviceId target service id.
	 * @return new service descriptor with altered service id.
	 */
	public ServiceDescriptor changeServiceId(String aServiceId){
		try{
			ServiceDescriptor newSD = (ServiceDescriptor)this.clone();
			newSD.serviceId = aServiceId;
			return newSD;
		}catch(CloneNotSupportedException e){
			throw new AssertionError("Can't happen");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		result = prime * result
				+ ((protocol == null) ? 0 : protocol.hashCode());
		result = prime * result
				+ ((serviceId == null) ? 0 : serviceId.hashCode());
		result = prime * result
				+ ((instanceId == null) ? 0 : instanceId.hashCode());
		return result;
	}

	@Override public boolean equals(Object o){
		return o instanceof ServiceDescriptor ?
				protocol == ((ServiceDescriptor)o).protocol &&
				port == ((ServiceDescriptor)o).port &&
				BasicComparable.compareString(host, ((ServiceDescriptor)o).host)==0 &&
				BasicComparable.compareString(serviceId, ((ServiceDescriptor)o).serviceId)==0 && 
				BasicComparable.compareString(instanceId, ((ServiceDescriptor)o).instanceId)==0 
				: false;
	}
	
	public String getHost(){
		return host;
	}
	
	public int getPort(){
		return port;
	}
	
	public String getProtocol(){
		return protocol.toString().toLowerCase();
	}
	
	public String getServiceId(){
		return serviceId;
	}
	
	public String getInstanceId() {
		return instanceId;
	}

	public static String getTimeString(long timestamp) {
		assert timestamp > 0;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);

		return String.format("%04d%02d%02d%02d%02d%02d",
			calendar.get(Calendar.YEAR),
			calendar.get(Calendar.MONTH) + 1,
			calendar.get(Calendar.DAY_OF_MONTH),
			calendar.get(Calendar.HOUR_OF_DAY),
			calendar.get(Calendar.MINUTE),
			calendar.get(Calendar.SECOND));
	}
	
	public static long parseTimeString(String s) {
		assert s != null;
		
		// Number of characters per component
		final int[] numChars = {4, 2, 2, 2, 2, 2}; // yyyyMMDDHHmmss
		// Component values
		int[] vals = new int[numChars.length];		
		
		Calendar calendar = Calendar.getInstance();		

		try {
			for(int i = 0, j = 0; i < numChars.length; j += numChars[i], ++i)
				vals[i] = Integer.parseInt(s.substring(j, j + numChars[i]), 10);
		} catch(IndexOutOfBoundsException e) {
			return 0;
		} catch(NumberFormatException e) {
			return 0;
		}
		
		calendar.set(vals[0], vals[1] - 1, vals[2], vals[3], vals[4], vals[5]);
		
		return calendar.getTimeInMillis();		
	}
}
