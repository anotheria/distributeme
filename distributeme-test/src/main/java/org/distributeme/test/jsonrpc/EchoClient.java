package org.distributeme.test.jsonrpc;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.anoprise.metafactory.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: vitaly
 * Date: 2/6/11
 * Time: 12:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class EchoClient {
    private static Logger log = LoggerFactory.getLogger(EchoClient.class);

    public static void main(String a[]) throws Exception{
        addRemoteFactory(EchoService.class,
				"org.distributeme.test.jsonrpc.jsonrpc.generated.ClientEchoServiceFactory");

        EchoService echoServiceGen = MetaFactory.get(EchoService.class, Extension.JSONRPC);
        System.out.println("WARNING DEACTIVATED TEMPORARLY");

        long resultGen = echoServiceGen.echo(20);
        System.out.println(resultGen);
        echoServiceGen.echo(41);
        echoServiceGen.echo(63);
        echoServiceGen.echoObjectParam1(new A());
        echoServiceGen.echoObjectParam(new A(30));
        A a1 = echoServiceGen.echoManyParams(30, "40", 19L);
        System.out.println(a1);
    }

    /**
	 * Loads a factory via its class name and adds it to the meta factory as remote factory. This is used to prevent compile time dependencies between written
	 * and generated code.
	 *
	 * @param interf
	 *            - interface
	 * @param factoryClassName
	 *            - factory name itself
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Service> void addRemoteFactory(Class<T> interf, String factoryClassName) {
		try {
			MetaFactory.addFactoryClass(interf, Extension.JSONRPC, (Class<ServiceFactory<T>>) Class.forName(factoryClassName));
		} catch (ClassNotFoundException cnfe) {
			log.error(MarkerFactory.getMarker("FATAL"), "Couldn't load factory class " + factoryClassName + " for service: " + interf);
		}
	}
}
