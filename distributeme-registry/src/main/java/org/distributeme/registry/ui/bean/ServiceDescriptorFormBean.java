package org.distributeme.registry.ui.bean;

import net.anotheria.util.BasicComparable;
import net.anotheria.util.sorter.IComparable;

import static org.distributeme.registry.ui.bean.ServiceDescriptorFormBeanSortType.*;
/**
 * Bean for communication between controller and view.
 * 
 * @author dsilenko
 */
public class ServiceDescriptorFormBean implements IComparable{
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
	 * The protocol this instance supports.
	 */
	private String protocol;
	/**
	 * A unique id (random generated) which is only valid for the live time of the one instance.
	 */
	private String instanceId;
	/**
	 * Full service id.
	 */
	private String globalServiceId;
	/**
	 * Registration string of binded service.
	 */
	private String registrationString;
	/**
	 * Is service online or not.
	 */
	private boolean isOnline;

	/**
	 * Default constructor.
	 */
	public ServiceDescriptorFormBean() {
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getGlobalServiceId() {
		return globalServiceId;
	}

	public void setGlobalServiceId(String globalServiceId) {
		this.globalServiceId = globalServiceId;
	}

	public String getRegistrationString() {
		return registrationString;
	}

	public void setRegistrationString(String registrationString) {
		this.registrationString = registrationString;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean online) {
		isOnline = online;
	}

	@Override
	public String toString() {
		return "ServiceDescriptorFormBean{" +
				"serviceId='" + serviceId + '\'' +
				", host='" + host + '\'' +
				", port='" + port + '\'' +
				", protocol='" + protocol + '\'' +
				", instanceId='" + instanceId + '\'' +
				", globalServiceId='" + globalServiceId + '\'' +
				", registrationString='" + registrationString + '\'' +
				", isOnline=" + isOnline +
				'}';
	}

	@Override
	public int compareTo(IComparable anotherComprable, int method) {
		ServiceDescriptorFormBean bean = (ServiceDescriptorFormBean)anotherComprable;
		switch(method){
		case SORT_BY_SERVICEID:
			return BasicComparable.compareString(serviceId, bean.serviceId);
		case SORT_BY_HOST:
			return BasicComparable.compareString(host, bean.host);
		case SORT_BY_PORT:
			return BasicComparable.compareInt(port, bean.port);
		case SORT_BY_PROTOCOL:
			return BasicComparable.compareString(protocol, bean.protocol);
		case SORT_BY_INSTANCEID:
			return BasicComparable.compareString(instanceId, bean.instanceId);
		case SORT_BY_GLOBALSERVICEID:
			return BasicComparable.compareString(globalServiceId, bean.globalServiceId);
		default:
				throw new IllegalArgumentException("Unsupported method for sorting "+method);
		
		}
	}
}
