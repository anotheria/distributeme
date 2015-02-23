package org.distributeme.test.jsonrpc;

import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * Created by IntelliJ IDEA.
 * User: vitaly
 * Date: 2/5/11
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class EchoServiceFactory implements ServiceFactory<EchoService> {
    @Override
    public EchoService create() {
        return new EchoServiceImpl();
    }
}
