package org.distributeme.registry.servlet;

import net.anotheria.moskito.web.MoskitoHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Base servlet for registry functionality.
 * @author lrosenberg
 */
public abstract class BaseRegistryServlet extends MoskitoHttpServlet{
	
	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(BaseRegistryServlet.class);

	/**
	 * Result constant for sucess case.
	 */
	public static final String RESULT_SUCCESS = "SUCCESS";
	/**
	 * Result constant for error case.
	 */
	public static final String RESULT_ERROR   = "ERROR";
	
	/**
	 * Returns the value of the submitted id parameter.
	 * @param req http servlet request object.
	 * @return value of the parameter. 
	 */
	protected String getId(HttpServletRequest req){
		return getRequiredParameter(req, "id");
	}

	/**
	 * Returns a required parameter of given name. If the parameter is not present an illegal argument exception is thrown.
	 * @param req http servlet request object.
	 * @param parameterName name of the parameter.
	 * @return value of the parameter.
	 */
	protected String getRequiredParameter(HttpServletRequest req, String parameterName){
		String parameter = req.getParameter(parameterName);
		if (parameter==null || parameter.length()==0)
			throw new IllegalArgumentException("Parameter \""+parameterName+"\" is required but empty.");
		return parameter;
		
	}
	
	/**
	 * Returns the value of the submitted operation parameter.
	 * @param req
	 * @return
	 */
	protected String getOperation(HttpServletRequest req){
		String op = req.getPathInfo();
		if (op==null)
			op = "";
		while (op.length()>0 && op.charAt(0)=='/')
			op = op.substring(1);
		return op;
	}

	/**
	 * Sends success or error to into the response stream.
	 * @param res http servlet response object.
	 * @param value value to send.
	 */
	protected void sendBooleanResponse(HttpServletResponse res, boolean value){
		sendResponse(res, value  ? RESULT_SUCCESS : RESULT_ERROR);
	}
	
	/**
	 * Sends a message response to the client. The message is sent as plain text.
	 * @param res http servlet response object.
	 * @param message message to send.
	 */
	protected void sendResponse(HttpServletResponse res, String message){
		res.setContentLength(message.getBytes().length);
		res.setContentType("text/plain");
		OutputStream out = null;
		try{
			 out = res.getOutputStream();
			 out.write(message.getBytes());
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

}
