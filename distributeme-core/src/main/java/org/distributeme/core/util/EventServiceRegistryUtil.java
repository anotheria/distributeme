package org.distributeme.core.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.anotheria.util.StringUtils;
import org.distributeme.core.Location;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.conventions.WebOperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility classes for requests to the registry via http.
 * @author lrosenberg
 *
 */
public class EventServiceRegistryUtil extends BaseRegistryUtil{
	
	/**
	 * List delimiter.
	 */
	private static final char DELIMITER = ',';
	/**
	 * Cached empty list used as return value on some failures.
	 */
	private static final List<ServiceDescriptor> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<ServiceDescriptor>(0));
	/**
	 * Web-Application name.
	 */
	public static final String APP = "esregistry";
	/**
	 * Converts a list of service descriptors to a string.
	 * @param descriptors
	 * @return
	 */
	public static final String list2string(List<ServiceDescriptor> descriptors){
		StringBuilder ret = new StringBuilder();
		for (ServiceDescriptor d : descriptors){
			if (ret.length()>0)
				ret.append(DELIMITER);
			ret.append(d.getSystemWideUniqueId());
		}
		return ret.toString();
	}
	/**
	 * Converts a list of string representations of service descriptors to a list of ServiceDescriptor objects. 
	 * @param encodedString
	 * @return
	 */
	public static final List<ServiceDescriptor> string2list(String encodedString){
		String tokens[] = StringUtils.tokenize(encodedString, DELIMITER);
		ArrayList<ServiceDescriptor> ret = new ArrayList<ServiceDescriptor>();
		for(String t : tokens){
			ret.add(ServiceDescriptor.fromSystemWideUniqueId(t));
		}
		return ret;
	}
	
	/**
	 * Internal helper class for consumer/supplier operation target separation.
	 * @author another
	 *
	 */
	private static enum Target{
		/**
		 * EventService consumer.
		 */
		CONSUMER(WebOperations.ADD_CONSUMER),
		/**
		 * EventService suppliers.
		 */
		SUPPLIER(WebOperations.ADD_SUPPLIER);
		/**
		 * Associated web operation.
		 */
		private WebOperations operation;
		
		private Target(WebOperations anOperation){
			operation = anOperation;
		}
		/**
		 * Returns the associated registration operation.
		 * @return
		 */
		public WebOperations getOperation(){
			return operation;
		}
		/**
		 * Returns the associated notify-operation.
		 * @return
		 */
		public WebOperations getNotifyOperation(){
			switch (operation){
			case ADD_CONSUMER:
				return WebOperations.NOTIFY_CONSUMER;
			case ADD_SUPPLIER:
				return WebOperations.NOTIFY_SUPPLIER;
			default:
				throw new IllegalArgumentException("Unsupported 'as' target");
			}
		}
	}

	@SuppressFBWarnings("DM_DEFAULT_ENCODING")
	protected static final List<ServiceDescriptor> registerAtRegistryAs(String channelName, Target as, ServiceDescriptor descriptor){
		String url = getRegistryBaseUrl();
		url += as.getOperation().toWeb();
		url += "?";
		url += "channel="+encode(channelName);
		url += "&id="+encode(descriptor.getSystemWideUniqueId());

		byte data[] = getUrlContent(url);
		if (data == null )
			throw new RuntimeException("Got no reply from registry");
		
		String reply = new String(data);
		String[] tokens = StringUtils.tokenize(reply, DELIMITER);
		if (tokens.length==0)
			return EMPTY_LIST;
		List<ServiceDescriptor> ret = new ArrayList<ServiceDescriptor>(tokens.length);
		for (String t : tokens){
			ret.add(ServiceDescriptor.fromSystemWideUniqueId(t));
		}
		
		return ret;
	}
	
	public static final List<ServiceDescriptor> registerConsumerAtRegistryAndGetSuppliers(String channelName, ServiceDescriptor myConsumer){
		return registerAtRegistryAs(channelName, Target.CONSUMER, myConsumer);
	}

	public static final List<ServiceDescriptor> registerSupplierAtRegistryAndGetConsumers(String channelName, ServiceDescriptor mySupplier){
		return registerAtRegistryAs(channelName, Target.SUPPLIER, mySupplier);
	}

	public static final void notifyConsumerNotAvailable(ServiceDescriptor consumer){
		notifyNotAvailable(Target.CONSUMER, consumer);
	}

	public static final void notifySupplierNotAvailable(ServiceDescriptor supplier){
		notifyNotAvailable(Target.SUPPLIER, supplier);
	}

	/**
	 * Offers a possibility to mark a descriptor as unavailable.
	 * @param as the type of descriptor, supplier or consumer.
	 * @param descriptor
	 */
	@SuppressFBWarnings("DM_DEFAULT_ENCODING")
	public static final void notifyNotAvailable(Target as, ServiceDescriptor descriptor){
		String url = getRegistryBaseUrl();
		url += as.getNotifyOperation().toWeb();
		url += "?";
		url += "id="+encode(descriptor.getSystemWideUniqueId());

		byte data[] = getUrlContent(url);
		if (data == null )
			throw new RuntimeException("Got no reply from registry");
		
		String reply = new String(data);
		if ("ERROR".equals(reply))
			throw new RuntimeException("Notify failed");
	}

	/**
	 * Returns the base url for this registry application. Globally configured host and port are used.
	 * @return
	 */
	protected static String getRegistryBaseUrl(){
		return getRegistryBaseUrl(APP);
	}
	
	/**
	 * Returns the base url for this registry application.
	 * @param host customized host.
	 * @param port customized port.
	 * @return
	 */
	protected static String getRegistryBaseUrl(Location location){
		return getRegistryBaseUrl(APP, location.getHost(), location.getPort(), location.getProtocol(), location.getContext());
	}
}
