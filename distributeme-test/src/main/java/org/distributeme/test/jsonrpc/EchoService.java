package org.distributeme.test.jsonrpc;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.core.ServiceDescriptor;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: vitaly
 * Date: 2/5/11
 * Time: 9:12 PM
 * To change this template use File | Settings | File Templates.
 */
@DistributeMe(
		//initcode = {"lu.netservices.cdate.business.shared.BusinessTierConfigurationUtil.configureMetaFactoryForProduction();"},
        protocols = {ServiceDescriptor.Protocol.JSONRPC, ServiceDescriptor.Protocol.RMI}
)
public interface EchoService extends Service {
    /**
     * Simply returns the given parameter.
     *
     * @param aValue - given long value
     * @return long value
     * @throws EchoServiceException backend failure
     */
    long echo(long aValue) throws EchoServiceException;

    /**
     * Simply returns the given object.
     *
     * @param aValue - given object
     * @return object
     * @throws EchoServiceException backend failure
     */
    Object echoObjectParam1(Object aValue) throws EchoServiceException;

     A echoObjectParam(A aValue) throws EchoServiceException;

     A echoManyParams(int ind, String strNum, Long incremt) throws EchoServiceException, IOException;
}
