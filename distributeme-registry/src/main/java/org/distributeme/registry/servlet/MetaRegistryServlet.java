package org.distributeme.registry.servlet;

import net.anotheria.moskito.aop.annotation.Monitor;
import org.distributeme.core.RegistryUtil;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.registry.metaregistry.Cluster;
import org.distributeme.registry.metaregistry.MetaRegistry;
import org.distributeme.registry.metaregistry.MetaRegistryConfig;
import org.distributeme.registry.metaregistry.MetaRegistryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Servlet for the http interface of the meta registry.
 * @author lrosenberg.
 */
@SuppressWarnings("serial")
@Monitor (producerId = "MetaRegistryServlet", category = "servlet", subsystem = "distributeme")
public class MetaRegistryServlet extends BaseRegistryServlet{
	/**
	 * Supported operations.
	 * @author lrosenberg.
	 *
	 */
	private static enum Operation{
		/**
		 * Bind a service descriptor.
		 */
		BIND,
		/**
		 * Unbind a service descriptor.
		 */
		UNBIND,
		/**
		 * List of binded services.
		 */
		LIST,
		/**
		 * Resolve a service operation.
		 */
		RESOLVE,
		/**
		 * Notify bind.
		 */
		NBIND,
		/**
		 * Notify unbind.
		 */
		NUNBIND,
		/**
		 * Ping.
		 */
		PING,
		/**
		 * Identify the id of this registry in the cluster.
		 */
		IDENTIFY,
	};
	
	/**
	 * Singleton instance.
	 */
	private static MetaRegistry registry = MetaRegistryImpl.getInstance();
	/**
	 * Config.
	 */
	private static final MetaRegistryConfig config = MetaRegistryConfig.create();
	/**
	 * The logger.
	 */
	private static Logger log = LoggerFactory.getLogger(MetaRegistryServlet.class);
	
	@Override
	protected void moskitoDoGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
	
		String op = getOperation(req);
		//NOTE this can throw an illegal argument exception, but since this servlet will never be exposed, its ok, and there
		//is no further error handling needed.
		Operation operation = Operation.valueOf(op.toUpperCase());

		switch(operation){
		case BIND:
			bind(req, res);
			break;
		case UNBIND:
			unbind(req, res);
			break;
		case LIST:
			list(req, res);
			break;
		case RESOLVE:
			resolve(req, res);
			break;
		case IDENTIFY:
			identify(req, res);
			break;
		case PING:
			ping(req, res);
			break;
		case NBIND:
			notifyBind(req, res);
			break;
		case NUNBIND:
			notifyUnbind(req, res);
			break;

		default:
			throw new IllegalArgumentException("Parseable but (yet) unsupported operation.");
		}
	}
	

	/**
	 * Processes bind request.
	 * @param req the http servlet request object.
	 * @param res the http servlet response object.
	 */
	private void bind(HttpServletRequest req, HttpServletResponse res){
		
		String registrationId = getId(req);
		ServiceDescriptor toBind = ServiceDescriptor.fromRegistrationString(registrationId);
		boolean success = registry.bind(toBind);
		sendBooleanResponse(res, success);
	}

	/**
	 * Processes unbind request.
	 * @param req the http servlet request object.
	 * @param res the http servlet response object.
	 */
	private void unbind(HttpServletRequest req, HttpServletResponse res){
		String registrationId = getId(req);
		ServiceDescriptor toUnBind = ServiceDescriptor.fromRegistrationString(registrationId);
		boolean success = registry.unbind(toUnBind);
		sendBooleanResponse(res, success);
			
	}

	/**
	 * Processes resolve request.
	 * @param req the http servlet request object.
	 * @param res the http servlet response object.
	 */
	private void resolve(HttpServletRequest req, HttpServletResponse res){
		String serviceId = getId(req);
		ServiceDescriptor toResolve = ServiceDescriptor.fromResolveString(serviceId);
		ServiceDescriptor service = registry.resolve(serviceId);
		
		if (service==null) {
			if (config.isRegistryParentLookup()) {
				service = RegistryUtil.resolve(toResolve, config);
				if (service == null) {
					sendBooleanResponse(res, false);
				} else {
					sendResponse(res, service.getRegistrationString());
				}
			} else {
				sendBooleanResponse(res, false);
			}
		} else
			sendResponse(res, service.getRegistrationString());
	}


	
	//yes, this isn't nice, but it had to be worked out fast.
	/**
	 * Processes list request.
	 * @param req the http servlet request object.
	 * @param res the http servlet response object.
	 */
	private void list(HttpServletRequest req, HttpServletResponse res){//NOPMD
		
		StringBuilder m = new StringBuilder();
		m.append("<?xml version=\"1.0\"?>");
		m.append("<services>");
		List<ServiceDescriptor> bindinds = registry.list();
		for (ServiceDescriptor service : bindinds){
			m.append("<service serviceId=\"").append(service.getServiceId()).append("\" host=\"").append(service.getHost()).append("\"");
			m.append(" port =\"").append(service.getPort()).append("\"");
			m.append(" protocol =\"").append(service.getProtocol()).append("\"");
			m.append(" instanceId =\"").append(service.getInstanceId()).append("\"");
			m.append(" globalId =\"").append(service.getGlobalServiceId()).append("\"");
			m.append(" registrationString =\"").append(service.getRegistrationString()).append("\"");
			m.append("/>");
		}
		m.append("</services>");
		
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
			log.warn("sendResponse(res, "+message+")", e);
		}finally{
			if (out!=null){
				try{
					out.close();
				}catch(IOException ignored){//NOPMD
				}
			}
		}
		
	}
	
	/**
	 * Sends internal cluster identification to the output. This is used to determine which instance of the cluster is one himself. 
	 * @param req
	 * @param res
	 */
	private void identify(HttpServletRequest req, HttpServletResponse res){
		log.debug("incoming identify");
		sendResponse(res, Cluster.INSTANCE.getId());	
	}
	
	private void ping(HttpServletRequest req, HttpServletResponse res){
		log.debug("incoming ping");
		sendResponse(res, Cluster.INSTANCE.getId());	
	}
	
	/**
	 * Called as notification on a bind from another registry.
	 * @param req
	 * @param res
	 */
	private void notifyBind(HttpServletRequest req, HttpServletResponse res){
		String registrationId = getId(req);
		log.debug("Notified about remote bind (registration) "+registrationId);
		ServiceDescriptor toBind = ServiceDescriptor.fromRegistrationString(registrationId);
		registry.remoteBind(toBind);
		sendBooleanResponse(res, Boolean.TRUE);
	}

	/**
	 * Called as notification on an unbind from another registry.
	 * @param req
	 * @param res
	 */
	private void notifyUnbind(HttpServletRequest req, HttpServletResponse res){
		String registrationId = getId(req);
		log.debug("Notified about remote unbind (deregistration) "+registrationId);
		ServiceDescriptor toUnBind = ServiceDescriptor.fromRegistrationString(registrationId);
		registry.remoteUnbind(toUnBind);
		sendBooleanResponse(res, Boolean.TRUE);
	}

}
