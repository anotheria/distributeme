package org.distributeme.registry.servlet;

import net.anotheria.moskito.aop.annotation.Monitor;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.conventions.WebOperations;
import org.distributeme.core.util.EventServiceRegistryUtil;
import org.distributeme.registry.esregistry.ChannelDescriptor;
import org.distributeme.registry.esregistry.EventServiceRegistry;
import org.distributeme.registry.esregistry.EventServiceRegistryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Servlet that enables access to the event service registry via http protocol.
 * @author lrosenberg
 *
 */
@Monitor(producerId = "EventServiceRegistryServlet", category = "servlet", subsystem = "distributeme")
public class EventServiceRegistryServlet extends BaseRegistryServlet{
	/**
	 * SerialUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Registry instance.
	 */
	private transient EventServiceRegistry registry = EventServiceRegistryImpl.getInstance();
	/**
	 * Logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(EventServiceRegistryServlet.class);
	
	@Override
	protected void moskitoDoGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
	
		String op = getOperation(req);
		//NOTE this can throw an illegal argument exception, but since this servlet will never be exposed, its ok, and there
		//is no further error handling needed.
		WebOperations operation = WebOperations.valueOf(op.toUpperCase());

		switch(operation){
		case ADD_CONSUMER:
			addConsumer(req, res);
			break;
		case ADD_SUPPLIER:
			addSupplier(req, res);
			break;
		case LIST:
			list(req, res);
			break;
		case NOTIFY_CONSUMER:
			notifyConsumerUnavailable(req, res);
			break;
		case NOTIFY_SUPPLIER:
			notifySupplierUnavailable(req, res);
			break;
		case RESET:
			reset(req, res);
			break;

		default:
			throw new IllegalArgumentException("Parseable but (yet) unsupported operation.");
		}
	}
	
	/**
	 * Reads the channel parameter from the incoming request. Throws an exception if the parameter is not present.
	 */
	private String getChannelParameter(HttpServletRequest req){
		return getRequiredParameter(req, "channel");
	}
	
	/**
	 * Reads the descriptor id parameter from the incoming request. Throws an exception if the parameter is not present.
	 */
	private String getDescriptorParameter(HttpServletRequest req){
		return getRequiredParameter(req, "id");
	}
	
	private void addSupplier(HttpServletRequest req, HttpServletResponse res){
		String channel = getChannelParameter(req);
		String supplierAsString = getDescriptorParameter(req);
		ServiceDescriptor supplier = ServiceDescriptor.fromSystemWideUniqueId(supplierAsString);
		List<ServiceDescriptor> consumers = registry.addSupplier(channel, supplier);
		sendResponse(res, EventServiceRegistryUtil.list2string(consumers));
	}
	
	private void addConsumer(HttpServletRequest req, HttpServletResponse res){
		String channel = getChannelParameter(req);
		String consumerAsString = getDescriptorParameter(req);
		ServiceDescriptor consumer = ServiceDescriptor.fromSystemWideUniqueId(consumerAsString);
		List<ServiceDescriptor> suppliers = registry.addConsumer(channel, consumer);
		sendResponse(res, EventServiceRegistryUtil.list2string(suppliers));
		
	}

	private void notifyConsumerUnavailable(HttpServletRequest req, HttpServletResponse res){
		String descriptorAsString = getDescriptorParameter(req);
		ServiceDescriptor descriptor = ServiceDescriptor.fromSystemWideUniqueId(descriptorAsString);
		boolean result = true;
		try{
			registry.notifyConsumerUnavailable(descriptor);
		}catch(Exception e){
			result = false;
			LOG.error("notifyConsumerUnavailable", e);
		}
		sendBooleanResponse(res, result);
	}

	private void notifySupplierUnavailable(HttpServletRequest req, HttpServletResponse res){
		String descriptorAsString = getDescriptorParameter(req);
		ServiceDescriptor descriptor = ServiceDescriptor.fromSystemWideUniqueId(descriptorAsString);
		boolean result = true;
		try{
			registry.notifySupplierUnavailable(descriptor);
		}catch(Exception e){
			result = false;
			LOG.error("notifySupplierUnavailable", e);
		}
		sendBooleanResponse(res, result);
	}
	
	private void reset(HttpServletRequest req, HttpServletResponse res){//NOPMD
		registry.reset();
		sendBooleanResponse(res, Boolean.TRUE);
	}

	//yes, this isn't nice, but it had to be worked out fast.
	private void list(HttpServletRequest req, HttpServletResponse res){
		
		StringBuilder m = new StringBuilder();
		m.append("<?xml version=\"1.0\"?>");
		m.append("<channels>");
		List<ChannelDescriptor> channels = registry.getChannels();
		for (ChannelDescriptor channel : channels){
			m.append("<channel name=\"").append(channel.getName()).append("\"");
			m.append(">");
			m.append('\n');
			m.append("<suppliers>");
			for (ServiceDescriptor supplier : channel.getSuppliers()){
				m.append("<supplier>"+supplier.getSystemWideUniqueId()+"</supplier>");
				m.append('\n');
			}
			m.append("</suppliers>");
			m.append('\n');
			m.append("<consumers>");
			for (ServiceDescriptor consumer : channel.getConsumers()){
				m.append("<consumer>"+consumer.getSystemWideUniqueId()+"</consumer>");
				m.append('\n');
			}
			m.append("</consumers>");
			m.append('\n');
			m.append("</channel>");
			m.append('\n');
		}
		m.append("</channels>");
		
		String message = m.toString();

		byte[] messageBytes = message.getBytes(Charset.forName("UTF-8"));
		res.setContentLength(messageBytes.length);
		res.setContentType("text/xml");
		OutputStream out = null;
		try{
			 out = res.getOutputStream();
			 out.write(messageBytes);
			 out.flush();
		}catch(IOException e){
			LOG.warn("sendResponse(res, "+message+")", e);
		}finally{
			if (out!=null){
				try{
					out.close();
				}catch(IOException ignored){//NOPMD
				}
			}
		}
		
	}
}
