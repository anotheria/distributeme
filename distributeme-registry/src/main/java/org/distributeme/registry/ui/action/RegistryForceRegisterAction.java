package org.distributeme.registry.ui.action;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import org.distributeme.registry.metaregistry.MetaRegistryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;

/**
 * This action sends out a signal that forces all other services to re-register.
 * @author lrosenberg
 */
public class RegistryForceRegisterAction extends BaseRegistryAction implements Action {

	private static Logger log = LoggerFactory.getLogger(RegistryForceRegisterAction.class);

	private MetaRegistryConfig registryConfig = MetaRegistryConfig.create();

	@Override
	public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {

		int minPort = registryConfig.getRegistryPortMin();
		int maxPort = registryConfig.getRegistryPortMax();

		try {
			DatagramPacket outgoing = new DatagramPacket("register".getBytes(Charset.defaultCharset()), "register".length());
			DatagramSocket socket = new DatagramSocket();
			socket.setBroadcast(true);
			outgoing.setAddress(InetAddress.getByAddress(new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255}));
			for (int i = minPort; i < maxPort; i++) {
				outgoing.setPort(i);
				socket.send(outgoing);
			}
			addFlashMessage(req, "Sent re-register request");
		}catch(IOException e){
			addFlashMessage(req, "Re-register request failed: "+e.getMessage());
			log.error("Can't sent registration request", e);
		}

		return mapping.redirect();
	}

}
