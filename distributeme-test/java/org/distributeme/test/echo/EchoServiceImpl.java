package org.distributeme.test.echo;

import java.io.Serializable;
import java.util.HashMap;

public class EchoServiceImpl implements EchoService{

	@Override
	public void throwException(String message) throws EchoServiceException {
		throw new EchoServiceException(message);
	}

	@Override
	public Echo echo(Echo in) {
		in.setReply(System.currentTimeMillis());
		return in;
	}

	@Override
	public long echo(long parameter) {
		return parameter;
	}

	@Override
	public void printHello() {
		System.out.println("Hello World!");
	}

	@Override
	public String methodWithMultipleParameters(String param1, String param2,
			String param3) {
		return param1+param2+param3;
	}

	@Override
	public <T extends Serializable> T echo(T aValue)
			throws EchoServiceException {
		return aValue;
	}

	@Override
	public <T extends Serializable, Y extends Number> T echo(T aValue,
			Y aParameter) throws EchoServiceException {
		return aValue;
	}

	@Override
	public void dontThrowException(String message) {
		System.out.println(message);
	}

	@Override
	public void testCallByRef(HashMap<String, String> params)
			throws EchoServiceException {
		params.put("SERVERSIDE", "Greetinx");
	}

	@Override
	public void testCallByRefWithInterceptors(HashMap<String, String> params)
			throws EchoServiceException {
		params.put("SERVERSIDE", "Greetinx with Interceptors");
	}
	
	
}
