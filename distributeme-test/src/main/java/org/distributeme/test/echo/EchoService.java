package org.distributeme.test.echo;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.ServerListener;
import org.distributeme.core.listener.ServerLifecycleSysOutPrinterListener;

import java.io.Serializable;
import java.util.HashMap;

@DistributeMe()
@ServerListener(listenerClass=ServerLifecycleSysOutPrinterListener.class)
public interface EchoService extends Service {
	long echo(long parameter) throws EchoServiceException;

	void printHello() throws EchoServiceException;

	String methodWithMultipleParameters(String param1, String param2, String param3) throws EchoServiceException;

	Echo echo(Echo in) throws EchoServiceException;

	void throwException(String message) throws EchoServiceException;

	void dontThrowException(String message);

	/**
	 * ...
	 * @param aValue
	 * @return
	 * @throws EchoServiceException
	 */
	<T extends Serializable> T echo(T aValue) throws EchoServiceException;

	<T extends Serializable, Y extends Number> T echo(T aValue, Y aParameter) throws EchoServiceException;
	
	/**
	 * This method illustrates that modifying a hashmap on the server side doesn't transport it back to client. In case someone was unsure.
	 * @param params
	 * @throws EchoServiceException
	 */
	void testCallByRef(HashMap<String,String> params) throws EchoServiceException;
	
	void testCallByRefWithInterceptors(HashMap<String,String> params) throws EchoServiceException;

}
