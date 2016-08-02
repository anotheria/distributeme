package org.distributeme.support.eventservice;

import net.anotheria.anoprise.eventservice.EventTransportShell;
import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.SupportService;
import org.distributeme.core.ServiceDescriptor;


@DistributeMe( factoryClazz=EventServiceRMIBridgeServiceFactory.class)
@SupportService
public interface EventServiceRMIBridgeService extends Service {
	void deliverEvent(EventTransportShell shell) throws EventServiceRMIBridgeServiceException;
	void registerRemoteConsumer(String channelName, ServiceDescriptor myReference) throws EventServiceRMIBridgeServiceException;
	void registerRemoteSupplier(String channelName, ServiceDescriptor myReference) throws EventServiceRMIBridgeServiceException;
	
	/**
	 * Returns the instanceid of this instance. This is needed to separate between crashed/ended and restarted services on same port.
	 * @return
	 * @throws EventServiceRMIBridgeServiceException
	 */
    String getInstanceId() throws EventServiceRMIBridgeServiceException;

}
