package org.distributeme.test.jsonrpc.ssl;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.anoprise.metafactory.ServiceFactory;
import org.distributeme.test.jsonrpc.A;
import org.distributeme.test.jsonrpc.EchoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

/**
 * Client demonstrating JSONRPC call to http endpoint using SSL.
 * SSL can be enabled using distributeme.json.ssl=true.
 * If SSL specified keystore location (distributeme.json.ssl.keystore),
 * keystore password (distributeme.json.ssl.password) and keypassword (distributeme.json.ssl.keyPassword) should be defined
 * Optionally trust keystore location (distributeme.json.ssl.truststore) and trust keypassword(distributeme.json.ssl.trustPassword) can be specified.
 * Demo service was run with following parameters:
 * -Ddistributeme.json.ssl=true  -Ddistributeme.json.ssl.password=375ui345 -Ddistributeme.json.ssl.keystore=./distributeme-test/java/org/distributeme/test/jsonrpc/ssl/ec -Ddistributeme.json.ssl.keyPassword=sdf63fd
 *
 */
public class SslEchoClient {
    private static Logger log = LoggerFactory.getLogger(SslEchoClient.class);

    public static void main(String a[]) throws Exception{
        addRemoteFactory(EchoService.class,
				"org.distributeme.test.jsonrpc.jsonrpc.generated.ClientEchoServiceFactory");

        EchoService echoServiceGen = MetaFactory.get(EchoService.class, Extension.JSONRPC);
        System.setProperty("distributeme.json.ssl", "true");
        System.setProperty("distributeme.json.ssl.password", "375ui345");
        System.setProperty("distributeme.json.ssl.keystore", "./distributeme-test/java/org/distributeme/test/jsonrpc/ssl/ec");
        System.setProperty("distributeme.json.ssl.keyPassword", "sdf63fd");

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
