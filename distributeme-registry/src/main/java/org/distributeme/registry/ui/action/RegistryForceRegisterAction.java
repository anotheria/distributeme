package org.distributeme.registry.ui.action;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * This action sends out a signal that forces all other services to re-register.
 * @author lrosenberg
 */
public class RegistryForceRegisterAction extends BaseRegistryAction implements Action {

	private static Logger log = LoggerFactory.getLogger(RegistryForceRegisterAction.class);

	@Override
	public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {



		try {
			DatagramPacket outgoing = new DatagramPacket(new String("register").getBytes(), "register".length());
			DatagramSocket socket = new DatagramSocket();
			socket.setBroadcast(true);
			outgoing.setAddress(InetAddress.getByAddress(new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255}));
			for (int i = 9250; i < 9400; i++) {
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
