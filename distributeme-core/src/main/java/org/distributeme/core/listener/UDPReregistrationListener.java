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
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

/**
 * This listener activates a UDP socket and listens for the incoming packets. If the packet contains the register command,
 * it triggers a reregistration.
 *
 * @author lrosenberg
 * @since 22.02.15 17:51
 */
public class UDPReregistrationListener implements ServerLifecycleListener{

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(UDPReregistrationListener.class);

	/**
	 * Cmd string for registration.
	 */
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

	/**
	 * Starts a server on a given port.
	 * @param port
	 */
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
							String command = new String(incomingPacket.getData(), Charset.defaultCharset());
							command = command.trim();
							System.out.println("Incoming command: "+command+ '.');

							if (Objects.equals(command, CMD_REGISTER))
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

	/**
	 * Registration command.
	 */
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
