package org.distributeme.core.listener;

import org.distributeme.core.RMIRegistryUtil;
import org.distributeme.core.RegistryUtil;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.util.LocalServiceDescriptorStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 22.02.15 17:51
 */
public class UDPReregistrationListener implements ServerLifecycleListener{

	private static Logger log = LoggerFactory.getLogger(UDPReregistrationListener.class);

	public static final String CMD_REGISTER = "register";

	@Override
	public void afterStart() {
		int port = RMIRegistryUtil.getRmiRegistryPort();
		log.debug("Trying to setup incoming UDP Server on port: "+port);
		startUdpServer(port);
	}

	@Override
	public void beforeShutdown() {

	}

	private void startUdpServer(int port){
		try {
			final DatagramSocket serverSocket = new DatagramSocket(port);
			final Thread receiver = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						DatagramPacket incomingPacket = new DatagramPacket(new byte[100], 100);
						try {
							serverSocket.receive(incomingPacket);
							String command = new String(incomingPacket.getData());
							command = command.trim();
							System.out.println("Incoming command: "+command+".");

							if (command != null && command.equals(CMD_REGISTER))
								register();
						} catch (IOException e) {
							log.warn("Can't parse incoming packet", e);
						}
					}
				}
			});
			receiver.start();

		}catch(IOException e){
			log.warn("Can't create server socket", e);
		}
	}

	private void register(){
		List<ServiceDescriptor> descriptors = LocalServiceDescriptorStore.getInstance().getServiceDescriptors();
		System.out.println("Have to register following descriptors: "+descriptors);
		for (ServiceDescriptor descriptor : descriptors){
			if (!RegistryUtil.bind(descriptor)){
				log.error("Couldn't re-bind myself to the central registry at "+RegistryUtil.describeRegistry()+" for "+descriptor);
				System.err.println("Couldn't re-bind myself at the central registry at "+RegistryUtil.describeRegistry()+" for "+descriptor);
			}
		}
	}


}
