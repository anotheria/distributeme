package org.distributeme.support.eventservice;

import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventService;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.EventServiceListener;
import net.anotheria.anoprise.eventservice.EventTransportShell;
import net.anotheria.anoprise.eventservice.ProxyType;
import net.anotheria.anoprise.eventservice.RemoteEventChannelConsumerProxy;
import net.anotheria.anoprise.eventservice.RemoteEventChannelSupplierProxy;
import net.anotheria.anoprise.eventservice.RemoteEventChannelSupportFactory;
import net.anotheria.util.IdCodeGenerator;
import org.distributeme.core.RMIRegistryUtil;
import org.distributeme.core.RegistryUtil;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.ServiceDescriptor.Protocol;
import org.distributeme.core.util.EventServiceRegistryUtil;
import org.distributeme.support.eventservice.generated.EventServiceRMIBridgeServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiMeRemoteEventChannelRMISupport implements RemoteEventChannelSupportFactory, EventServiceListener{
	
	private static ServiceDescriptor descriptor = null;
	
	private static final String INSTANCE_ID = IdCodeGenerator.generateCode(10);
	
	private static final ConcurrentHashMap<ServiceDescriptor, EventServiceRMIBridgeService> bridges = new ConcurrentHashMap<ServiceDescriptor, EventServiceRMIBridgeService>();

	private static EventService es ;
	
	private ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	private static Logger LOG = LoggerFactory.getLogger(DiMeRemoteEventChannelRMISupport.class);
	
	DiMeRemoteEventChannelRMISupport(){
		
		descriptor = RegistryUtil.createLocalServiceDescription(Protocol.RMI, 
				"org_distributeme_support_eventservice_EventServiceRMIBridgeService", INSTANCE_ID, RMIRegistryUtil.getRmiRegistryPort());
		es = EventServiceFactory.createEventService();
	}
	
	protected ServiceDescriptor getHomeReference(){
		return descriptor;
	}

	@Override
	public RemoteEventChannelConsumerProxy createRemoteEventChannelConsumerProxy(String channelName) {
		return new DiMeRemoteEventChannelConsumerProxy(channelName);
	}

	@Override
	public RemoteEventChannelSupplierProxy createRemoteEventChannelSupplierProxy(String channelName) {
		return new DiMeRemoteEventChannelSupplierProxy(channelName);
	}
 
	@Override
	public void channelCreated(String channelName, ProxyType type) {
		
		switch(type){
		case PUSH_CONSUMER_PROXY:
			localConsumerProxyCreated(channelName);
			break;
		case PUSH_SUPPLIER_PROXY:
			localSupplierProxyCreated(channelName);
			break;
		default:
			throw new AssertionError("Unknown proxy type "+type );
		}
	}
	
	private EventServiceRMIBridgeService getBridge(ServiceDescriptor descriptor){
		EventServiceRMIBridgeService ret = bridges.get(descriptor);
		if (ret!=null)
			return ret;
		try{
			@SuppressWarnings("unchecked")Class<EventServiceRMIBridgeService> clazz = (Class<EventServiceRMIBridgeService>)Class.forName("org.distributeme.support.eventservice.generated.RemoteEventServiceRMIBridgeServiceStub");
			Constructor<EventServiceRMIBridgeService> c = clazz.getConstructor(ServiceDescriptor.class);
			EventServiceRMIBridgeService newBridge = c.newInstance(descriptor);
			EventServiceRMIBridgeService old = bridges.putIfAbsent(descriptor, newBridge);
			return old == null ? newBridge : old;
		}catch(ClassNotFoundException e){
			throw new AssertionError("Misconfigured? can't find org.distributeme.support.eventservice.generated.RemoteEventServiceRMIBridgeServiceStub");
		}catch(NoSuchMethodException e){
			throw new AssertionError("Misconfigured? can't find org.distributeme.support.eventservice.generated.RemoteEventServiceRMIBridgeServiceStub constructor");
		}catch(Exception e){
			LOG.error("getBridge("+descriptor+")", e);
		}
		return null;
	}
	
	private void localConsumerProxyCreated(String channelName){
		ServiceDescriptor me = getHomeReference();
		
		List<ServiceDescriptor> suppliers = EventServiceRegistryUtil.registerConsumerAtRegistryAndGetSuppliers(channelName, descriptor);
		
		for (ServiceDescriptor s : suppliers){
			if (me.equals(s)){
				LOG.debug("Skipped registering at myself");
				continue;
			}
			registerAsConsumerAtRemoteSupplier(channelName, s);
		}
	}
	
	private void registerAsConsumerAtRemoteSupplier(String channelName, ServiceDescriptor supplier){
		EventServiceRMIBridgeService bridge = getBridge(supplier);
		if (bridge==null){
			notifyBrokenSupplier(supplier);
			return;
		}
		
		try{
			String bridgeInstanceId = bridge.getInstanceId();
			if (!(bridgeInstanceId.equals(supplier.getInstanceId()))){
				LOG.info("Instanceid mismatch, expected "+supplier.getInstanceId()+", received "+bridgeInstanceId+" throwing away");
				notifyBrokenSupplier(supplier);
				return;
			}
			LOG.debug("Registering @ "+supplier);
			bridge.registerRemoteConsumer(channelName, descriptor);
		}catch(EventServiceRMIBridgeServiceException e){
			LOG.error("can't connect to : registerAsConsumerAtRemoteSupplier("+channelName+", "+supplier+")", e);
			notifyBrokenSupplier(supplier);
		}catch(RuntimeException e){
			LOG.error("can't connect to : registerAsConsumerAtRemoteSupplier("+channelName+", "+supplier+")", e);
			notifyBrokenSupplier(supplier);
		}
	}
	
	private void notifyBrokenSupplier(ServiceDescriptor supplier){
		try{
			EventServiceRegistryUtil.notifySupplierNotAvailable(supplier);
		}catch(Exception e){
			LOG.warn("notifyBrokenSupplier("+supplier+") failed", e);
		}
		bridges.remove(supplier);
	}

	private void notifyBrokenConsumer(ServiceDescriptor consumer){
		try{
			EventServiceRegistryUtil.notifyConsumerNotAvailable(consumer);
		}catch(Exception e){
			LOG.warn("notifyBrokenConsumer("+consumer+") failed ",e );
		}
		bridges.remove(consumer);
	}
	
	void notifyBrokenConsumer(RemoteConsumerWrapper wrapper){
		LOG.debug("NOTIFY brokenConsumer: "+wrapper);
		EventChannel channel = es.obtainEventChannel(wrapper.getChannelName(), ProxyType.REMOTE_CONSUMER_PROXY);
		((DiMeRemoteEventChannelConsumerProxy)channel).removeRemoteConsumer(wrapper);
		notifyBrokenConsumer(wrapper.getHomeReference());
		
	}

	private void localSupplierProxyCreated(String channelName){
		final ServiceDescriptor me = getHomeReference();
		
		List<ServiceDescriptor> consumers = EventServiceRegistryUtil.registerSupplierAtRegistryAndGetConsumers(channelName, me);
		
		if (LOG.isDebugEnabled())
			LOG.debug("Consumers: "+consumers);
		
		for (ServiceDescriptor c : consumers){
			EventServiceRMIBridgeService bridge = getBridge(c);
			if (bridge==null){
				notifyBrokenConsumer(c);
				continue;
			}
			
			try{
				String bridgeInstanceId = bridge.getInstanceId();
				if (!(bridgeInstanceId.equals(c.getInstanceId()))){
					LOG.debug("Instanceid mismatch, expected "+c.getInstanceId()+", received "+bridgeInstanceId+" throwing away");
					notifyBrokenConsumer(c);
					continue;
				}
				LOG.debug("Registering @ "+c);
				bridge.registerRemoteSupplier(channelName, me);
			}catch(EventServiceRMIBridgeServiceException e){
				LOG.error("localSupplierProxyCreated", e);
			}
		}
	}

	@Override
	public void channelDestroyed(String channelName, ProxyType type) {
		LOG.info("Channel "+channelName+" destroyed");
	}

	/**
	 * Called to initialize eventservice remoting. DiMe generated services are doing this in server startup automatically. Only needed in 
	 * standalone apps.
	 */
	public static final void initEventService(){
		try{
			EventServiceRMIBridgeServer.init();
			EventServiceRMIBridgeServer.createServiceAndRegisterLocally();
			//fix null port - overwrite descriptor
			descriptor = RegistryUtil.createLocalServiceDescription(Protocol.RMI, 
					"org_distributeme_support_eventservice_EventServiceRMIBridgeService", INSTANCE_ID, RMIRegistryUtil.getRmiRegistryPort());
		}catch(Exception e){
			LOG.error("Can't init eventservice - probably running without events", e);
		}
	}
	
	/**
	 * Registers a new remote consumer at local eventservice.
	 * @param channelName
	 * @param myReference
	 */
	void registerRemoteConsumer(String channelName, ServiceDescriptor myReference){
		EventChannel channel = es.obtainEventChannel(channelName, ProxyType.REMOTE_CONSUMER_PROXY);
		LOG.debug("REGISTER REMOTE CONSUMER @ channel "+channel+", consumer: "+myReference);
		RemoteConsumerWrapper wrapper = new RemoteConsumerWrapper(this, channelName, myReference, getBridge(myReference));
		((DiMeRemoteEventChannelConsumerProxy)channel).addRemoteConsumer(wrapper);
		LOG.debug("REGISTER REMOTE CONSUMER @ channel "+channel+", consumer: "+myReference+" DONE!");
	}
	
	/**
	 * Registeres a new remote supplier at local eventservice.
	 * @param channelName
	 * @param myReference
	 */
	void registerRemoteSupplier(final String channelName, final ServiceDescriptor myReference){
		LOG.debug("Register remote supplier "+myReference+" for channel "+channelName+" called.");
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				try{
					registerAsConsumerAtRemoteSupplier(channelName, myReference);
				}catch(Exception e){
					LOG.error("Can't register as consumer at remote supplier, channel: "+channelName+", myRef: "+myReference);
				}
			}
		});
	}

	public void deliverEvent(EventTransportShell shell){
		EventChannel channel = es.obtainEventChannel(shell.getChannelName(), ProxyType.REMOTE_SUPPLIER_PROXY);
		((DiMeRemoteEventChannelSupplierProxy)channel).deliverEvent(shell.getData());
	}

}
 